package Chess;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.ArrayList;

public class View extends JFrame{

    private final int ALPHA_LENGTH = 26;
    private JLabel wordLabel, remainingLabel, player1Label, player2Label, player1Score, player2Score;
    private JButton[] letterButtons;
    private JTextField wordInput, wordInput2;
    private JButton setWordButton, guessWordButton, quitButton;
    private JPanel topPanel, middlePanel, bottomPanel, mainPanel;
    private Font font;
    private boolean isHost;

    public View(boolean isHost){

        //Crete components
        mainPanel = new JPanel(new GridLayout(3, 1));
        topPanel = new JPanel(new GridBagLayout());
        middlePanel = new JPanel(new GridBagLayout());
        bottomPanel = new JPanel(new GridBagLayout());
        player1Label = new JLabel();
        player2Label = new JLabel();
        player1Score = new JLabel();
        player2Score = new JLabel();
        remainingLabel = new JLabel();
        wordLabel = new JLabel();
        wordInput = new JTextField();
        wordInput2 = new JTextField();
        setWordButton = new JButton("Set Word");
        guessWordButton = new JButton("Guess Word");
        quitButton = new JButton("Quit");
        this.isHost = isHost;

        //Set fonts for labels, inputs and buttons
        font = new Font("Monospaced", Font.PLAIN, 20);
        wordLabel.setFont(font);
        guessWordButton.setFont(font);
        setWordButton.setFont(font);
        quitButton.setFont(font);
        wordInput.setFont(font);
        wordInput2.setFont(font);
        remainingLabel.setFont(font);
        player1Label.setFont(font);
        player2Label.setFont(font);
        player1Score.setFont(font);
        player2Score.setFont(font);

        //Set player names and scores
        if(this.isHost){
            player1Label.setText("Player 1 (You) ");
            player2Label.setText("Player 2 ");
        }
        else{
            player1Label.setText("Player 1 ");
            player2Label.setText("Player 2 (You)");
        }
        this.setScore(0);
        this.setOpponentScore(0);

        //Add all letters to the game panel
        letterButtons = new JButton[ALPHA_LENGTH];
        int length = letterButtons.length;
        bottomPanel.add(remainingLabel, getGBC(0, 0, length/2, 1, false, false));
        for(int i = 0; i < length; i++){
            letterButtons[i] = new JButton(Character.toString((char) (i + 65)));
            letterButtons[i].setFont(font);
            if(i < (length/2.0)){
                bottomPanel.add(letterButtons[i], getGBC(i, 1, 1, 1, false, false));
            }
            else{
                bottomPanel.add(letterButtons[i], getGBC(i % (length/2), 2, 1, 1, false, false));
            }

        }

        //Add guess word input and button
        topPanel.add(player1Label, getGBC(0, 0 , 4, 1, true, false));
        topPanel.add(player1Score, getGBC(4, 0, 4, 1, true, false));
        topPanel.add(player2Label, getGBC(0, 1 , 4, 1, true, false));
        topPanel.add(player2Score, getGBC(4, 1, 4, 1, true, false));
        topPanel.add(quitButton, getGBC(4, 2, 4, 1, true, false));
        middlePanel.add(wordLabel, getGBC(0, 0, 8, 1, true, false));
        bottomPanel.add(wordInput, getGBC(0, 3, 7, 1, true, false));
        bottomPanel.add(guessWordButton, getGBC(7, 3, 6, 1, true, false));
        bottomPanel.add(wordInput2, getGBC(0, 4, 7, 1, true, false));
        bottomPanel.add(setWordButton, getGBC(7, 4, 6, 1, true, false));

        //Add borders
        topPanel.setBorder(new TitledBorder("Score"));
        middlePanel.setBorder(new TitledBorder("Word"));
        bottomPanel.setBorder(new TitledBorder("Guess"));

        //Sizes of text boxes
        wordInput.setPreferredSize(new Dimension(200, 50));
        wordInput2.setPreferredSize(new Dimension(200, 50));

        //Add to main panel
        mainPanel.add(topPanel);
        mainPanel.add(middlePanel);
        mainPanel.add(bottomPanel);
        frameSetup();
    }

    /*
     * Sets up the frame size, title, etc.
     */
    private void frameSetup(){
        this.setTitle("Hangman");
        this.add(mainPanel);
        this.pack();
        this.setSize(900, 850);
        this.getContentPane();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

    public void displayError(String errorMessage){
        JLabel error = new JLabel(errorMessage);
        error.setFont(font);
        JOptionPane.showMessageDialog(this, error, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void displayGuesserWin(String word){
        JLabel message = new JLabel("Congratulations, you guessed: " + word);
        message.setFont(font);
        JOptionPane.showMessageDialog(this, message, "You Win!", JOptionPane.PLAIN_MESSAGE);
    }
    public void displayGuesserLose(String word){
        JLabel message = new JLabel("Sorry, you lost. Correct word: " + word);
        message.setFont(font);
        JOptionPane.showMessageDialog(this, message, "You Lose!", JOptionPane.PLAIN_MESSAGE);
    }
    public void displayHostWin(String word){
        JLabel message = new JLabel("Congratulations, your opponent could not guess: " + word);
        message.setFont(font);
        JOptionPane.showMessageDialog(this, message, "You Win!", JOptionPane.PLAIN_MESSAGE);
    }
    public void displayHostLose(String word){
        JLabel message = new JLabel("Sorry, you lost. Your opponent guessed: " + word);
        message.setFont(font);
        JOptionPane.showMessageDialog(this, message, "You Lose!", JOptionPane.PLAIN_MESSAGE);
    }

    public void setScore(int score){
        if(this.isHost){
            player1Score.setText("Score: " + Integer.toString(score));
        }
        else{
            player2Score.setText("Score: " + Integer.toString(score));
        }
    }
    public void setOpponentScore(int score){
        if(this.isHost){
            player2Score.setText("Score: " + Integer.toString(score));
        }
        else{
            player1Score.setText("Score: " + Integer.toString(score));
        }
    }
    public void setWordLabel(String word){
        String newWord = "";
        for(int i = 0; i < word.length(); i++){
            newWord += word.charAt(i) + " ";
        }
        this.wordLabel.setText(newWord);
    }
    public void setRemainingLabel(int remaining){
        this.remainingLabel.setText("Remaining guesses: " + remaining);
    }
    public JButton getSetWordButton(){
        return this.setWordButton;
    }
    public JButton getGuessWordButton(){
        return this.guessWordButton;
    }
    public JButton[] getLetterButtons(){
        return letterButtons;
    }
    public String getGuessWord(){
        return wordInput.getText();
    }
    public String getSetWord(){
        return wordInput2.getText();
    }
    public void clearGuessWord(){
        wordInput.setText("");
    }
    public void clearSetWord(){
        wordInput2.setText("");
    }
    public void setAsGuesser(){
        for(JButton button : letterButtons){
            button.setEnabled(true);
        }
        guessWordButton.setEnabled(true);
        setWordButton.setEnabled(false);
    }
    public void setAsHost(){
        for(JButton button : letterButtons){
            button.setEnabled(false);
        }
        guessWordButton.setEnabled(false);
        setWordButton.setEnabled(true);
    }
    public void setToWait(){
        for(JButton button : letterButtons){
            button.setEnabled(false);
        }
        guessWordButton.setEnabled(false);
        setWordButton.setEnabled(false);
    }
    public void disableSetWord(){
        setWordButton.setEnabled(false);
    }
    public void clearWord(){
        wordLabel.setText("");
    }
    public void clearRemaining(){
        remainingLabel.setText("");
    }
    public JButton getQuitButton(){
        return quitButton;
    }
    public void resetScores(){
        player1Score.setText("Score: 0");
        player2Score.setText("Score: 0");
    }
    public void quitMessage(int score, int opponentScore){
        JLabel message = new JLabel("Game is quitting. Final score: You: " + score + ", Opponent: " + opponentScore);
        message.setFont(font);
        JOptionPane.showMessageDialog(this, message, "Goodbye", JOptionPane.PLAIN_MESSAGE);
    }
}
