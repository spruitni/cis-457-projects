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
        do{            
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected");
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start(); 
        }while(true);
    }
}

class ClientHandler extends Thread{
    private Socket connectionSocket; 
    private BufferedReader inFromClient;
    //private DataOutputStream outToClient;
    private static final String FILE_DIRECTORY = "serverFiles";
    private static final String EOF = "eof";
    private static final String FILE_FOUND = "200";
    private static final String FILE_NOT_FOUND = "550";

    public ClientHandler(Socket connectionSocket){
        this.connectionSocket = connectionSocket;
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
                String ipAddress = connectionSocket.getInetAddress().getHostAddress();
                int port = Integer.parseInt(tokens[0]);
                Socket dataSocket = new Socket(ipAddress, port);
                DataOutputStream dataToClient = new DataOutputStream(dataSocket.getOutputStream());    
                if(tokens.length >= 2){
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
                            System.out.println(FILE_NOT_FOUND + '\n');
                        }
                    }
                    else if(tokens[1].equals("stor:")){
                        //Checks if the file already exists
                        String filePath = FILE_DIRECTORY + "/" + tokens[2];
                        if(FileClass.fileExists(filePath) == null){
                            BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath));
                            BufferedReader dataFromClient = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                            String fileLine;
                            while(!(fileLine = dataFromClient.readLine()).equals(EOF)){
                                fileWriter.write(fileLine + '\n');
                            }
                            fileWriter.close();
                        }
                    }
                }
                else if(tokens[1].equals("quit")){
                    cont = false;
                }
                dataSocket.close();
                dataToClient.close();
            }
            catch(IOException ex){
            }
        }while(cont);
        try{
            connectionSocket.close();
        }
        catch(IOException ex){
            System.out.println("Unable to disconnect: (IO EX: " + ex + ")");
        }
    }



}