package Chess;
/**********************************************************************
 *The move Class handles the moving of the pieces
 * @author Brenden Nahed, Jake Geers, Nathan Lindenbaum, Nick Spruit
 * @version (12/3/2017)
 *********************************************************************/ 
public class Move {

	public int fromRow, fromColumn, toRow, toColumn;

	public Move() {
	}
/**********************************************************************
 * Method that moves the pieces.
 * @param fromRow; The initial row.
 * @param fromCol; The initial column.
 * @param toRow; The row the piece wants to move to.
 * @param toColumn; The Column the piece wants to move to.
 *********************************************************************/ 
	public Move(int fromRow, int fromColumn, int toRow, int toColumn) {
		this.fromRow = fromRow;
		this.fromColumn = fromColumn;
		this.toRow = toRow;
		this.toColumn = toColumn;
	}
}
