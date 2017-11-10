import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class CentralServer{

    //Connection port and  server socket
    private static final int CONN_PORT = 7171;
    private static ServerSocket serverSocket = null;

    public static void main(String args[]) throws IOException{
        
        //Create database when the server starts
        NapsterDatabase database = new NapsterDatabase();

        //Create server socket on port 7171
        try{
            serverSocket = new ServerSocket(CONN_PORT);
        }
        catch(IOException ex){
            System.out.println("Cannot connect to port");
            System.exit(1);
        }

        //Continuously listen for client connections
        int clientNumber = 0;
        do{            
            Socket clientSocket = serverSocket.accept();
            clientNumber++; 
            System.out.println("Client " + clientNumber + " connected");

            //Create and start a new thread for each client with the client socket and number
            ClientHandler clientHandler = new ClientHandler(clientSocket, clientNumber);
            clientHandler.start();
        }while(true);
    }
}

class ClientHandler extends Thread{
    
    //Control connection socket 
    private Socket connectionSocket; 
    private BufferedReader inFromClient;
    private int clientNumber;

    //Creates client
    public ClientHandler(Socket connectionSocket, int clientNumber){
        this.connectionSocket = connectionSocket;
        this.clientNumber = clientNumber;
        
        //Get control input from client
        try{
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        }
        catch(IOException ex){
        }
    }

    //The logic for the client-server connection
    public void run(){
        
        //Continue logic until client closes connection
        boolean cont = true;
        do{
            String message;
            try{
                if((message = inFromClient.readLine()).equals("quit")){
                    cont = false;
                }
                else{
                    String[] messageParts = message.split("\\s");
                    NapsterDatabase.addUser(messageParts);
                    NapsterDatabase.printUsers();   
                }
            }
            catch(IOException ex){
                System.out.println("Error reading from client");
            }
        }while(cont);

        //Close control connection
        try{
            connectionSocket.close();
            System.out.println("Client " + clientNumber + " disconnected");
        }
        catch(IOException ex){
            System.out.println("Unable to disconnect: (IO EX: " + ex + ")");
        }
    }
}