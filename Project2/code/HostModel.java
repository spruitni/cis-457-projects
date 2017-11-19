import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

//Class performs host functions
public class HostModel{

    //Class attributes
    private final String EOF = "EOF";
    private final String EOL = "EOL";
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private final String JSON_FILE_NAME = "fileInfo.json";
    private final String FILE_DIRECTORY = "../hostDescriptions/";
    private int port;
    private String ip;
    
    //Create a connection to server
    public boolean connectToServer(String serverIP, int serverPort){
        try{
            this.socket = new Socket(serverIP, serverPort);
            return true;
        }
        catch(IOException ex){
            System.out.println("Problem connecting to server");
            return false;
        }
    }
    
    //Sets up host connection details
    public void setup(String ipAddress, int portNumber){
        ip = ipAddress;
        port = portNumber;
    }
    
    //Send message to server (username, hostname, speed, )
    public void sendMessage(String message){
        try{
            outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(message + '\n');
        }
        catch(IOException ex){
            System.out.println("Problem writing to server: " + ex);            
        }
    }

    //Read file info from server
    public ArrayList<String[]> readSearchResults(){
        ArrayList<String[]> results = new ArrayList<String[]>();
        try{
            inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;
            while(!(message = inFromServer.readLine()).equals(EOF)){
                results.add(message.split(","));
            }
        }
        catch(IOException ex){
            System.out.println("Problem reading search results from server: " + ex);
        }
        finally{
            return results;
        }
    }


    //Upload file to current directory with file info
    public void uploadFile(String username){
        try{
            JSONObject obj1 = new JSONObject();
            JSONArray arr1 = new JSONArray();
            File hostDesc = new File(FILE_DIRECTORY +  username + "Files.txt");
            BufferedReader reader = new BufferedReader(new FileReader(hostDesc));
            String line;
            while((line = reader.readLine()) != null) {
                String[] descParts = line.split(",");
                JSONObject obj2 = new JSONObject();
                obj2.put("Username", username);
                obj2.put("Filename", descParts[0]);
                obj2.put("Description", descParts[1]);
                arr1.add(obj2);            
            }
            reader.close();
            obj1.put("Files", arr1);
            FileWriter file = new FileWriter(JSON_FILE_NAME);
            file.write(obj1.toJSONString());
            file.close();
        }
        catch(IOException ex){
            System.out.println("Problem writing JSON file: " + ex);
        }
    }
}

