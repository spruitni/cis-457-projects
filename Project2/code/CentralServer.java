import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/* 
 * This CentralServer class is all information and event handling for
 * the central Napster Server. All users will connect to this and be
 * able to search all other user's files. This will keep an updated
 * .json file with arraylists for each user.
 *
 * Note: this must be running for the HostController's to be able to
 * connect to the server.
 */
public class CentralServer{

    //Connection port and  server socket
    private static final int CONN_PORT = 7171;
    private static ServerSocket serverSocket = null;
    
    public static void main(String args[]) throws IOException{

        //Create the server database
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
        do{      

            //Create and start a new thread for each client with the client socket
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            clientHandler.start();
        }while(true);
    }
}

/*
 * This ClientHandler is started once a user connects to the server.
 * This class allows the centralserver to have multiple people connect
 * to it.
 */
class ClientHandler extends Thread{
    
    //Control connection socket 
    private Socket connectionSocket; 
    private BufferedReader inFromClient;
    private DataOutputStream outToClient;
    private final String EOF = "EOF";
    private static final String jsonFile = "fileInfo.json";

    //Creates client
    public ClientHandler(Socket connectionSocket){
        this.connectionSocket = connectionSocket;

        //Get control input from client
        try{
            inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            outToClient = new DataOutputStream(connectionSocket.getOutputStream());
        }
        catch(IOException ex){
            System.out.println("Problem creating input/output streams: " + ex);
        }
    }

    //The logic for the client-server connection
    public void run(){
        
        //Continue logic until client closes connection
        boolean cont = true;

        //Server can REGISTER host (upload file info) and SEARCH for file info

        String username = "";
        do{
            try{
                String message = inFromClient.readLine();
                String[] messageParts = message.split("\\s");
                String[] messageBody = Arrays.copyOfRange(messageParts, 1, messageParts.length);
                
                //Host registers with server
                if(messageParts[0].equals("Register") && messageParts.length > 1){
                    NapsterDatabase.addUser(messageBody);
                    username = messageParts[1];
                    System.out.println(username + " has joined");
                }

                //Read the host upload
                else if(messageParts[0].equals("Upload")){
                    addFileInfo();
                }

                //Server sends file info to host upon search
                else if(messageParts[0].equals("Search") && messageParts.length > 1){
                    ArrayList<String[]> results = NapsterDatabase.search(messageParts[1]);
                    for(String[] result: results){
                        outToClient.writeBytes(String.join(",",result) + '\n');
                    }
                    outToClient.writeBytes(EOF + '\n');
                }
                else if(messageParts[0].equals("quit")){
                    cont = false;
                    NapsterDatabase.removeUser(username);
                    connectionSocket.close();
                    inFromClient.close();
                }
            }
            catch(IOException ex){
                System.out.println("Error reading from client: " + ex);
            }
        }while(cont);

        //Close control connection
        try{
            connectionSocket.close();
            System.out.println(username + " has left");
        }
        catch(IOException ex){
            System.out.println("Unable to disconnect: (IO EX: " + ex + ")");
        }
    }

    //Parse JSON file and Add file information to the "files" table
    private void addFileInfo(){
        try{
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(jsonFile));
            JSONArray filesArray = (JSONArray) jsonObject.get("Files");
            Iterator<JSONObject> iterator = filesArray.iterator();
            while(iterator.hasNext()){
                JSONObject jsonObject2 = iterator.next(); 
                String[] fileInfo = new String[4];
                fileInfo[0] = (String) jsonObject2.get("Username");
                fileInfo[1] = (String) jsonObject2.get("Filename");
                fileInfo[2] = (String) jsonObject2.get("Description");
                for(String[] user : NapsterDatabase.users){
                    if(user[0].equals(fileInfo[0])){
                        fileInfo[3] = user[3];
                        break;
                    }
                }
                NapsterDatabase.addFiles(fileInfo);
            }
        }
        catch(ParseException ex){
            System.out.println("Problem parsing JSON: " + ex);
        }
        catch(IOException ex){
            System.out.println("Problem writing file info: " + ex);
        }
    }
}

//DATABASE HERE

//The Napster "Database" which stores records in an arraylist
class NapsterDatabase{
    
    //Database is made of tables for users and files
    static String jsonFile = "fileInfo.json";
    static ArrayList<String[]> users;   //username, hostname, port, speed
    static ArrayList<String[]> files;   //username, filename, description, speed
    public NapsterDatabase(){
        users = new ArrayList<String[]>();
        files = new ArrayList<String[]>(); 
    }
    
    //Add user to the "users" table
    public static void addUser(String[] userInfo){
        users.add(userInfo);
    }
    
    //Add files to the "files" table
    public static void addFiles(String[] fileInfo){
        files.add(fileInfo);
    }

    //Remove user and files associated
    public static void removeUser(String username){
        ListIterator<String[]> iter = users.listIterator();
        while(iter.hasNext()){
            String[] u = iter.next().clone();
            if(u[0].equals(username)){
                iter.remove();
            }
        }
        ListIterator<String[]> iter2 = files.listIterator();
        while(iter2.hasNext()){
            String[] f = iter2.next().clone();
            if(f[0].equals(username)){
                iter2.remove();
            }
        }
    }
    
    //Search for a keyword and return any that match
    public static ArrayList<String[]> search(String searchDesc){
        ArrayList<String[]> results = new ArrayList<String[]>();
        for(String[] file : files){
            if(file[2].toLowerCase().contains(searchDesc.toLowerCase())){
                String[] s = new String[4];
                for(String[] user : users){
                    if(user[0].equals(file[0])){
                        s[0] = file[0]; //username
                        s[1] = file[1]; //filename
                        s[2] = user[1] + ":" + user[2]; //IP + port
                        s[3] = user[3]; //speed
                    }
                }
                results.add(s);
            }
        }    
        return results;
    }
}