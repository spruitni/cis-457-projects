import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;
/************************************************************
 * FTP Client Application
 * Author: Nick Spruit
 * Date: October 17, 2017
 * Desc: This application creates a client that can 
 * communicate withe the server. Using a control connection, 
 * the client can send commands to the server, including 
 * LIST, RETR, STOR, and QUIT. 
 ***********************************************************/
public class FTPClient{
    public static void main(String agv[]) throws Exception{
        
        //Constants for directory, EOF, and messages
        final String EOF = "eof";
        final String FILE_DIRECTORY = "clientFiles/";
        final String FILE_FOUND = "200";
        final String FILE_NOT_FOUND = "550";
        int connectionPort = 0;
        String ipAddress = null;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        //Continue allowing the user to enter valid connection arguments
        String[] tokens;
        do{
            tokens = userInput.readLine().split("\\s");
        }while(!validConnectionInput(tokens));

        try{

            //Get IP and port number
            ipAddress = tokens[1];
            connectionPort = Integer.parseInt(tokens[2]);
            Socket connectionSocket = null;

            //Connection/command messages
            String welcomeMessage = "\nWelcome to the Text FTP Server!";
            String possibleCommands = "list:  |  retr:  <filename> |  stor: <filename> |  quit\n";
            System.out.println(welcomeMessage + '\n' + possibleCommands);
            
            //Create control connection to server
            connectionSocket = new Socket(ipAddress, connectionPort);
            DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
                
            //Loop variable for continued user input
            boolean openConnection = true;
            while(openConnection){
                String message = userInput.readLine();
                int welcomePort = connectionPort + 2;
                ServerSocket welcomeSocket = new ServerSocket(welcomePort);
                   
                //User enters list:
                if(message.equals("list:")){

                    //Send command to server and connect to server
                    outToServer.writeBytes(welcomePort + " " + message +  " " + '\n');
                    Socket dataSocket = welcomeSocket.accept();
                    BufferedReader dataFromServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                    String fileName;
                    System.out.println("\nFiles on server:");

                    //Reads a list of files from the server
                    while(!(fileName = dataFromServer.readLine()).equals(EOF)){
                        System.out.println("  " + fileName);
                    }
                    System.out.println("(End of files)\n");
                    dataSocket.close();
                    welcomeSocket.close();
                }

                //User enters retr: or stor:
                else if(message.startsWith("retr:") || message.startsWith("stor:")){

                    //Get command and file name
                    String[] messageTokens = message.split("\\s");
                    if(messageTokens.length == 2){
                            
                        //Write message to server
                        String fileName = messageTokens[1];
                        String messageToServer = welcomePort + " " + message + " " + fileName + " " + '\n';
                        
                        //Retrieve file from server
                        if(messageTokens[0].equals("retr:")){
                            String fileLine;
                            outToServer.writeBytes(messageToServer);
                            Socket dataSocket = welcomeSocket.accept();
                            BufferedReader dataFromServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream())); 
                            try{

                                //File is found on the server, read file line by line
                                if((fileLine = dataFromServer.readLine()).equals(FILE_FOUND)){
                                    BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FILE_DIRECTORY +  "/" + fileName));
                                    System.out.println("\nDownloading file from server...");
                                    while(!(fileLine = dataFromServer.readLine()).equals(EOF)){
                                        fileWriter.write(fileLine + '\n');
                                    }
                                    System.out.println("File Downloaded!\n");
                                    fileWriter.close();
                                }
                                else{
                                    System.out.println("File not found on server!\n");
                                }
                            }
                            catch(IOException ex){
                                System.out.println("IO Exception: " + ex);
                            }
                            finally{
                                dataSocket.close();
                                welcomeSocket.close();
                            }
                        }

                        //Store file on server
                        else if(messageTokens[0].equals("stor:")){
                            File file;
                            outToServer.writeBytes(messageToServer);
                            Socket dataSocket = welcomeSocket.accept();
                            DataOutputStream dataToServer = new DataOutputStream(dataSocket.getOutputStream());

                            //File is found on the client, read file line by line and write to server
                            if((file = FileClass.fileExists(FILE_DIRECTORY + "/" + messageTokens[1])) != null){
                                dataToServer.writeBytes(FILE_FOUND + '\n');
                                BufferedReader fileReader = new BufferedReader(new FileReader(file));
                                String fileLine;
                                System.out.println("\nUploading file to server...");          
                                while((fileLine = fileReader.readLine()) != null){
                                    dataToServer.writeBytes(fileLine + '\n');
                                }
                                System.out.println("File uploaded!\n");
                                
                                //Write EOF to server
                                dataToServer.writeBytes(EOF + '\n');
                                fileReader.close();
                                dataSocket.close();
                            }
                            //Close outpu stream and data socket
                            else{
                                dataToServer.writeBytes(FILE_NOT_FOUND + '\n');
                                System.out.println("File not found on client!");
                            }
                            welcomeSocket.close();
                        }
                    }
                    else{
                        System.out.println("Invalid number of arguments for 'retr:' command");
                    }
                }

                //Write "quit" message to server and discontinue the loop
                else if(message.equals("quit")){
                    openConnection = false;
                    outToServer.writeBytes("quit\n");
                }

                //Invalid command, list commands
                else{
                    System.out.println("\nInvalid command");
                    System.out.println(possibleCommands);
                    welcomeSocket.close();
                }
                //Prompt user for next command
                if(!message.equals("quit")){
                    System.out.println("Enter next command:\n");
                }
            }
            
            //Close control connection
            try{
                connectionSocket.close();
                System.out.println("\nClosing connection...");
            }
            catch(IOException ex){
                System.out.println("\nUnable to disconnect: (IO EX: " + ex + ")");
                System.exit(1);
            }
            System.out.println("Terminated");
        }
        catch(IOException ex){
            System.out.println("Error with connection");
        }        
    }


    //Check for valid connection arguments
    private static boolean validConnectionInput(String[] tokens){

        //Check for valid number of arguments, first argument is 'connect'
        //and the port number is greater than 1024
        if(tokens.length == 3){
            if(tokens[0].toUpperCase().equals("CONNECT")){
                try{
                    int portNumber = Integer.parseInt(tokens[2]);
                    if(portNumber >= 1024){                        
                        return true;
                    }
                    else{
                        System.out.println("Invalid port number (must be >= 1024)");
                    }
                }
                catch(NumberFormatException ex){
                    System.out.println("Port number must be an integer");
                }
            }
            System.out.println("Invalid first argument");
        }
        else{
            System.out.println("Invalid number of arguments");
        }
        return false;
    }
}