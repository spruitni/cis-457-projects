import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.awt.event.WindowEvent;

public class HostController{

    private HostModel hostModel;
    private HostView hostView;
    private ActionListener actionListener;
    private WindowListener windowListener;

    public HostController(){
        this.hostView = new HostView();
    }

    //Listens for GUI events
    public void control(){
        hostModel = new HostModel();

        //Listen for button clicked, etc.
        actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent event){
                
                //CONNECT BUTTON IS CLICKED - Connect to server, send user info and file info
                if(event.getSource() == hostView.getConnectButton()){

                    //This host's files will be in a directory that is named the username
                    //This is done so that when the host uploads the file info, it knows what files to upload
                    hostModel.connectToServer(hostView.getServerName(), hostView.getPort());
                    String username = hostView.getUserName();
                    String hostname = hostView.getHostName();
                    String hostPort = Integer.toString(hostView.getHostPort());
                    String speed = hostView.getSpeed();
                    hostModel.sendMessage("Register " + username + " " + hostname + " " + hostPort + " " + speed);
                    System.out.println("Sent user info to central server");
                    hostModel.uploadFile(username);
                    System.out.println("Sent file info to central server");

                    //Host is now allowed to do keyword searches, enable search button
                    hostView.getSearchButton().setEnabled(true);
                }

                //Search
                if(event.getSource() == hostView.getSearchButton()){
                    String keyword = hostView.getKeyword();
                    if(!keyword.isEmpty() && keyword != null){
                        hostModel.sendMessage("Search " + keyword);
                        hostModel.readMessage();



                        //ArrayList<String[]> results = NapsterDatabase.search(keyword);
                        //String[] columnNames = {"Filename", "Hostname", "Description", "Speed"};
                        //hostView.addTable(columnNames, results);
                    }
                }
            }
        };

        //Listen for window close, then close connection
        windowListener = new WindowListener(){
            public void windowClosing(WindowEvent event){
                hostView.dispose();
                try{
                    hostModel.quit();
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

    //Start Host GUI
    public static void main(String[] args){
        HostController hc = new HostController();
        hc.control();
    }

}