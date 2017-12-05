package Chess;

/*
 * The difficulty levels, based on number of guesses allowed
 */
public enum Difficulty{
    VERY_EASY(10),
    EASY(9),
    MEDIUM(8),
    HARD(7),
    VERY_HARD(6);

    private int guesses;
    Difficulty(int guesses){
        this.guesses = guesses;
    }

    public int guesses(){
        return guesses;
    }
}
