package Chess;

public class Bishop extends ChessPiece {
/***********************************************************************
This will allow us to create a protected Bishop Player for each White
and Black Team
@param player will tell us whether the player is black or white
@return player to the instance variable
**********************************************************************/
	protected Bishop(Player player) {
		super(player);
	}
/***********************************************************************
Returns the type of this piece as Bishop
@param none
@return type to know that this piece is a Bishop
***********************************************************************/
	@Override
	public String type() {
		return "bishop";
	}

/**********************************************************************
We are checking to see if the move of this Bishop piece is valid
@param move We get the coordinates from where the piece 
was originally to 
where it will be moving
@param board
@return boolean is the move valid?
***********************************************************************/
	@Override
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		int endRow =move.toRow;
		int endCol = move.toColumn;
		int startRow = move.fromRow;
		int startCol = move.fromColumn;
		if (super.isValidMove(move,board) == false)
			return false;
		if (Math.abs(move.toRow - move.fromRow) == 
				(Math.abs(move.toColumn - move.fromColumn))){
			//if endRow > startRow and endCol >startCol (down and right)
			//if there is a piece on the line return false
			if(endRow>startRow && endCol>startCol){
				for(int i=1;i<endRow-startRow;i++)
					if(board[startCol+i][startRow+i]!=null)
						return false;
			}
			//if endRow > startRow and endCol <startCol (down and left)
			//if there is a piece on the line return false
			else if(endRow>startRow && endCol<startCol){
				for(int i=1;i<endRow-startRow;i++)
					if(board[startCol-i][startRow+i]!=null)
						return false;
			}
			//if endRow < startRow and endCol >startCol (up and right)
			//if there is a piece on the line return false
			else if(endRow<startRow && endCol>startCol){
				for(int i=1;i<startRow-endRow;i++)
					if(board[startCol+i][startRow-i]!=null)
						return false;
			}
			//if endRow < startRow and endCol <startCol (up and left)
			//if there is a piece on the line return false
			else if(endRow<startRow && endCol<startCol){
				for(int i=1;i<startRow-endRow;i++)
					if(board[startCol-i][startRow-i]!=null)
						return false;
			}
			return true;
		}else{
			return false;
		}
	}
}