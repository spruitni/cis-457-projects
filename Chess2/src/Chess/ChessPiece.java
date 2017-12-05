package Chess;
/**********************************************************************
 * ChessPiece class that impletments IChessPiece
 * @author Brenden Nahed, Jake Geers, Nathan Lindenbaum, Nick Spruit
 * @version (12/3/2017)
 *********************************************************************/ 
public abstract class ChessPiece implements IChessPiece{
	private Player owner;
	private boolean hasMoved=false;
	protected ChessPiece(Player player) {
		this.owner = player;
	}
	public abstract String type();
	public Player player() {
		return owner;
	}
	
	
/**********************************************************************
 * Boolean that tells if a piece has moved.
 * @return hasMoved; Boolean
 *********************************************************************/ 
	public boolean hasMoved() {
		return hasMoved;
	}
/**********************************************************************
 *Set has moved sets if a piece has moved.
 *@param hasMoved; Boolean that sets if a piece has moved.
 *********************************************************************/ 
	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
/**********************************************************************
 * The Panel of the projects makes the game come to life. I put all the
 * pieces on the board and displays it to the players for them to 
 * interact with it.
 * @author Brendan Nahed Daniel Wynalda Estavan Mares
 * @version (3/22/2016)
 *********************************************************************/ 
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		if(board[move.fromColumn][move.fromRow] ==null)
			return false;
		//if the user tries to move the piece to where it already is
		if(move.toColumn == move.fromColumn && move.toRow == 
				move.fromRow)
			return false;

		//if the piece in the destination is not owned by 
		//the current player
		if(board[move.toColumn][move.toRow] == null){
			;//do nothing
		}	
		else if(board[move.toColumn][move.toRow].player()
				==this.player())
			return false;
		return true;

	}

}