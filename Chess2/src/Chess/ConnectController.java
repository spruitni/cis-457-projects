package Chess;

import java.io.*;
import java.net.*;
/*
 * Connect controller is a sub-class of controller. Since the server and client have the same game functionality, they 
 * share the same code, except connect controller will create a socket that connects to the server socket given an IP
 * address and port number.
 */
public class ConnectController extends Controller{
    private Socket socket;
    private DataOutputStream dos;
    private BufferedReader br;
    public ConnectController(String ipAddress, int port){
        super(false);
        try{
            socket = new Socket(ipAddress, port);
            System.out.println("Connection created");
            dos = new DataOutputStream(socket.getOutputStream());
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.control(dos);
            this.listen(br, dos);
            dos.close();
            br.close();
            socket.close();
            System.out.println("Game over");
        }
        catch(IOException ex){
            System.out.println("Cannot setup server");
            System.exit(1);
        }
    }
}
