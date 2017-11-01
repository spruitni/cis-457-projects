import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostController{

    //private HostModel hostModel;
    private HostView hostView;
    private ActionListener actionListener;

    public HostController(/*HostModel, */ HostView hostView){
        //this.hostModel = hostModel;
        this.hostView = hostView;
    }

    //Gets info from the button connect
    public void control(){
        actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent event){
                if(event.getSource() == hostView.getConnectButton()){
                    System.out.println("WORKING!");
                }
            }
        };
        hostView.getConnectButton().addActionListener(actionListener);
    }
    public static void main(String[] args){
        HostController hc = new HostController(new HostView());
        hc.control();
    }

}