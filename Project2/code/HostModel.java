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
    private final String FILE_FOUND = "FILE_FOUND";
    private final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    private final String JSON_FILE_NAME = "fileInfo.json";
    private final String FILE_DESC_DIR = "../hostDescriptions/";
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private String hostFileDir;
    private String hostName;
    private ArrayList<String> hostFiles;
    private HostThread hostThread;
    private String hostIP;
    private int hostPort;
    private BufferedReader dataFromServer;
    private DataOutputStream dataToServer;
    private Socket hostSocket = null;
    
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
    public void setup(String hostIP, int hostPort){
        this.hostPort = hostPort;
        this.hostIP = hostIP;
        hostThread = new HostThread(hostPort, hostName, hostFiles, hostFileDir);
        hostThread.start();
    }
    
    //Shuts down the host server
    public void shutdown(){
        if(hostThread != null){
            hostThread.shutdown();
        }
        if(hostSocket != null){
            try{
                hostSocket.close();
            }
            catch(IOException ex){
                System.out.println("Error disconnecting host");
            }
        }
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

    //Upload JSON file to current directory, which contains file info
    public void uploadFile(String username){
        hostName = username;
        hostFileDir = "../" + username + "Files/";
        hostFiles = new ArrayList<String>();
        
        try{
            JSONObject obj1 = new JSONObject();
            JSONArray arr1 = new JSONArray();
            File hostDesc = new File(FILE_DESC_DIR +  username + "Files.txt");
            BufferedReader reader = new BufferedReader(new FileReader(hostDesc));
            String line;
            while((line = reader.readLine()) != null) {
                String[] descParts = line.split(",");
                JSONObject obj2 = new JSONObject();
                obj2.put("Username", username);
                obj2.put("Filename", descParts[0]);
                obj2.put("Description", descParts[1]);
                arr1.add(obj2);            
                hostFiles.add(descParts[0]);
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

    //Get command from the Host
    public String getCommand(String command){
        String[] commandParts = command.split("\\s");
        if(commandParts[0].equals("connect") && commandParts.length == 3){
            String ipAddress = commandParts[1];
            int port = Integer.parseInt(commandParts[2]);
            try{
                hostSocket = new Socket(ipAddress, port);
                return "Connected to " + ipAddress + ":" + port + "\n";
            }
            catch(IOException ex){
                System.out.println("Problem connecting to server");
                return "Could not connect\n";
            }
            
        }
        else if(commandParts[0].equals("retr") && commandParts.length == 2){
            if(hostSocket != null){
                String fileName = commandParts[1];
                try{
                    dataFromServer = new BufferedReader(new InputStreamReader(hostSocket.getInputStream()));
                    dataToServer = new DataOutputStream(hostSocket.getOutputStream());
                    dataToServer.writeBytes(fileName + '\n');
                    if(dataFromServer.readLine().equals(FILE_FOUND)){
                        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(hostFileDir + "/" + fileName));
                        String fileLine;
                        while(!(fileLine = dataFromServer.readLine()).equals(EOF)){
                            fileWriter.write(fileLine + '\n');
                        }
                        fileWriter.close();
                        return "File retrieved\n";
                    }
                    else{
                        return "File not found\n";
                    }
                }
                catch(IOException ex){
                }
            }
            else{
                return "Not connected to host\n";
            }
        }
        return "Invalid command\n";
    }
}

//Thread for the host server
class HostThread extends Thread{
    private final String EOF = "EOF";
    private final String FILE_FOUND = "FILE_FOUND";
    private final String FILE_NOT_FOUND = "FILE_NOT_FOUND";
    private int hostPort;
    private String hostName;
    private String hostFileDir;
    private ArrayList<String> hostFiles;
    private ServerSocket serverSocket;
    //private Socket clientSocket;
    private DataOutputStream outToClient;
    private BufferedReader inFromClient;
    private boolean cont = true;

    public HostThread(int hostPort, String hostName,  ArrayList<String> hostFiles, String hostFileDir){
        this.hostPort = hostPort; 
        this.hostName = hostName;
        this.hostFiles = hostFiles;
        this.hostFileDir = hostFileDir;
        try{
            serverSocket = new ServerSocket(hostPort);
        }
        catch(IOException ex){
            System.out.println("Cannot setup host server: " + ex);
        }
    }
    public void run(){
        while(cont){
            try{
                Socket clientSocket;
                System.out.println("Waiting for clients...");
                clientSocket = serverSocket.accept();
                inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String fileName = inFromClient.readLine();
                BufferedReader fileReader = new BufferedReader(new FileReader(hostFileDir + fileName));
                outToClient = new DataOutputStream(clientSocket.getOutputStream());
                if(fileExists(fileName)){
                    String fileLine;
                    outToClient.writeBytes(FILE_FOUND + '\n');
                    while((fileLine = fileReader.readLine()) != null){
                        outToClient.writeBytes(fileLine + '\n');
                    }
                    outToClient.writeBytes(EOF + '\n');
                }
                else{
                    outToClient.writeBytes(FILE_NOT_FOUND + '\n');    
                }
                fileReader.close();
                clientSocket.close();
            }
            catch(IOException ex){
                System.out.println("No client accepted: " + ex);
            }
        }
    }
    private boolean fileExists(String fileName){
        for(String file : hostFiles){
            if(file.equals(fileName)){
                return true;
            }
        }
        return false;
    }
    public void shutdown(){
        try{
            cont = false;
            serverSocket.close();
            if(inFromClient != null){
                inFromClient.close();
            }
        }
        catch(IOException ex){
            System.out.println("Problem shutting down host server");
        }
    }
}
