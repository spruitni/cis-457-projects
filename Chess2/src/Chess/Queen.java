package Chess;

public class Queen extends ChessPiece {

/**********************************************************************
This will allow us to create a protected Queen Player for each White
and Black Team
@param player will tell us whether the player is black or white
@return player to the instance variable
**********************************************************************/
	protected Queen(Player player) {
		super(player);
	}
/**********************************************************************	
 Returns the type of this piece as Queen
@param none
@return type to know that this piece is a Queen
***********************************************************************/
	@Override
	public String type() {
		return "queen";
	}
/*****************************************************************
We are checking to see if the move of this Queen piece is valid
@param move We get the coordinates
from where the piece was originally to 
where it will be moving
@param board
@return boolean is the move valid?
**********************************************************************/
	@Override
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		Rook rook1=
				new Rook(board[move.fromColumn][move.fromRow].player());
		Bishop bishop1=
				new Bishop(
						board[move.fromColumn][move.fromRow].player());

		if(bishop1.isValidMove(move, board) 
				|| rook1.isValidMove(move, board))
			return true;
		else
			return false;
	}
}