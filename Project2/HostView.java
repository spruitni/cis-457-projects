import java.awt.GridLayout;
import javax.swing.*;

public class HostView extends JFrame{
    
    private JButton connectButton;
    private JTextField serverName, port;
    private JPanel topPanel;

    //Create GUI
    public HostView(){
        this.setTitle("GV-Napster Host");
        this.setLayout(new GridLayout(3,1)); 
        this.setSize(900,900);
        this.getContentPane();   
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.topPanel = new JPanel(new GridLayout(0, 6, 10, 10));
        this.serverName = new JTextField();
        this.port = new JTextField();
        this.connectButton = new JButton("Connect");
        topPanel.add(this.serverName);
        topPanel.add(this.port);
        topPanel.add(this.connectButton);
        this.add(this.topPanel);
        this.setVisible(true);
    }

    //Get connect button
    public JButton getConnectButton(){
        return connectButton;
    }

    //Get server name (IP address)
    public String getServerName(){
        return serverName.getText();
    }

    //Get port number as integer
    public int getPort(){
        return Integer.parseInt(port.getText());
    }
}