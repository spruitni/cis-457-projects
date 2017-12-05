package Chess;

import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JOptionPane;

/**********************************************************************
 * The following class generates the game of Chess. It keeps track of
 * the pieces, check if moves are valid. It also runs the undo and redo
 * button. Handles the Pawn Upgrade. The code brings all the pieces 
 * together and has them work together.
 * @author Brenden Nahed, Jake Geers, Nathan Lindenbaum, Nick Spruit
 * @version (12/3/2017)
 *********************************************************************/

public class ChessModel implements IChessModel {
    private IChessPiece[][] board;
    private Player white = Player.WHITE;
    private Player black = Player.BLACK;
    public Player player=white;
    public Player userPlayer = white;
    public Move moves[] = new Move[1000000];
    public IChessPiece killedPieces[] = new IChessPiece[1000000];
    public int typeOfMove[] = new int[1000000];
    //1 = "normal" 2 = castle 3 = enPassant
    private int counter=0;
    private final int dimensions=8;

    /**********************************************************************
     * Get counter gets the counter.
     * @return counter
     *********************************************************************/

    public int getCounter() {
        return counter;
    }
    /**********************************************************************
     * Set counter sets the counter to the counter parameter.
     * @param counter
     *********************************************************************/
    public void setCounter(int counter) {
        this.counter = counter;
    }
    /**********************************************************************
     * ChessModel creates the board and sets the pieces to their initial
     * position. Also sets the color of the pieces.
     *********************************************************************/
    // declare other instance variables as needed
    public ChessModel() {
        board = new IChessPiece[dimensions][dimensions];
        board[0][0]=new Rook(black);
        board[1][0]=new Knight(black);
        board[2][0]=new Bishop(black);
        board[3][0]=new King(black);
        board[4][0]=new Queen(black);
        board[5][0]=new Bishop(black);
        board[6][0]=new Knight(black);
        board[7][0]=new Rook(black);
        for (int i=0; i<dimensions; i++)
            board[i][1] = new Pawn(black);

        board[0][7]=new Rook(white);
        board[1][7]=new Knight(white);
        board[2][7]=new Bishop(white);
        board[3][7]=new King(white);
        board[4][7]=new Queen(white);
        board[5][7]=new Bishop(white);
        board[6][7]=new Knight(white);
        board[7][7]=new Rook(white);
        for (int i=0; i<dimensions; i++)
            board[i][6] = new Pawn(white);

    }
    /**********************************************************************
     *Is Complete checks if the game is over.
     *@return false; If the game is not over.
     *@return true; If the game is over
     *********************************************************************/
    public boolean isComplete() {
        if (!this.inCheck(player)){
            return false;
        }
        for(int row = 0; row <dimensions; row++)
            for(int col=0; col<dimensions; col++){
                if(board[col][row]!=null)
                    if(board[col][row].player()==player){
                        for(
                                int newRow = 0; newRow <dimensions; newRow++)
                            for(
                                    int newCol=0; newCol<dimensions; newCol++){
                                //creates a move that moves a piece to one
                                //of the spaces the piece can move to.
                                Move move1= new Move(row, col, newRow, newCol);
                                //if the move is valid and the player is the current
                                //player and the move does not cause check returns false
                                if (this.isValidMove(move1)
                                        && !this.causesCheck(move1)
                                        &&inCheck(player)){
                                    return false;
                                }
                            }
                    }
            }
        return true;
    }

    /**********************************************************************
     * The is Valid move checks if the enter move is valid. Though it is
     * overridden in the individual isValidMove methods in each piece.
     * @param move; The initial move entered form the panel.
     * @return true; If the move is valid.
     *********************************************************************/
    public boolean isValidMove(Move move) {
        return (board[move.fromColumn][move.fromRow]
                .isValidMove(move, board));
    }
    /**********************************************************************
     * The is Valid move checks if the enter move is valid. Though it is
     * overridden in the individual isValidMove methods in each piece.
     * @param move; The initial move entered form the panel.
     * @param inPlayer; The user to compare to the turn.
     * @return true; If the move is valid.
     *********************************************************************/
    public boolean isValidMove(Move move, Player inPlayer){
        if(this.player == inPlayer){
            return isValidMove(move);
        }
        return false;
    }
    /**********************************************************************
     * The next Player changes the turn.
     *********************************************************************/
    public void nextPlayer(){
        player=player.next();
    }

    /**********************************************************************
     *Clear board resets the board to a new game.
     *********************************************************************/
    public void clearBoard(){
        for(int row = 0; row <dimensions; row++)
            for(int col=0; col<dimensions; col++){
                board[row][col]= null;
            }
    }
    /**********************************************************************
     * Move moves the actual players on the board when called upon by the
     * panel.
     * @param move; The move the is being entered from the panel.
     *********************************************************************/
    public void move(Move move) {
        board[move.toColumn][move.toRow] =
                board[move.fromColumn][move.fromRow];
        board[move.fromColumn][move.fromRow]=null;
    }
    /**********************************************************************
     * Moves the player back to its precious spot.
     * @param move; The previous move.
     * @param tempChessPiece; The piece being entered to undo.
     *********************************************************************/
    public void unMove(Move move, IChessPiece tempChessPiece) {
        board[move.fromColumn][move.fromRow] =
                board[move.toColumn][move.toRow];
        board[move.toColumn][move.toRow]=tempChessPiece;
    }
    /**********************************************************************
     * Checks if the entered player is in check.
     * @param p; The current player.
     * @return true; If the player is in check.
     * @return false; If the player is not in check
     *********************************************************************/
    public boolean inCheck(Player p) {
        int kingCol=-1;
        int kingRow=-1;
        for(int row = 0; row <dimensions; row++)
            for(int col=0; col<dimensions; col++){
                if(board[col][row]!=null){
                    if(board[col][row].type()=="king" &&
                            board[col][row].player()==p){
                        kingCol=col;
                        kingRow=row;
                    }
                }
            }
        if(kingCol!=-1 && kingRow!=-1)
            if(board[kingCol][kingRow].player()== p){
                for(int row = 0; row <dimensions; row++)
                    for(int col=0; col<dimensions; col++){
                        if(board[col][row]!=null){
                            if(board[col][row].player()!=p){
                                Move move=
                                        new Move(row, col,kingRow, kingCol);
                                if (this.isValidMove(move)){
                                    return true;
                                }
                            }
                        }
                    }
            }
        return false;
    }
    /**********************************************************************
     * The isEnPassant checks if the en passant move is availble for the
     * pawn.
     * @param move; If the enter move is an En Passant
     * @return false; If the move is not an En Passant
     * @return true; If the move is an EnPassant
     *********************************************************************/
    public boolean isEnPassant(Move move){
        if(board[move.toColumn][move.toRow]!=null)
            return false;
        //if the white pawn wants to do en poissant
        if(move.fromRow==3 && move.toRow==2
                && (move.toColumn==move.fromColumn-1 ||
                move.toColumn==move.fromColumn+1)){
            if(move.fromColumn<7 && move.fromColumn>0){
                if(board[move.fromColumn+1][move.fromRow]!=null){
                    if(board[move.fromColumn+1][move.fromRow].type()==
                            "pawn" &&
                            board[move.fromColumn+1][move.fromRow].
                                    player()==player.BLACK)
                        if(move.toColumn==move.fromColumn+1)
                            return true;
                }
                if (board[move.fromColumn-1][move.fromRow] !=null){
                    if(board[move.fromColumn-1][move.fromRow].type()==
                            "pawn" &&
                            board[move.fromColumn-1][move.fromRow].
                                    player()==player.BLACK)
                        if(move.toColumn==move.fromColumn-1)
                            return true;
                }
            }
        }
        //if black pawn wants to enPassant
        else if(move.fromRow==4 && move.toRow==5
                && (move.toColumn==move.fromColumn-1 ||
                move.toColumn==move.fromColumn+1)){
            if(move.fromColumn<7 && move.fromColumn>0){
                if(board[move.fromColumn+1][move.fromRow]!=null)
                    if(board[move.fromColumn+1][move.fromRow].type()==
                            "pawn" && board[move.fromColumn+1][move.fromRow].
                            player()==player.WHITE)
                        if(move.toColumn==move.fromColumn+1)
                            return true;
                if (board[move.fromColumn-1][move.fromRow] !=null)
                    if(board[move.fromColumn-1][move.fromRow].type()==
                            "pawn" &&board[move.fromColumn-1][move.fromRow].
                            player()==player.WHITE)
                        if(move.toColumn==move.fromColumn-1)
                            return true;
            }
        }
        return false;

    }
    /**********************************************************************
     * Is Castle checks if the move selected if a castling move, and checks
     * if it is valid to do.
     * @param move; If the entered move from the panel is a Castling move.
     * @return false; If the entered move is not a valid castling move.
     * @return true; If the entered move is a valid castling move.
     *********************************************************************/
    public boolean isCastle(Move move){
        if(board[move.fromColumn][move.fromRow].type()!="king")
            return false;
        Move move1, move2;
        move1=new Move(move.fromRow,0,move.fromRow,2);
        move2=new Move(move.fromRow,7,move.fromRow,4);

        if((move.toColumn==1 || move.toColumn==5)  && move.fromRow==
                move.toRow && (move.fromRow ==7 || move.fromRow==0))
            if (board[move.fromColumn][move.fromRow].hasMoved()==false)
                if(board[0][move.fromRow] !=null){
                    if(board[0][move.fromRow].isValidMove(move1,
                            board)){
                        return true;
                    }
                    if(board[7][move.fromRow] !=null)
                        if (board[7][move.fromRow].isValidMove(move2, board)){
                            return true;
                        }
                }
        return false;
    }
    /**********************************************************************
     * This method checks if the entered move cause check.
     * @param move; The entered move being tested.
     * @return true; If the entered move keeps the player in check.
     * @return false; If the entered move takes the player out of check.
     *********************************************************************/
    public boolean causesCheck(Move move){
        IChessPiece tempChessPiece=board[move.toColumn][move.toRow];
        move(move);
        // Checks if the entered move keeps the player in check or takes
        //them of check.
        if(inCheck(player)){
            unMove(move, tempChessPiece);
            return true;
        }
        else{
            unMove(move, tempChessPiece);
            return false;
        }
    }
    /**********************************************************************
     * Method that checks if a piece has moved;
     * @param move; if a move has been used;
     * @param hasMoved; boolean that tells if a piece has moved or not.
     *********************************************************************/
    public void setHasMoved(Move move, boolean hasMoved){
        if(board[move.fromColumn][move.fromRow]!=null)
            board[move.fromColumn][move.fromRow].setHasMoved(hasMoved);
    }
    /**********************************************************************
     * Remove piece removes pieces from the chess board.
     * @param row; row that the piece is at.
     * @param col; col that the piece is at.
     *********************************************************************/
    public void removePiece(int row, int col){
        board[col][row]=null;
    }
    /**********************************************************************
     * New pawn creates a new pawn.
     * @param row; Row that the new pawn will be placed.
     * @param col; Column that the new pawn will be placed.
     *********************************************************************/
    public void newPawn(int row, int col){
        board[col][row]=new Pawn(player);
    }
    /**********************************************************************
     * The current Player tells the panel, when called upon by the panel,
     * who the current player is.
     * @return player; The current player.
     *********************************************************************/
    public Player currentPlayer() {
        return player;
    }
    /**********************************************************************
     * Allows the user to set the current player.
     *********************************************************************/
    public void setCurrentPlayer(Player p){
        player = p;
    }
    /**********************************************************************
     * Number of rows, is the rows on the board.
     * @return dimensions; The amount of rows on the chess board
     *********************************************************************/
    public int numRows() {
        return dimensions;
    }
    /**********************************************************************
     * Number of columns, is the columns on the board.
     * @return dimensions; The amount of columns on the chess board
     *********************************************************************/
    public int numColumns() {
        return dimensions;
    }
    /**********************************************************************
     * Piece at finds pieces on the board for the panel when called upon.
     * @param row; The entered row the panel is looking at.
     * @param column; The entered column the panel is looking at.
     * @return board[column][row]; The place on the board.
     *********************************************************************/
    public IChessPiece pieceAt(int row, int column) {
        return board[column][row];
    }
    /**********************************************************************
     * Pawn upgrade upgrades the pawn when it reaches the opposite side of
     * the board.
     * @param piece; The new piece the player wants.
     * @param row; What row on the board they want the new piece.
     * @param col; What column on the board they want the new piece.
     *********************************************************************/
    public void pawnUpgrade(int piece, int row, int col){
        if(player != white){

            if(piece==0){
                board[col][row]=null;
                board[col][row]=
                        new Queen(white);
            }
            if(piece==1){
                board[col][row]=null;
                board[col][row]=
                        new Rook(white);
            }
            if(piece==2){
                board[col][row]=null;
                board[col][row]=
                        new Bishop(white);
            }
            if(piece==3){
                board[col][row]=null;
                board[col][row]=
                        new Knight(white);
            }
        }
        if(player != black){
            if(piece==0){
                board[col][row]=null;
                board[col][row]=
                        new Queen(black);
            }
            if(piece==1){
                board[col][row]=null;
                board[col][row]=
                        new Rook(black);
            }
            if(piece==2){
                board[col][row]=null;
                board[col][row]=
                        new Bishop(black);
            }
            if(piece==3){
                board[col][row]=null;
                board[col][row]=
                        new Knight(black);
            }
        }
        typeOfMove[counter]=4;
    }
    /**********************************************************************
     * Total pieces counts the amount of queens, rooks, bishops, and knights
     * on the board.
     * @param p; The current player.
     * @return numPieces; The array of pieces on the board.
     *********************************************************************/
    public int[] totalPieces(Player p){
        int[] numPieces={1,2,2,2};
        for(int row=0; row<dimensions; row++)
            for(int col=0; col<dimensions; col++){
                if(board[col][row]!=null){
                    if(board[col][row].player()!=p){
                        if(board[col][row].type()=="queen"){
                            numPieces[0]--;
                        }
                        else if(board[col][row].type()=="rook"){
                            numPieces[1]--;
                        }
                        else if(board[col][row].type()=="bishop"){
                            numPieces[2]--;
                        }
                        else if(board[col][row].type()=="knight"){
                            numPieces[3]--;
                        }
                    }
                }
            }
        return numPieces;
    }
}