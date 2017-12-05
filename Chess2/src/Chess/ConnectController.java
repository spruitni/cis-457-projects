package Chess;

import java.io.*;
import java.net.*;

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
