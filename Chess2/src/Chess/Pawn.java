package Chess;
public class Pawn extends ChessPiece {
	
/**********************************************************************
This will allow us to create a protected Pawn Player for each White
and Black Team
@param player will tell us whether the player is black or white
@return player to the instance variable
**********************************************************************/
	protected Pawn(Player player) {
		super(player);
	}
/**********************************************************************
Returns the type of this piece as Pawn
@param none
@return type to know that this piece is a Pawn
**********************************************************************/
	public String type() {
		return "pawn";
	}
/**********************************************************************
We are checking to see if the move of this Pawn piece is valid
@param move We get the coordinates from where 
the piece was originally to 
where it will be moving
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
		if(move.fromColumn == move.toColumn 
				&& board[move.toColumn][move.toRow] != null)
		{
			return false;
		}
		//if the pawn is white
		if(board[move.fromColumn][move.fromRow].player() 
				== Player.WHITE){			
			//If player tries to move white pawn down the 
			//screen return false
			if(move.toRow > move.fromRow)
				return false;
			//If the white pawn tries en passant
			if(move.fromRow==3 && move.toRow==2){
				if(move.fromColumn<7 && move.fromColumn>0){
					if(board[move.fromColumn+1][move.fromRow]!=null){
						if
						(board[move.fromColumn+1][move.fromRow].type()==
						"pawn" && 
						board[move.fromColumn+1][move.fromRow].
						player()==Player.BLACK){
							if(move.toColumn==move.fromColumn+1)
							return true;
						}
					}
					if (board[move.fromColumn-1][move.fromRow] !=null){
						if(
						board[move.fromColumn-1][move.fromRow].type()==
						"pawn" &&board[move.fromColumn-1][move.fromRow].
						player()==Player.BLACK){
							if(move.toColumn==move.fromColumn-1)
							return true;
						}
				}
				}
			}
			//if white pawn tries to move diagonal
			if((move.fromColumn+1 == move.toColumn && 
					move.fromRow-1== move.toRow) ||
					(move.fromColumn-1 == move.toColumn && 
					move.fromRow-1 == move.toRow)){
				//if the option is null diagonal is not allowed
				if(board[move.toColumn][move.toRow] == null){
					return false;
				}
				//if player at destination is black it is allowed
				else if(board[move.toColumn][move.toRow].player() 
						== Player.BLACK)
					return true;
				//otherwise it is not
			}
			
			//if the pawn leaves its column
			if(move.toColumn!=move.fromColumn)
				return false;
			//if white pawn tries to move two spaces forward 
			//from the starting row it returns false
			if(move.fromRow==6 && move.toRow==4
					&& board[move.fromColumn][5]==null)
				return true;
			//if white pawn tries to move one space forward 
			//from any other row
			else if(move.toRow == move.fromRow-1)
				return true;
			else
				return false;
		}
		//If the pawn is not White (i.e. Black)
		else
		{//If player tries to move black pawn up the screen return false
			if(move.toRow < move.fromRow)
				return false;
			//if black pawn wants to enPassant
			else if(move.fromRow==4 && move.toRow==5 
					&& (move.toColumn==move.fromColumn-1 ||
					move.toColumn==move.fromColumn+1)){
				if(move.fromColumn<7 && move.fromColumn>0){
					if(board[move.fromColumn+1][move.fromRow]!=null)
						if(
						board[move.fromColumn+1][move.fromRow].type()==
						"pawn" && 
						board[move.fromColumn+1][move.fromRow].
						player()==Player.WHITE){
							if(move.toColumn==move.fromColumn+1)
								if(move.toColumn==move.fromColumn+1)
							return true;
						}
					if (board[move.fromColumn-1][move.fromRow] !=null)
						if(
						board[move.fromColumn-1][move.fromRow].type()==
						"pawn" &&board[move.fromColumn-1][move.fromRow].
						player()==Player.WHITE)
							if(move.toColumn==move.fromColumn-1)
								if(move.toColumn==move.fromColumn-1)
							return true;
				}
			}
			//if black pawn tries to move diagonal
			if((move.fromColumn+1 == move.toColumn && 
					move.fromRow+1 == move.toRow) ||
					(move.fromColumn-1 == move.toColumn && 
					move.fromRow+1 == move.toRow))
			{
				if(board[move.toColumn][move.toRow] == null){
					return false;
				}	
				//if player at destination is white diagonal is allowed
				else if(board[move.toColumn][move.toRow].player()
						== Player.WHITE)
					return true;
			}
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
			//if the pawn leaves its column
			if(move.toColumn!=move.fromColumn)
				return false;
			//if black pawn tries to move more than two spaces forward 
			//from the starting row
			if(move.fromRow==1 && move.toRow==3
					&& board[move.fromColumn][2]==null)
				return true;
			//if black pawn tries to move more than one space forward 
			//from any other row
			else if(move.toRow == move.fromRow+1)
				return true;
			else
				return false;
		}
	}
}