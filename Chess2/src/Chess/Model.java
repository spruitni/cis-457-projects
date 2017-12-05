package Chess;
public class Model{

    private String word;
    private char[] wordProgress;
    private int numberOfGuesses;
    private char[] letterGuesses;

    public Model(){ }

    /*
     * Creates a new game by resetting all the parameters
     * word: The word that is to be guessed by the guesser
     * difficulty: The level of difficulty that determines the numebr of guesses allowed
     */
    public void newGame(String word, Difficulty difficulty){
        this.word = word.toUpperCase();
        this.wordProgress = new char[word.length()];
        for(int i = 0; i < wordProgress.length; i++){
            wordProgress[i] = '_';
        }
        this.numberOfGuesses = difficulty.guesses();
        this.letterGuesses = new char[this.numberOfGuesses];
    }

    /*
     * Return true if the letter exists in the word, otherwise false, and decrements guess count
     * letter: Character represents the letter to search
     */
    public boolean guessLetter(char letter){
        numberOfGuesses--;
        int position = word.indexOf(letter);
        if(position >= 0){
            updateGuess(letter);
            return true;
        }
        return false;
    }

    /*
     * Return true if the word is equals to the guessed word, otherwise false
     */
    public boolean wordGuessed(){
        return getWord().equals(getWordProgress());
    }

    /*
     * Update the guessed word with a letter
     * letter: The letter to update in the guessed word
     */
    private void updateGuess(char letter){
        for(int i = 0; i < word.length(); i++){
            if(word.charAt(i) == letter){
                wordProgress[i] = letter;
            }
        }
    }

    /*
     * Return true if the word is guessed correctly
     * guess: The word to compare against the actual word
     */
    public boolean guessWord(String guess){
        numberOfGuesses--;
        if(guess.toUpperCase().equals(word)){
            return true;
        }
        return false;
    }

    /*
     * Return true if the guesser is allowed to continue guessing letters
     */
    public boolean allowGuess(){
        if(numberOfGuesses > 0){
            return true;
        }
        return false;
    }

    /*
     * Returns true if the letter has already been guessed, otherwise false
     * letter: The letter being guessed
     */
    public boolean alreadyGuessed(char letter){
        for(char guessedLetter : letterGuesses){
            if(guessedLetter == letter){
                return true;
            }
        }
        return false;
    }

    /*
     * Returns true if the word only has letters and is not empty
     * word: The word to set
     */
    public boolean validWord(String word){
        return word.chars().allMatch(Character::isLetter) && !word.isEmpty() && !(word == null);
    }


    /*
     * Setters and Getters
     */
    public String getWord(){ return word; }
    public String getWordProgress(){ return new String(wordProgress); }
    public int getnumberOfGuesses(){ return numberOfGuesses; }
}