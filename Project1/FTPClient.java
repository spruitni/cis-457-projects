import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;


public class FTPClient{

    public static void main(String agv[]) throws Exception{
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

        
        if(validConnectionInput(tokens)){
            ipAddress = tokens[1];
            connectionPort = Integer.parseInt(tokens[2]);
            Socket connectionSocket = null;
            try{
                String welcomeMessage = "\nWelcome to the Text FTP Server!";
                String possibleCommands = "list:  |  retr:  |  stor:  |  quit\n";
                System.out.println(welcomeMessage + '\n' + possibleCommands);

                connectionSocket = new Socket(ipAddress, connectionPort);
                

                DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
                
                //Loop variable for continued user input
                boolean openConnection = true;
                while(openConnection){
                    String message = userInput.readLine();
                    int welcomePort = connectionPort + 2;
                    ServerSocket welcomeSocket = new ServerSocket(welcomePort);
                   
                    
                    //list command
                    if(message.equals("list:")){
                        outToServer.writeBytes(welcomePort + " " + message +  " " + '\n');
                        Socket dataSocket = welcomeSocket.accept();
                        BufferedReader dataFromServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                        String fileName;
                        System.out.println("\nFiles on server:");
                        while(!(fileName = dataFromServer.readLine()).equals(EOF)){
                            System.out.println("  " + fileName);
                        }
                        System.out.println("(End of files)\n");
                        dataSocket.close();
                        welcomeSocket.close();
                    }
                    //retr command
                    else if(message.startsWith("retr:") || message.startsWith("stor:")){
                        String[] messageTokens = message.split("\\s");
                        if(messageTokens.length == 2){
                            String fileName = messageTokens[1];
                            String messageToServer = welcomePort + " " + message + " " + fileName + " " + '\n';
                            
                            if(messageTokens[0].equals("retr:")){
                                outToServer.writeBytes(messageToServer);
                                Socket dataSocket = welcomeSocket.accept();
                                BufferedReader dataFromServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream())); 
                                BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FILE_DIRECTORY +  "/" + fileName));
                                String fileLine;
                                try{
                                    if((fileLine = dataFromServer.readLine()).equals(FILE_FOUND)){
                                        System.out.println("Downloading file from server...");
                                        while(!(fileLine = dataFromServer.readLine()).equals(EOF)){
                                            fileWriter.write(fileLine + '\n');
                                        }
                                        System.out.println("File Downloaded!\n");
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
                                    fileWriter.close();
                                }
                            }
                            else if(messageTokens[0].equals("stor:")){
                                File file;
                                outToServer.writeBytes(messageToServer);
                                Socket dataSocket = welcomeSocket.accept();
                                DataOutputStream dataToServer = new DataOutputStream(dataSocket.getOutputStream());
                                if((file = FileClass.fileExists(FILE_DIRECTORY + "/" + messageTokens[1])) != null){
                                    dataToServer.writeBytes(FILE_FOUND + '\n');
                                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                                    String fileLine;
                                    System.out.println("Uploading file to server...");
                                    
                                    while((fileLine = fileReader.readLine()) != null){
                                        dataToServer.writeBytes(fileLine + '\n');
                                    }

                                    System.out.println("File uploaded!\n");
                                    dataToServer.writeBytes(EOF + '\n');
                                    fileReader.close();
                                    dataSocket.close();
                                }
                                else{
                                    dataToServer.writeBytes(FILE_NOT_FOUND + '\n');
                                    System.out.println("File does not exist");
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
                System.out.println("IO Exception");
            }
        }
        else{
            System.out.println("Inavalid connection arguments");
        }
    }


    private static boolean validConnectionInput(String[] tokens){
        if(tokens.length == 3){
            if(tokens[0].toUpperCase().equals("CONNECT")){
                try{
                    Integer.parseInt(tokens[2]);
                    return true;
                }
                catch(NumberFormatException ex){
                    System.out.println("Invalid port number");
                    return false;
                }
            }
            System.out.println("Invalid first argument");
            return false;
        }
        System.out.println("Invalid number of arguments");
        return false;
    }
}