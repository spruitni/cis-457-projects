package Chess;
import javax.swing.JFrame;

/**********************************************************************
 * The GUI that displays the JFrame for the Panel.
 * @author Brenden Nahed, Jake Geers, Nathan Lindenbaum, Nick Spruit
 * @version (12/3/2017)
 *********************************************************************/ 
public class ChessGUI {
	private ChessPanel panel;
	private JFrame frame;

/**
 * ChessGUI is the constructor for that builds the frame and panel for
 * the Chess game.
 */
	public ChessGUI() {
		
		JFrame frame = new JFrame("Chess Game");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		panel = new ChessPanel(frame);
		frame.getContentPane().add(panel);
		
		frame.setSize(650,740);
		frame.setResizable(false);
		frame.setVisible(true);
		
	}
}