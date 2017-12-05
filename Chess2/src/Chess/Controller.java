package Chess;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import javax.swing.JButton;
import java.awt.event.WindowEvent;
import javax.swing.*;
import java.awt.Font;
import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public abstract class Controller{
    private int score;
    private int opponentScore;
    private boolean isHost;
    private Model model;
    private View view;
    private ActionListener actionListener;
    private WindowListener windowListener;

    //Creates a view and model
    public Controller(boolean isHost){
        this.model = new Model();
        this.view = new View(isHost);
        this.score = 0;
        this.opponentScore = 0;

        //Initially, set the host as the word host, client as the word guesser
        if(isHost){
            view.setAsHost();
        }
        else{
            view.setToWait();
        }
    }

    private void updateGUI(){
        view.setRemainingLabel(model.getnumberOfGuesses());
        view.setWordLabel(model.getWordProgress());
    }

    private void resetGUI(){
        view.clearRemaining();
        view.setWordLabel("");
        view.setToWait();
    }

    public void listen(BufferedReader br, DataOutputStream dos){
        boolean cont = true;
        while(cont){
            try{
                String message = br.readLine();
                String[] messageParts = message.split("\\s");
                if(messageParts[0].equals("Letter")){
                    model.guessLetter(messageParts[1].charAt(0));
                    if(model.wordGuessed()){
                        opponentScore++;
                        view.setOpponentScore(score);
                        view.displayHostLose(model.getWord());
                        model = new Model();
                        view.setToWait();
                        resetGUI();
                    }
                    else if(!model.allowGuess() && !model.wordGuessed()){
                        score++;
                        view.setScore(score);
                        view.displayHostWin(model.getWord());
                        model = new Model();
                        resetGUI();
                        view.setToWait();
                    }
                    else{
                        updateGUI();
                    }
                }
                else if(messageParts[0].equals("Guess")){
                    if(model.guessWord(messageParts[1])){
                        opponentScore++;
                        view.setOpponentScore(opponentScore);
                        view.displayHostLose(model.getWord());
                        model = new Model();
                        resetGUI();
                        view.setToWait();
                    }
                    else{
                        updateGUI();
                    }
                }
                else if(messageParts[0].equals("Set")){
                    view.setAsGuesser();
                    model.newGame(messageParts[1], Difficulty.EASY);
                    updateGUI();
                }
                else if(messageParts[0].equals("quit")){
                    view.quitMessage(score, opponentScore);
                    view.dispose();
                    dos.writeBytes("quitListening");
                    cont = false;
                }
                else if(messageParts[0].equals("quitListening")){
                    cont = false;
                }
            }
            catch(IOException ex){
                System.out.println("Error reading message");
            }
        }
    }


    //Listens for GUI events
    public void control(DataOutputStream dos){

        //Listen for button clicked, etc.
        actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for(JButton button : view.getLetterButtons()){
                    if(e.getSource() == button){
                        button.setEnabled(false);
                        String letter = button.getText();
                        try{
                            model.guessLetter(letter.charAt(0));
                            if(model.wordGuessed()){
                                score++;
                                view.setScore(score);
                                view.displayGuesserWin(model.getWord());
                                model = new Model();
                                resetGUI();
                                view.setAsHost();
                            }
                            else if(!model.allowGuess() && !model.wordGuessed()){
                                opponentScore++;
                                view.setOpponentScore(opponentScore);
                                view.displayGuesserLose(model.getWord());
                                model = new Model();
                                resetGUI();
                                view.setToWait();
                                view.setAsHost();
                            }
                            else{
                                updateGUI();
                            }
                            dos.writeBytes("Letter " + letter + "\n");
                        }
                        catch(IOException ex){
                            System.out.println("Error writing letter");
                        }
                        break;
                    }
                }
                if(e.getSource() == view.getGuessWordButton()){
                    String word = view.getGuessWord();
                    view.clearGuessWord();
                    try{
                        if(model.guessWord(word)){
                            score++;
                            view.setScore(score);
                            view.displayGuesserWin(model.getWord());
                            resetGUI();
                            model = new Model();
                            view.setAsHost();
                        }
                        else{
                            updateGUI();
                        }
                        dos.writeBytes("Guess " + word + "\n");
                    }
                    catch(IOException ex){
                        System.out.println("Error guessing word");
                    }
                }
                else if(e.getSource() == view.getSetWordButton()){
                    try{
                        String word = view.getSetWord();
                        view.clearSetWord();
                        if(model.validWord(word)){
                            model.newGame(word, Difficulty.EASY);
                            dos.writeBytes("Set " + word + "\n");
                            view.setWordLabel(model.getWordProgress());
                            view.disableSetWord();
                        }
                        else{
                            view.displayError("Invalid word selection '" + word + " '");
                        }
                    }
                    catch(IOException ex){
                        System.out.println("Error setting word");
                    }
                }
                else if(e.getSource() == view.getQuitButton()){
                    try{
                        view.quitMessage(score, opponentScore);
                        dos.writeBytes("quit\n");
                        view.dispose();
                    }
                    catch(IOException ex){
                        System.out.println("Error quitting");
                    }
                }
            }
        };

        //Listen for window close, then close connection
        windowListener = new WindowListener(){
            public void windowClosing(WindowEvent event){
            }

            //Not really needed, but abstract interface methods need be overridden
            public void windowClosed(WindowEvent event){}
            public void windowOpened(WindowEvent event){}
            public void windowIconified(WindowEvent event){}
            public void windowDeiconified(WindowEvent event){}
            public void windowActivated(WindowEvent event){}
            public void windowDeactivated(WindowEvent event){}
            public void windowGainedFocus(WindowEvent event){}
            public void windowLostFocus(WindowEvent event){}
            public void windowStateChanged(WindowEvent event){}
        };

        //Add listeners to GUI components
        view.getSetWordButton().addActionListener(actionListener);
        view.getGuessWordButton().addActionListener(actionListener);
        view.getQuitButton().addActionListener(actionListener);
        for(JButton button : view.getLetterButtons()){
            button.addActionListener(actionListener);
        }
    }
}