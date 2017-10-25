import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;

//Class performs host functions
public class Host{

    //Class attributes
    private final String EOF = "EOF";
    private Socket socket;
    private DataOutputStream outToServer;

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
            System.out.println("Problem writing to server");
        }
    }
}