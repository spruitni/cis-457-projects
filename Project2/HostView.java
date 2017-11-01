import java.awt.GridLayout;
import javax.swing.*;

public class HostView extends JFrame{
    
    private JButton connectButton;
    private JTextField serverName, port;
    private JPanel topPanel;

    public HostView(){
        this.setTitle("GV-Napster Host");
        this.setLayout(new GridLayout(3,1)); 
        this.setSize(900,900);
        this.getContentPane();   
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    
    public JButton getConnectButton(){
        return connectButton;
    }
    public String getServerName(){
        return serverName.getText();
    }
    public String getPort(){
        return port.getText();
    }
    public void setText(String text){
        this.serverName.setText(text);
    }

}