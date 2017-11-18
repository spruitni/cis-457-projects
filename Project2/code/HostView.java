import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

public class HostView extends JFrame{
    
    private JLabel serverLabel, portLabel, userLabel, hostLabel, 
            hostPortLabel, speedLabel, keywordLabel, commandLabel, messageLabel;
    private JTextField serverName, port, userName, hostName, hostPort, keyword, command;
    private JScrollPane tableScroll, scroll;
    private JTextArea commandWindow;
    private JButton connectButton, searchButton, goButton;
    private JComboBox speed;
    private JTable table;
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
        hostPortLabel = new JLabel("Host Port: ");
        speedLabel = new JLabel("Speed: ");
        keywordLabel = new JLabel("Keyword: ");
        commandLabel = new JLabel("Enter Command: ");
        messageLabel = new JLabel();
        serverName = new JTextField();
        port = new JTextField();
        userName = new JTextField();
        hostName = new JTextField();
        hostPort = new JTextField();
        keyword = new JTextField();
        command = new JTextField();
        commandWindow = new JTextArea();
        scroll = new JScrollPane(commandWindow);
        connectButton = new JButton("Connect");
        searchButton = new JButton("Search");
        goButton = new JButton("Go");

        speed = new JComboBox<>(ConnectionSpeed.values());
        
        //Set component details
        serverName.setPreferredSize(new Dimension(150,30));
        port.setPreferredSize(new Dimension(150,30));
        userName.setPreferredSize(new Dimension(150,30));
        hostName.setPreferredSize(new Dimension(150,30));
        hostPort.setPreferredSize(new Dimension(150,30));
        keyword.setPreferredSize(new Dimension(300,30));
        command.setPreferredSize(new Dimension(300,30));
        scroll.setPreferredSize(new Dimension(300,100));
        searchButton.setEnabled(false);
        goButton.setEnabled(false);

        //Add title borders
        topPanel.setBorder(new TitledBorder("Connection"));
        middlePanel.setBorder(new TitledBorder("Search"));
        bottomPanel.setBorder(new TitledBorder("FTP"));

        //Adds components to the top panel
        topPanel.add(serverLabel, getGBC(0,0,1,1,false,false));
        topPanel.add(serverName, getGBC(1,0,1,1,false,false));
        topPanel.add(portLabel, getGBC(2,0,1,1,false,false));
        topPanel.add(port, getGBC(3,0,1,1,false,false));
        topPanel.add(connectButton, getGBC(4,0,4,1,true,false));
        topPanel.add(userLabel, getGBC(0,1,1,1,false,false));
        topPanel.add(userName, getGBC(1,1,1,1,false,false));
        topPanel.add(hostLabel, getGBC(2,1,1,1,false,false));
        topPanel.add(hostName, getGBC(3,1,1,1,false,false));
        topPanel.add(hostPortLabel, getGBC(4,1,1,1,false,false));
        topPanel.add(hostPort, getGBC(5,1,1,1,false,false));
        topPanel.add(speedLabel, getGBC(6,1,1,1,false,false));
        topPanel.add(speed, getGBC(7,1,1,1,false,false));
        topPanel.add(messageLabel, getGBC(0,2,6,1,true,false));

        //Add components to the middle panel
        middlePanel.add(keywordLabel, getGBC(0,0,2,1,true,false));
        middlePanel.add(keyword, getGBC(2,0,2,1,true,false));
        middlePanel.add(searchButton, getGBC(4,0,2,1,true,false));

        //Add components to bottom panel
        bottomPanel.add(commandLabel, getGBC(0,0,2,1,true,false));
        bottomPanel.add(command, getGBC(2,0,2,1,true,false));
        bottomPanel.add(goButton, getGBC(4,0,2,1,true,false));
        bottomPanel.add(scroll, getGBC(0,1,6,4, true, true));
        
        //Add sub-panels to main panel
        mainPanel.add(topPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(bottomPanel);
        frameSetup();

    }
    
    private void frameSetup(){
        
        //JFrame setup and additions
        this.setTitle("GV-Napster Host");
        this.add(mainPanel);
        this.setSize(900,700);
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

    //Add table with search results
    public void addTable(String[] columnNames, ArrayList<String[]> data){
        if(tableScroll != null){
            middlePanel.remove(tableScroll);
        }
        middlePanel.revalidate();
        middlePanel.repaint();
        String[][] dataArray = new String[data.size()][4];
        for(int i = 0; i < data.size(); i++){
            String[] fileInfo = data.get(i);
            for(int j = 0; j < fileInfo.length; j++){
                dataArray[i][j] = fileInfo[j];
            }
        }
        table = new JTable(dataArray, columnNames);
        tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(600,100));
        middlePanel.add(tableScroll, getGBC(0, 1, 12, 4, true, true));
    }

    //Get button values
    public JButton getConnectButton(){
        return connectButton;
    }
    public JButton getSearchButton(){
        return searchButton;
    }
    public JButton getGoButton(){
        return goButton;
    }

    //Get server name (IP address)
    public String getServerName(){
        return serverName.getText();
    }

    //Get port number as integer
    public String getPort(){
        return port.getText();
    }

    //Get username
    public String getUserName(){
        return userName.getText();
    }

    //Get host name
    public String getHostName(){
        return hostName.getText();
    }

    //Get host port number as integer
    public String getHostPort(){
        return hostPort.getText();
    }

    //Set message to user
    public void setMessage(String m){
        messageLabel.setText(m);
    }

    //Get selected speed
    public String getSpeed(){
        return speed.getSelectedItem().toString();
    }

    //Get keyword search
    public String getKeyword(){
        return keyword.getText();
    }
}