package Chess;

public class King extends ChessPiece{
/**********************************************************************
This will allow us to create a protected King Player for each White
and Black Team
@param player will tell us whether the player is black or white
@return player to the instance variable
***********************************************************************/
	protected King(Player player) {
		super(player);
	}
/**********************************************************************
Returns the type of this piece as King
@param none
@return type to know that this piece is a King
**********************************************************************/
	@Override
	public String type() {
		return "king";
	}
/**********************************************************************
We are checking to see if the move of this King piece is valid
@param move We get the coordinates from where the piece 
was originally to 
where it will be moving
@param board
@return boolean is the move valid?
**********************************************************************/
	@Override
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		if (super.isValidMove(move,board) == false)
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
						if (board[7][move.fromRow]
								.isValidMove(move2, board)){
							return true;
						}
				}

		//Checks to make sure everything is within 1 spot away or else 
		//it returns false
		if (move.toColumn - move.fromColumn > 1 || 
				move.toRow - move.fromRow > 1 || 
				( move.toColumn - move.fromColumn < -1 || 
						move.toRow - move.fromRow < -1)){
			return false;
		}
		else{
			return true;
		}
	}
}