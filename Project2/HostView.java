import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class HostView extends JFrame{
    
    private JLabel serverLabel, portLabel, userLabel, hostLabel, speedLabel, keywordLabel;
    private JTextField serverName, port, userName, hostName, keyword;
    private JButton connectButton, searchButton;
    private JComboBox speed;
    private JPanel mainPanel, topPanel, middlePanel, bottomPanel;

    //Create GUI
    public HostView(){
        mainPanel = new JPanel(new GridLayout(3,1));
        topPanel = new JPanel(new GridBagLayout());
        middlePanel = new JPanel(new GridBagLayout());
        bottomPanel = new JPanel(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        serverLabel = new JLabel("Server Name: ");
        portLabel = new JLabel("Port: ");
        userLabel = new JLabel("User Name: ");
        hostLabel = new JLabel("Host Name: ");
        speedLabel = new JLabel("Speed: ");
        keywordLabel = new JLabel("Keyword: ");
        serverName = new JTextField();
        port = new JTextField();
        userName = new JTextField();
        hostName = new JTextField();
        keyword = new JTextField();
        connectButton = new JButton("Connect");
        searchButton = new JButton("Search");

        speed = new JComboBox<>(ConnectionSpeed.values());
        
        //Set component sizes
        serverName.setPreferredSize(new Dimension(150,30));
        port.setPreferredSize(new Dimension(150,30));
        userName.setPreferredSize(new Dimension(150,30));
        hostName.setPreferredSize(new Dimension(150,30));
        keyword.setPreferredSize(new Dimension(150,30));

        //Add title borders
        topPanel.setBorder(new TitledBorder("Connection"));
        middlePanel.setBorder(new TitledBorder("Search"));
        bottomPanel.setBorder(new TitledBorder("FTP"));

        //Adds components to the top panel
        topPanel.add(serverLabel, getGBC(0,0,1,1,false,false));
        topPanel.add(serverName, getGBC(1,0,1,1,false,false));
        topPanel.add(portLabel, getGBC(2,0,1,1,false,false));
        topPanel.add(port, getGBC(3,0,1,1,false,false));
        topPanel.add(connectButton, getGBC(4,0,2,1,true,false));
        topPanel.add(userLabel, getGBC(0,1,1,1,false,false));
        topPanel.add(userName, getGBC(1,1,1,1,false,false));
        topPanel.add(hostLabel, getGBC(2,1,1,1,false,false));
        topPanel.add(hostName, getGBC(3,1,1,1,false,false));
        topPanel.add(speedLabel, getGBC(4,1,1,1,false,false));
        topPanel.add(speed, getGBC(5,1,1,1,false,false));

        //Add components to the middle panel
        middlePanel.add(keywordLabel, getGBC(0,0,2,1,true,false));
        middlePanel.add(keyword, getGBC(2,0,2,1,true,false));
        middlePanel.add(searchButton, getGBC(4,0,2,1,true,false));

        //Add sub-panels to main panel
        mainPanel.add(topPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(bottomPanel);

        //JFrame setup and additions
        this.setTitle("GV-Napster Host");
        this.add(mainPanel);
        this.setSize(900,600);
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

        //Margin around each component
        gbc.insets = new Insets(5, 5, 5, 5);
        return gbc;
    }

    //Get button values
    public JButton getConnectButton(){
        return connectButton;
    }
    public JButton getSearchButton(){
        return searchButton;
    }

    //Get server name (IP address)
    public String getServerName(){
        return serverName.getText();
    }

    //Get port number as integer
    public int getPort(){
        return Integer.parseInt(port.getText());
    }

    //Get username
    public String getUserName(){
        return userName.getText();
    }

    //Get host name
    public String getHostName(){
        return hostName.getText();
    }

    //Get selected speed
    public String getSpeed(){
        return speed.getSelectedItem().toString();
    }


}