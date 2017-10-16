import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class FTPServer{
    private static final int CONN_PORT = 7171;
    private static ServerSocket serverSocket = null;
    
    public static void main(String args[]) throws IOException{

        try{
            serverSocket = new ServerSocket(CONN_PORT);
        }
        catch(IOException ex){
            System.out.println("Cannot connect to port");
            System.exit(1);
        }
        int clientNumber = 0;
        do{            
            Socket clientSocket = serverSocket.accept();
            clientNumber++; 
            System.out.println("Client " + clientNumber + " connected");
            ClientHandler clientHandler = new ClientHandler(clientSocket, clientNumber);
            clientHandler.start();
        }while(true);
    }
}

class ClientHandler extends Thread{
    private Socket connectionSocket; 
    private BufferedReader inFromClient;
    private int clientNumber;
    //private DataOutputStream outToClient;
    private static final String FILE_DIRECTORY = "serverFiles";
    private static final String EOF = "eof";
    private static final String FILE_FOUND = "200";
    private static final String FILE_NOT_FOUND = "550";

    public ClientHandler(Socket connectionSocket, int clientNumber){
        this.connectionSocket = connectionSocket;
        this.clientNumber = clientNumber;
        try{
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        }
        catch(IOException ex){
        }
    }
    public void run(){
        boolean cont = true;
        do{
            try{
                String[] tokens = inFromClient.readLine().split("\\s");
                if(tokens.length >= 2){

                    //Set up data connection
                    String ipAddress = connectionSocket.getInetAddress().getHostAddress();
                    int port = Integer.parseInt(tokens[0]);
                    Socket dataSocket = new Socket(ipAddress, port);
                    DataOutputStream dataToClient = new DataOutputStream(dataSocket.getOutputStream());    
                    
                    if(tokens[1].equals("list:")){
                        File folder = new File(FILE_DIRECTORY);
                        for(File file : folder.listFiles()){
                            dataToClient.writeBytes(file.getName() + '\n');
                        }
                        dataToClient.writeBytes(EOF + '\n');
                    }
                    else if(tokens[1].equals("retr:")){
                        File file;    
                        if((file = FileClass.fileExists(FILE_DIRECTORY + "/" + tokens[2])) != null){
                            BufferedReader fileReader = new BufferedReader(new FileReader(file));
                            String fileLine;
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
                    else if(tokens[1].equals("stor:")){
                        String filePath = FILE_DIRECTORY + "/" + tokens[2];
                        BufferedReader dataFromClient = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                        //File does not exist on file server
                        if(FileClass.fileExists(filePath) == null){
                            String fileLine;    
                            //File exists on the client side
                            if((fileLine = dataFromClient.readLine()).equals(FILE_FOUND)){
                                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath));
                                while(!(fileLine = dataFromClient.readLine()).equals(EOF)){
                                    fileWriter.write(fileLine + '\n');
                                }
                                fileWriter.close();
                                System.out.println("File stored!");
                            }
                            else{
                                System.out.println("File not stored!");
                            }
                        }
                        dataFromClient.close();
                    }
                    dataSocket.close();
                    dataToClient.close();
                }
                else if(tokens[0].equals("quit")){

                    cont = false;
                }
            }
            catch(IOException ex){
            }
        }while(cont);
        try{
            System.out.println("Client " + clientNumber + " disconnected");
            connectionSocket.close();
        }
        catch(IOException ex){
            System.out.println("Unable to disconnect: (IO EX: " + ex + ")");
        }
    }
}