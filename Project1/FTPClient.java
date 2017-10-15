import java.io.*; 
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;


public class FTPClient{

    public static void main(String agv[]) throws Exception{
        final String EOF = "eof";
        final String FILE_DIRECTORY = "clientFiles/";
        int connectionPort = 0;
        String ipAddress = null;
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        String[] tokens = userInput.readLine().split("\\s");
        if(validConnectionInput(tokens)){
            ipAddress = tokens[1];
            connectionPort = Integer.parseInt(tokens[2]);
            Socket connectionSocket = null;
            try{
                String welcomeMessage = "Welcome to the Text FTP Server!";
                String possibleCommands = "list:  |  retr:  |  stor:  |  quit";
                connectionSocket = new Socket(ipAddress, connectionPort);
                System.out.println(welcomeMessage);
                boolean openConnection = true;
                //Connection socket always needed
                DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());

                while(openConnection){
                    String message = userInput.readLine();
                    int welcomePort = connectionPort + 2;
                    ServerSocket welcomeSocket = new ServerSocket(welcomePort);
                    System.out.println("HERE");
                    
                    //list command
                    if(message.equals("list:")){
                        outToServer.writeBytes(welcomePort + " " + message +  " " + '\n');
                        Socket dataSocket = welcomeSocket.accept();
                        BufferedReader dataFromServer = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                        String fileName;
                        while(!(fileName = dataFromServer.readLine()).equals(EOF)){
                            System.out.println(fileName);
                        }
                        System.out.println("End of files");
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
                                    if((fileLine = dataFromServer.readLine()).equals("200")){
                                        System.out.println("Downloading file from server...");
                                        while(!(fileLine = dataFromServer.readLine()).equals(EOF)){
                                            fileWriter.write(fileLine + '\n');
                                        }
                                        System.out.println("File Downloaded");
                                        dataSocket.close();
                                    }
                                    else{
                                        System.out.println("File not found");
                                    }
                                }
                                catch(IOException ex){
                                    System.out.println("IO Exception: " + ex);
                                }
                                fileWriter.close();
                            }
                            else if(messageTokens[0].equals("stor:")){
                                File file;
                                if((file = FileClass.fileExists(FILE_DIRECTORY + "/" + messageTokens[1])) != null){
                                    outToServer.writeBytes(messageToServer);
                                    Socket dataSocket = welcomeSocket.accept();
                                    DataOutputStream dataToServer = new DataOutputStream(dataSocket.getOutputStream());
                                    BufferedReader fileReader = new BufferedReader(new FileReader(file));
                                    String fileLine;
                                    System.out.println("Uploading file to server...");
                                    System.out.println("HERE");
                                    while((fileLine = fileReader.readLine()) != null){
                                        dataToServer.writeBytes(fileLine + '\n');
                                    }
                                    dataToServer.writeBytes(EOF + '\n');
                                    dataSocket.close();
                                }
                            }
                        }
                        else{
                            System.out.println("Invalid number of arguments for 'retr:' command");
                        }
                    }
                    else if(message.equals("quit")){
                        openConnection = false;
                        //////////////////////////////////////////////
                        //TODO: "write" quit to server so it can close
                        //////////////////////////////////////////////
                    }
                    else{
                        System.out.println("Invalid command");
                        System.out.println(possibleCommands);
                        welcomeSocket.close();
                    }
                }
                try{
                    connectionSocket.close();
                    System.out.println("Closing connection...");
                }
                catch(IOException ex){
                    System.out.println("Unable to disconnect: (IO EX: " + ex + ")");
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