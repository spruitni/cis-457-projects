package Chess;

public class Rook extends ChessPiece {
/**********************************************************************
This will allow us to create a protected Rook Player for each White
and Black Team
@param player will tell us whether the player is black or white
@return player to the instance variable
***********************************************************************/
	protected Rook(Player player) {
		super(player);
	}
/***********************************************************************
Returns the type of this piece as Rook
@param none
@return type to know that this piece is a Rook
***********************************************************************/
	public String type() {
		return "rook";
	}
/**********************************************************************
 We are checking to see if the move of this Rook piece is valid
@param move We get the coordinates 
from where the piece was originally to where it will be moving
@param board
@return boolean is the move valid?
**********************************************************************/
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		int endRow =move.toRow;
		int endCol = move.toColumn;
		int startRow = move.fromRow;
		int startCol = move.fromColumn;
		if (super.isValidMove(move,board) == false)
			return false;
		//if column and row both change returns false
		if (move.toRow != move.fromRow && move.toColumn 
				!=move.fromColumn)
			return false;
		
		if(endRow>startRow && startCol == endCol){
			for(int i=startRow+1; i<=endRow-1; i++)
				if(board[startCol][i] !=null)
					return false;
		}
		else if(endRow<startRow &&startCol == endCol){
			for(int i=startRow-1; i>=endRow+1; i--)
				if(board[startCol][i] !=null)
					return false;
		}
		else if(endCol>startCol && startRow == endRow){
			for(int i=startCol+1; i<=endCol-1; i++)
				if(board[i][startRow] !=null)
					return false;
		}
		else if(endCol<startCol && startRow == endRow){
			for(int i=startCol-1; i>=endCol+1; i--)
				if(board[i][startRow] !=null)
					return false;
		}
		//otherwise returns true
		return true;
	}
}