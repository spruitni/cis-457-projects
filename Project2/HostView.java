import javax.swing.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class HostView extends JFrame{
    
    private JLabel serverLabel, portLabel, userLabel, hostLabel, speedLabel;
    private JTextField serverName, port, userName, hostName;
    private JButton connectButton;
    private JComboBox speed;
    private JPanel topPanel;

    //Create GUI
    public HostView(){
        this.setTitle("GV-Napster Host");
        topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        serverLabel = new JLabel("Server Name: ");
        portLabel = new JLabel("Port: ");
        userLabel = new JLabel("User Name: ");
        hostLabel = new JLabel("Host Name: ");
        speedLabel = new JLabel("Speed: ");
        serverName = new JTextField();
        port = new JTextField();
        userName = new JTextField();
        hostName = new JTextField();
        connectButton = new JButton("Connect");
        speed = new JComboBox<>(ConnectionSpeed.values());

        serverName.setPreferredSize(new Dimension(150,30));
        port.setPreferredSize(new Dimension(150,30));
        userName.setPreferredSize(new Dimension(150,30));
        hostName.setPreferredSize(new Dimension(150,30));


        topPanel.add(serverLabel, getGBC(0,0,1,1,false,false));
        topPanel.add(serverName, getGBC(1,0,1,1,false,false));
        topPanel.add(portLabel, getGBC(2,0,1,1,false,false));
        topPanel.add(port, getGBC(3,0,1,1,false,false));
        topPanel.add(connectButton, getGBC(4,0,4,1,true,false));
        topPanel.add(userLabel, getGBC(0,1,1,1,false,false));
        topPanel.add(userName, getGBC(1,1,1,1,false,false));
        topPanel.add(hostLabel, getGBC(2,1,1,1,false,false));
        topPanel.add(hostName, getGBC(3,1,1,1,false,false));
        topPanel.add(speedLabel, getGBC(4,1,1,1,false,false));
        topPanel.add(speed, getGBC(5,1,1,1,false,false));
        
        


        this.add(topPanel);
        this.setSize(900,300);
        this.getContentPane();   
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setVisible(true);
    }


    //Creates GridBagConstraints 
    private GridBagConstraints getGBC(int x, int y, int w, int h, boolean fillHor, boolean fillVer){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = w;
        gbc.gridheight = h;
        if(fillHor){
            gbc.fill = GridBagConstraints.HORIZONTAL;
        }
        if(fillVer){
            gbc.fill = GridBagConstraints.VERTICAL;
        }
        //Margin around each item
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;

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