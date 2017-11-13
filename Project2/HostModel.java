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
    private Socket socket;
    private DataOutputStream outToServer;
    private final String JSON_FILE_NAME = "fileInfo.json";
    private final String FILE_DIRECTORY = "hostFiles/";
    
    //Create a connection to server
    public void connectToServer(String serverIP, int serverPort){
        try{
            this.socket = new Socket(serverIP, serverPort);
        }
        catch(IOException ex){
            System.out.println("Problem connecting to server");
        }
    }

    //Send message to server (username, hostname, speed)
    public void sendMessage(String message){
        try{
            outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(message + '\n');
            if(message.equals("quit")){
                socket.close();
            }
        }
        catch(IOException ex){
            System.out.println("Problem writing to server" + ex);
        }
    }

    //Upload file to current directory with file info
    public void uploadFile(String username){
        try{
            JSONObject obj1 = new JSONObject();
            JSONArray arr1 = new JSONArray();
            File folder = new File(username + "Files");
            
            for(File file : folder.listFiles()){
                JSONObject obj2 = new JSONObject();
                obj2.put("Username", username);
                obj2.put("Filename", file.getName());
                obj2.put("Description", "some description here"); //WHERE DO THE DESCRIPTIONS COME FROM???
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
}

