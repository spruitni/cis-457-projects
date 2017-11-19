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

    public HostController(){
        this.hostView = new HostView();
        this.hostModel = new HostModel();
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
                            hostModel.setup(hostname, Integer.parseInt(hostPort));
                            hostView.getConnectButton().setEnabled(false);
                            hostView.setMessage(message);
                            hostModel.sendMessage("Register " + username + " " + hostname + " " + hostPort + " " + speed);
                            System.out.println("Sent user info to central server");
                            hostModel.uploadFile(username);
                            hostModel.sendMessage("Upload");
                            System.out.println("Sent file info to central server");
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
                    String keyword = hostView.getKeyword();
                    if(!keyword.isEmpty() && keyword != null){
                        hostModel.sendMessage("Search " + keyword);
                        ArrayList<String[]> results = hostModel.readSearchResults();
                        String[] columnNames = {"Username", "Filename", "Host Details", "Speed"};
                        hostView.addTable(columnNames, results);
                    }

                    //Host is now allowed to enter commands to get files from other hosts
                    hostView.getGoButton().setEnabled(true);
                }
            }
        };

        //Listen for window close, then close connection
        windowListener = new WindowListener(){
            public void windowClosing(WindowEvent event){
                hostView.dispose();
                try{
                    hostModel.sendMessage("quit");
                }
                catch(NullPointerException ex){
                    System.out.println("Problem disconnecting from central server: " + ex);
                }
            }
            
            //Not really needed, but abstract interface methods need be overridden
            public void windowClosed(WindowEvent event){System.out.println("GUI Closed");}
            public void windowOpened(WindowEvent event){System.out.println("GUI Opened");}
            public void windowIconified(WindowEvent event){System.out.println("GUI Iconified");} 
            public void windowDeiconified(WindowEvent event){System.out.println("GUI Deiconified");}
            public void windowActivated(WindowEvent event){System.out.println("GUI Activated");}
            public void windowDeactivated(WindowEvent event){System.out.println("GUI Deactivated");}
            public void windowGainedFocus(WindowEvent event){System.out.println("GUI Gained Focus");}
            public void windowLostFocus(WindowEvent event){System.out.println("GUI Lost Focus");}
            public void windowStateChanged(WindowEvent event){System.out.println("GUI State Change");}
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