import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

//Class performs host functions
public class Host{

    //Class attributes
    private final String EOF = "EOF";
    private Socket socket;
    private DataOutputStream outToServer;
    private final String JSON_FILE_NAME = "fileInfo.json";
    private final String FILE_DIRECTORY = "hostFiles/";

    //Create a connection to server
    public void connectToServer(String serverIP, int serverPort){
        try{
            socket = new Socket(serverIP, serverPort);
        }
        catch(IOException ex){
            System.out.println("Problem connecting to server");
        }
    }

    //Send data to server (such as username, hostname, conn. speed)
    public void sendData(String[] data){
        try{
            outToServer = new DataOutputStream(socket.getOutputStream());
            for(String line : data){
                outToServer.writeBytes(line + '\n');
            }
            outToServer.writeBytes(EOF + '\n');
            outToServer.close();
        }
        catch(IOException ex){
            System.out.println("Problem writing to server" + ex);
        }
    }

    //Upload file to current directory with file info
    public void uploadFile(){
        try{
            JSONObject obj1 = new JSONObject();
            JSONArray arr1 = new JSONArray();
            File folder = new File(FILE_DIRECTORY);
            
            for(File file : folder.listFiles()){
                JSONObject obj2 = new JSONObject();
                obj2.put("Filename", file.getName());
                obj2.put("Description", "some description here");
                arr1.add(obj2);                    
            }
            obj1.put("Files", arr1);
            FileWriter file = new FileWriter(JSON_FILE_NAME);
            file.write(obj1.toJSONString());
            file.close();
        }
        catch(IOException ex){
            System.out.println("Problem writing JSON file: " + ex);
        }
    }    

    /////////
    //TESTING
    /////////
    public static void main(String[] args){
        Host h = new Host();
        h.uploadFile();
        

    }
}
