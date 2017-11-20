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
    private Socket socket;
    private DataOutputStream outToServer;
    private BufferedReader inFromServer;
    private final String JSON_FILE_NAME = "fileInfo.json";
    private final String FILE_DIRECTORY = "../hostDescriptions/";
    private ArrayList<String> hostFiles;
    private int port;
    private String ip;
    private FTPClientServer ftpClientServer;

    private DataOutputStream out;
    private DataInputStream dis;
    private FileInputStream fis;
    private ServerSocket welcomeData;
    private static ServerSocket welcomeSocket;
    private Socket dataSocket;
   
    
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
    public void setup(String ipAddress, int portNumber){
        ip = ipAddress;
        port = portNumber;
        ftpClientServer = new FTPClientServer(port);
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
        hostFiles = new ArrayList<String>();
        try{
            JSONObject obj1 = new JSONObject();
            JSONArray arr1 = new JSONArray();
            File hostDesc = new File(FILE_DIRECTORY +  username + "Files.txt");
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
    public void getCommand(String command){
        String[] commandParts = command.split("\\s");
        if(commandParts[0].equals("connect")){
            String ipAddress = commandParts[1];
            int port = Integer.parseInt(commandParts[2]);
            try{
                welcomeData = new ServerSocket(port);
                dataSocket = welcomeData.accept();
                dis = new DataInputStream(new BufferedInputStream(dataSocket.getInputStream()));
                out = new DataOutputStream(dataSocket.getOutputStream());
            }
            catch(IOException ex){

            }

        }
        else if(commandParts[0].equals("retr")){
            out.writeBytes(command+ "\n");
            String file = commandParts[1];
            FileOutputStream out = null;
            boolean fileExists = true;
            try {
                out = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                System.out.println("Client error: File Not Recieved.");
                fileExists = false;
            } 
            try{
                if (fileExists) {
                    recieveFile(dis, out);
                }
                welcomeData.close();
                dataSocket.close();
                dis.close();
                out.close();
            }
            catch(IOException ex){
            }
        }

    }

    /*
     * This private method is used to send a file to the client. It takes as
     * parameters a FileInputStream fis and a DataOutStream os. While its trying
     * to send the file will update with a message indicating it is still working.
     */
    private static void sendFile(FileInputStream fis, DataOutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int bytes = 0;
        while ((bytes = fis.read(buffer)) != -1) {
            System.out.println("Sending File...");
            os.write(buffer, 0, bytes);
        }
    }

    /*
     * This private method is used to recieve a file to the client. It takes as
     * parameters a DataOutStream os and a FileInputStream fis. While its trying
     * to recieve the file will update with a message indicating it is still working.
     */
    private static void recieveFile(DataInputStream dis, FileOutputStream os) throws IOException{
        byte[] buffer = new byte[1024];
        int bytes;
        while ((bytes = dis.read(buffer)) != -1) {
            System.out.println("Recieving File...");
            os.write(buffer, 0, bytes);
        }
    }
}



/***************************************************************************************** */


class FTPClientServer extends Thread{

  private static ServerSocket welcomeSocket;
  private int port;

  /*
   * Main method for initiating the server. This will continue to listen to port
   * 5081 for new connections. Once a connection has been established it will pass
   * it off to the ClientThread class where the rest of the commands will be
   * handled.
   */
  
    public FTPClientServer(int port){
        this.port = port;
        this.start();
    }
    public void run(){    
        try {
            welcomeSocket = new ServerSocket(port);
            while (true) {
                Socket socket = welcomeSocket.accept();
                ClientThread thread = new ClientThread(socket);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Error Connecting...");
            System.exit(1);
        }
    }
}

/*
 * The ClientThread class extends Thread. This class will be used
 * to handle new connections made to the server. Once the connection
 * is made, it will come to this class where all the commands will be
 * parsed. Also, all the data will flow through here.
 */
class ClientThread extends Thread {

  private Socket clientConn;
  private DataOutputStream outToClient;
  private BufferedReader inFromClient;

  /*
   * Constructor. This method takes in a @param Socket connectionSocket.
   * This will then set clientConn. From here it will try to create new
   * bufferedreaders and dataoutputstreams.
   */
  public ClientThread(Socket connectionSocket) {

    clientConn = connectionSocket;
    System.out.println("User Connected"+ clientConn.getInetAddress());

    try {

      outToClient = new DataOutputStream(clientConn.getOutputStream());
      inFromClient = new BufferedReader(new InputStreamReader(clientConn.getInputStream()));

    } catch (IOException e) {

      e.printStackTrace();

    }
  }

  /*
   * This method is used by calling .start() on a new thread.
   * This will allow the multithreading of the server class.
   * (Multiple client connections) This method services all the
   * commands of the server and clients.
   */
  public void run() {
    File[] listOfFiles = null;
    String nextFile = null;

    try {

      /*
       * Going through all the the possible commands.
       */
      while (true) {

        String fromClient;
        String clientCommand;
        byte[] data;
        String frstln;
        int port;

        fromClient = inFromClient.readLine();
        StringTokenizer tokens = new StringTokenizer(fromClient);
        frstln = tokens.nextToken();
        port = Integer.parseInt(frstln);
        clientCommand = tokens.nextToken();
        try {
          nextFile = tokens.nextToken();
        } catch (Exception e) {
        }

        /*
         * Case "retr:"
         *
         * This method will be entered when the user wants to
         * return/download a file from the server. It creates input and output
         * streams. Then it will search the directory for the file the client
         * wants. If it has the file it will send it to them, if not an error
         * message will be sent.
         */
        if (clientCommand.equals("retr:")) {
          System.out.println("Sending file to client...");
          Socket dataSocket = new Socket(clientConn.getInetAddress(), port);
          DataOutputStream dataOutToClient = new DataOutputStream(dataSocket.getOutputStream());
          FileInputStream in = null;

          try {
            in = new FileInputStream(nextFile);
            outToClient.writeUTF("200 command ok");
            sendFile(in, dataOutToClient);
            in.close();
          } catch (FileNotFoundException e) {
              System.out.println("Failed to send file...");
            outToClient.writeUTF("550 file not found");
          }

          dataOutToClient.close();
          dataSocket.close();
          System.out.println("Data Socket closed");
        }
        
      }
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  /*
   * This private method is used to send a file to the client. It takes as
   * parameters a FileInputStream fis and a DataOutStream os. While its trying
   * to send the file will update with a message indicating it is still working.
   */
  private static void sendFile(FileInputStream fis, DataOutputStream os) throws Exception {
    byte[] buffer = new byte[1024];
    int bytes = 0;

    while ((bytes = fis.read(buffer)) != -1) {
      System.out.println("Sending File...");
      os.write(buffer, 0, bytes);
    }
  }

  /*
   * This private method is used to recieve a file to the client. It takes as
   * parameters a DataOutStream os and a FileInputStream fis. While its trying
   * to recieve the file will update with a message indicating it is still working.
   */
  private static void recieveFile(DataInputStream dis, FileOutputStream os) throws Exception {
    byte[] buffer = new byte[1024];
    int bytes;

    while ((bytes = dis.read(buffer)) != -1) {
      System.out.println("Recieving File...");
      os.write(buffer, 0, bytes);
    }
  }
}