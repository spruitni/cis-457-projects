package Chess;
import javax.swing.JFrame;

/**********************************************************************
 * The GUI that displays the JFrame for the Panel.
 * @author Brendan Nahed Daniel Wynalda Estavan Mares
 * @version (3/22/2016)
 *********************************************************************/ 
public class ChessGUI {
	private ChessPanel panel;
	private JFrame frame;

	public ChessGUI (){
		frame = new JFrame("Chess Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		panel = new ChessPanel(frame);
		frame.getContentPane().add(panel);

		frame.setSize(650,740);
		frame.setResizable(false);
		frame.setVisible(true);
	}

}