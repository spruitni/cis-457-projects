package Chess;

public class Knight extends ChessPiece {
/**********************************************************************
This will allow us to create a protected Knight Player for each White
and Black Team
@param player will tell us whether the player is black or white
@return player to the instance variable
***********************************************************************/
	protected Knight(Player player) {
		super(player);
	}
/***********************************************************************
 Returns the type of this piece as Knight
@param none
@return type to know that this piece is a Knight
**********************************************************************/
	@Override
	public String type() {
		return "knight";
	}
/**********************************************************************
We are checking to see if the move of this Knight piece is valid
@param move We get the coordinates from where the piece was
 originally to where it will be moving
@param board
@return boolean is the move valid?
***********************************************************************/
	@Override
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		if (super.isValidMove(move,board) == false)
			return false;
		//if column changes by more than 2 or row changes 
		//by more than 2 returns false
		if(Math.abs(move.fromColumn - move.toColumn)>2 || 
				Math.abs(move.fromRow - move.toRow) >2)
			return false;
		//if change in row + change in column does not equal 3 
		//returns false
		else if (Math.abs(move.fromColumn - move.toColumn) + 
				Math.abs(move.fromRow - move.toRow) !=3)
			return false;
		//if column or row changes by 2 and the other changes by 1 
		//returns true
		else 
			return true;
	}
}