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

    //Create a JSON file made of three fields
    public void uploadFile(String userName, String hostName, String connSpeed){
        JSONObject obj = new JSONObject();
        obj.put("userName", userName);
        obj.put("num", hostName);
        obj.put("balance", connSpeed);
        try{
            FileWriter file = new FileWriter(JSON_FILE_NAME);
            file.write(obj.toJSONString());
            file.close();
        }
        catch(IOException ex){
            System.out.println("Problem writing JSON file: " + ex);
        }
    }    

    /////////
    //TESTING
    /////////
    public static void main(String args[]){
        Host h = new Host();
        h.uploadFile("userName", "127.0.0.1", "Ethernet");

    }
}
