package Chess;
import java.io.*;

public class GVGameHub{

    /*
     * Start the game hub with a title, and instructions on how to begin.
     * It starts by asking for a game choice.
     */
    public GVGameHub(){
        boolean cont = true;
        System.out.println("****** Welcome to the GV Game Hub *******");
        System.out.println("2-player games available: Chess | Hangman");
        System.out.println("*****************************************");
        do{
            String message = getUserInput("Would you like to play Chess(c) or Hangman(h) or Quit(q)? ");
            if(message.equals("CHESS") || message.equals("C")){
                setUp("CHESS");
                cont = false;
            }
            else if(message.equals("HANGMAN") || message.equals("H")){
                setUp("HANGMAN");
                cont = false;
            }
            else if(message.equals("QUIT") || message.equals("Q")){
                System.out.println("Goodbye...");
                cont = false;
            }
        }while(cont);
    }

    /*
     * Returns the user input given a prompt from the command line
     * prompt: Prompt to send to the console
     */
    public String getUserInput(String prompt){
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(prompt);
        try{
            return userInput.readLine().trim().toUpperCase();
        }
        catch(IOException ex){
            return  "";
        }
    }

    /*
     * Sets up the game based on whether the client wants to be a client or server.
     * Will start the game of Chess or Hangman based on user's initial choice above.
     */
    public void setUp(String game){
        boolean cont = true;
        do{
           if(game.equals("CHESS")){
                        new ChessGUI();
                        cont = false;
            }
            else{
                String hostOrClient = getUserInput("Would you like to be a server(s) or client(c)? ");
                if(hostOrClient.equals("SERVER") || hostOrClient.equals("S")){
                    try{
                        int port = Integer.parseInt(getUserInput("Enter your listening port number: "));
                        new HostController(port);
                    
                    }
                    catch(NumberFormatException ex){
                        System.out.println("Invalid port");
                    }
                }
                else if(hostOrClient.equals("CLIENT") || hostOrClient.equals("C")){
                    try{
                        String ipAddress = getUserInput("Enter host IP address: ");
                        int port = Integer.parseInt(getUserInput("Enter host port number: "));
                        new ConnectController(ipAddress, port);
                        cont = false;
                    }
                    catch(NumberFormatException ex){
                        System.out.println("Invalid host port");
                    }
                }
            }
        }while(cont);
    }

    /*
     * Start the game hub
     */
    public static void main(String[] args){
        new GVGameHub();
    }
}
