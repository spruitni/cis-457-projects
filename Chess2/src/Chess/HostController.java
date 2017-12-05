package Chess;

import java.io.*;
import java.net.*;
/*
 * Host controller is a sub-class of controller. Since the server and client have the same game functionality, they 
 * share the same code, except host controller will create a server socket, given a port number, that waits for a client to connect.
 */
public class HostController extends Controller{
    private ServerSocket serverSocket = null;
    private DataOutputStream dos;
    private BufferedReader br;
    public HostController(int port){
        super(true);
        try{
            serverSocket = new ServerSocket(port);
            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection created");
            dos = new DataOutputStream(clientSocket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.control(dos);
            this.listen(br, dos);
            dos.close();
            br.close();
            serverSocket.close();
            System.out.println("Game over");
        }
        catch(IOException ex){
            System.out.println("Cannot setup server");
            System.exit(1);
        }
    }
}
