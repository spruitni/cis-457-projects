import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;

public class HostController{

    private HostModel hostModel;
    private HostView hostView;
    private ActionListener actionListener;
    private WindowListener windowListener;

    public HostController(HostModel hostModel, HostView hostView){
        this.hostModel = hostModel;
        this.hostView = hostView;
    }

    //Listens for GUI events
    public void control(){

        //Listen for button clicked, etc.
        actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent event){
                
                //Control connection to server
                if(event.getSource() == hostView.getConnectButton()){
                    hostModel.connectToServer(hostView.getServerName(), hostView.getPort());
                }
            }
        };

        //Listen for window close, then close connection
        windowListener = new WindowListener(){
            public void windowClosing(WindowEvent event){
                hostModel.sendMessage("quit");
                System.out.println("quit");
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

        hostView.getConnectButton().addActionListener(actionListener);
        hostView.addWindowListener(windowListener);
    }
    public static void main(String[] args){
        HostController hc = new HostController(new HostModel(), new HostView());
        hc.control();
    }

}