import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
/************************************************************
 * FTP Server Application
 * Author: Nick Spruit
 * Date: October 17, 2017
 * Desc: This application creates a server that can handle 
 * multiple clients. Using a control connection, the server 
 * can get commands from each client, including LIST, RETR, 
 * STOR, and QUIT. 
 ***********************************************************/
public class FTPServer{

    //Connection port and  server socket
    private static final int CONN_PORT = 7171;
    private static ServerSocket serverSocket = null;
    public static void main(String args[]) throws IOException{

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
    
    //Constants for directory, EOF, and messages
    private static final String FILE_DIRECTORY = "serverFiles";
    private static final String EOF = "eof";
    private static final String FILE_FOUND = "200";
    private static final String FILE_NOT_FOUND = "550";
    
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
        boolean cont = true;
        do{
            try{

                //Get control message
                String[] tokens = inFromClient.readLine().split("\\s");
                if(tokens.length >= 2){

                    //Set up data connection using client IP and port from client
                    String ipAddress = connectionSocket.getInetAddress().getHostAddress();
                    int port = Integer.parseInt(tokens[0]);
                    Socket dataSocket = new Socket(ipAddress, port);
                    DataOutputStream dataToClient = new DataOutputStream(dataSocket.getOutputStream());    
                    
                    //The client wants a list of the files on the server
                    if(tokens[1].equals("list:")){
                        File folder = new File(FILE_DIRECTORY);

                        //Read each file in the server directory
                        for(File file : folder.listFiles()){
                            dataToClient.writeBytes(file.getName() + '\n');
                        }
                        dataToClient.writeBytes(EOF + '\n');
                    }

                    //The client wants to retrieve a file from the server
                    else if(tokens[1].equals("retr:")){
                        File file;    

                        //Check if file exists on the server directory
                        if((file = FileClass.fileExists(FILE_DIRECTORY + "/" + tokens[2])) != null){
                            BufferedReader fileReader = new BufferedReader(new FileReader(file));
                            String fileLine;

                            //Write file to client line by line
                            dataToClient.writeBytes(FILE_FOUND + '\n');
                            while((fileLine = fileReader.readLine()) != null){
                                dataToClient.writeBytes(fileLine + '\n');
                            }
                            dataToClient.writeBytes(EOF + '\n');
                            fileReader.close();
                        }
                        else{
                            dataToClient.writeBytes(FILE_NOT_FOUND + '\n');
                        }
                    }

                    //The client wants to store a file on the server
                    else if(tokens[1].equals("stor:")){
                        String filePath = FILE_DIRECTORY + "/" + tokens[2];
                        BufferedReader dataFromClient = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                        String fileLine;    
                            
                        //File exists on the client side
                        if((fileLine = dataFromClient.readLine()).equals(FILE_FOUND)){
                            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath));
                            
                            //Write file line by line to server directory
                            while(!(fileLine = dataFromClient.readLine()).equals(EOF)){
                                fileWriter.write(fileLine + '\n');
                            }
                            fileWriter.close();
                        }
                        dataFromClient.close();
                    }

                    //Close data connection
                    dataSocket.close();
                    dataToClient.close();
                }

                //Quit reading client input
                else if(tokens[0].equals("quit")){
                    cont = false;
                }
            }
            catch(IOException ex){
                System.out.println("IO Exception: (IO EX: " + ex + ")");
            }
        }while(cont);

        //Close control connection
        try{
            System.out.println("Client " + clientNumber + " disconnected");
            connectionSocket.close();
        }
        catch(IOException ex){
            System.out.println("Unable to disconnect: (IO EX: " + ex + ")");
        }
    }
}