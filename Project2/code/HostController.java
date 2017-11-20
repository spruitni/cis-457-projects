import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.awt.event.WindowEvent;
import java.util.*;
import java.io.*;

public class HostController{

    private HostModel hostModel;
    private HostView hostView;
    private ActionListener actionListener;
    private WindowListener windowListener;
    private final String HOST_DESC = "../hostDescriptions/";
    private boolean connected;

    public HostController(){
        this.hostView = new HostView();
        this.hostModel = new HostModel();
        connected = false;
    }

    //Listens for GUI events
    public void control(){

        //Listen for button clicked, etc.
        actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent event){
                
                //CONNECT BUTTON IS CLICKED - Connect to server, send user info and file info
                if(event.getSource() == hostView.getConnectButton()){

                    //This host's files will be in a directory that is named the username
                    //This is done so that when the host uploads the file info, it knows what files to upload
                    String message;
                    if((message = validateInput()).equals("")){
                        String username = hostView.getUserName();
                        String hostname = hostView.getHostName();
                        String hostPort = hostView.getHostPort();
                        String speed = hostView.getSpeed();

                        //Connect to server
                        boolean connected = hostModel.connectToServer(hostView.getServerName(), Integer.parseInt(hostView.getPort()));
                        if(connected){

                            //Setup host info
                            connected = true;
                            hostView.getConnectButton().setEnabled(false);
                            hostView.setMessage(message);
                            hostModel.sendMessage("Register " + username + " " + hostname + " " + hostPort + " " + speed);
                            System.out.println("Sent user info to central server");
                            hostModel.uploadFile(username);
                            hostModel.sendMessage("Upload");
                            System.out.println("Sent file info to central server");
                            hostModel.setup(hostname, Integer.parseInt(hostPort));
                            hostView.getConnectButton().setEnabled(false);
        
                            //Host is now allowed to do keyword searches, enable search button, disable connect button
                            hostView.getSearchButton().setEnabled(true);
                        }
                        else{
                            hostView.setMessage("ERROR: CONNECTION FAILED");
                        }
                    }
                    else{
                        hostView.setMessage(message);
                    }
                }

                //Search
                if(event.getSource() == hostView.getSearchButton()){
                    String keyword = hostView.getKeyword().trim();
                    if(!keyword.isEmpty() && keyword != null){
                        hostModel.sendMessage("Search " + keyword);
                        ArrayList<String[]> results = hostModel.readSearchResults();
                        String[] columnNames = {"Username", "Filename", "Host Details", "Speed"};
                        hostView.addTable(columnNames, results);
                    }

                    //Host is now allowed to enter commands to get files from other hosts
                    hostView.getGoButton().setEnabled(true);
                }

                //Enter command
                if(event.getSource() == hostView.getGoButton()){
                    String command = hostView.getCommand();
                    hostView.setCommandWindow(">>> " + command + '\n');
                    if(command.equals("quit")){
                        hostModel.sendMessage("quit");
                        hostView.setCommandWindow("Disconnected\n");
                        connected = false;
                        hostModel.shutdown();
                        hostView.clear();
                    }
                    else if(!command.isEmpty() && command != null){
                        String message = hostModel.getCommand(command);
                        hostView.setCommandWindow(message);
                    }
                }
            }
        };

        //Listen for window close, then close connection
        windowListener = new WindowListener(){
            public void windowClosing(WindowEvent event){
                hostView.dispose();
                try{
                    //Disconnect only if host is still connected
                    if(connected){
                        hostModel.sendMessage("quit");
                        hostModel.shutdown();
                    }
                }
                catch(NullPointerException ex){
                    System.out.println("Problem disconnecting from central server: " + ex);
                }
            }
            
            //Not really needed, but abstract interface methods need be overridden
            public void windowClosed(WindowEvent event){}
            public void windowOpened(WindowEvent event){}
            public void windowIconified(WindowEvent event){} 
            public void windowDeiconified(WindowEvent event){}
            public void windowActivated(WindowEvent event){}
            public void windowDeactivated(WindowEvent event){}
            public void windowGainedFocus(WindowEvent event){}
            public void windowLostFocus(WindowEvent event){}
            public void windowStateChanged(WindowEvent event){}
        };

        //Add listeners to GUI components
        hostView.getConnectButton().addActionListener(actionListener);
        hostView.getSearchButton().addActionListener(actionListener);
        hostView.getGoButton().addActionListener(actionListener);
        hostView.addWindowListener(windowListener);
    }

    //Validates user input
    private String validateInput(){
        String hostname = hostView.getHostName();
        String serverPort = hostView.getHostPort();
        String serverName = hostView.getServerName();
        String username = hostView.getUserName();
        String hostPort = hostView.getHostPort();
        if(serverName.equals("") || serverPort.equals("") || username.equals("") || hostname.equals("") || hostPort.equals("")){
            return "ERROR: EMPTY FIELD(S)";
        }
        else{
            File folder = new File(HOST_DESC);
            boolean fileFound = false;
            for(File file : folder.listFiles()){
                if(file.getName().equals(username + "Files.txt")){
                    fileFound = true;
                }
            }
            if(!fileFound){
                return "ERROR: INVALID USERNAME";
            }
        }
        return "";
    }

    //Start Host GUI
    public static void main(String[] args){
        HostController hc = new HostController();
        hc.control();
    }
}