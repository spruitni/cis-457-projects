package Chess;

import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**********************************************************************
 * The Panel of the projects makes the game come to life. I put all the
 * pieces on the board and displays it to the players for them to 
 * interact with it.
 * @author Brenden Nahed, Jake Geers, Nathan Lindenbaum, Nick Spruit
 * @version (12/3/2017)
 *********************************************************************/ 
public class ChessPanel extends JPanel {

	private JButton[][] board;
	private ChessModel model;
	private Player white = Player.WHITE;
	private Player black = Player.BLACK;
	private ImageIcon pawnW = new ImageIcon("1Pawn-white.png");
	private ImageIcon pawnB = new ImageIcon("1Pawn-black.png");
	private ImageIcon rookW = new ImageIcon("1Rook-white.png");
	private ImageIcon rookB = new ImageIcon("1Rook-black.png");
	private ImageIcon bishopW = new ImageIcon("1Bishop-white.png");
	private ImageIcon bishopB = new ImageIcon("1Bishop-black.png");
	private ImageIcon knightW = new ImageIcon("1Knight-white.png");
	private ImageIcon knightB = new ImageIcon("1Knight-black.png");
	private ImageIcon queenW = new ImageIcon("1Queen-white.png");
	private ImageIcon queenB = new ImageIcon("1Queen-black.png");
	private ImageIcon kingW = new ImageIcon("1King-white.png");
	private ImageIcon kingB = new ImageIcon("1King-black.png");
	private JPanel center;
	private JPanel south;
	private JPanel north;
	private JLabel player1;
	private JLabel player2;
	private JLabel playerTurn;
	private JButton quit;
	private JButton reset;
	private JButton connect;
	private JButton redo;
	private JFrame frame;
	private int count=0;
	private final int dimensions=8;
	private int player1W=0;
	private int player2W=0;
	private int fromRow, fromCol;
	private boolean firstClick = true;
	private DataOutputStream dos;
	private BufferedReader br;
	private ServerSocket serverSocket;
	private Socket clientSocket;
	private boolean isHost;
	private boolean writeMove;

	// declare other instance variables as needed
	
	private ButtonListener buttonListener = new ButtonListener();

/**********************************************************************
 *Chess Panel instantiates all the initial JPanels, JLabels, and JButtons
 *********************************************************************/ 
	public ChessPanel(JFrame frame) {
		// complete this
		center = new JPanel();
		south = new JPanel();
		north = new JPanel();
		player1 = new JLabel("White wins: "+player1W);
		player2 = new JLabel("Black wins: "+player2W);
		quit = new JButton("Quit");
		//reset = new JButton("Reset");
		connect = new JButton("Connect");
		//redo = new JButton("Redo");
		this.frame=frame;
		streams();
		newBoard();

	}

/**********************************************************************
 *New Board creates the new board for the player.
 *********************************************************************/
	private void newBoard(){
		model = new ChessModel();
		quit.addActionListener(buttonListener);
		//reset.addActionListener(buttonListener);
		//connect.addActionListener(buttonListener);
		//redo.addActionListener(buttonListener);
		//reset.setPreferredSize(new Dimension(15, 25));
		center.setLayout(new GridLayout(dimensions, dimensions));
		board = new JButton[dimensions][dimensions];
		south.setLayout(new BorderLayout(dimensions, dimensions));
		north.setLayout(new BorderLayout(dimensions, dimensions));
		for (int row = 0; row < dimensions; row++)
			for (int col = 0; col < dimensions; col++) {
				board[row][col] = new JButton();
				board[row][col].addActionListener(buttonListener);
				board[row][col].setPreferredSize(new Dimension(75,75));
				board[row][col].setEnabled(true);
				if ((row + col) % 2 == 0) {
					board[row][col].setBackground(Color.DARK_GRAY);
					board[row][col].setBorderPainted(false);
					board[row][col].setOpaque(true);
				}
				else{
					board[row][col].setBackground(Color.orange);
					board[row][col].setBorderPainted(false);
					board[row][col].setOpaque(true);
				}
				center.add(board[row][col]);
			}
		//playerTurn = new JLabel("It is "+ model.player +"'s turn.");
		//north.add(redo, BorderLayout.EAST);
//		north.add(connect, BorderLayout.WEST);
		north.add(playerTurn, BorderLayout.CENTER);
		south.add(player1, BorderLayout.WEST);
		south.add(player2, BorderLayout.EAST);
		south.add(quit , BorderLayout.CENTER);
		//south.add(reset, BorderLayout.SOUTH);
		if(!isHost){
			model.userPlayer=black;
			connect.addActionListener(buttonListener);
			north.add(connect, BorderLayout.WEST);
		}
		add(north, BorderLayout.NORTH);
		add(center, BorderLayout.CENTER);
		add(south, BorderLayout.SOUTH);
		displayBoard();

	}
/**********************************************************************
 *Reset Board resets the Board display and resets the model.
 *********************************************************************/
	private void resetBoard(){
		center.removeAll();
		south.removeAll();
		north.removeAll();
		model.clearBoard();
		newBoard();
		north.revalidate();
		center.revalidate();
		south.revalidate();
		north.repaint();
		center.repaint();
		south.repaint();
	}
/**********************************************************************
*Steams sets up the tcp connection with the opponent.
*********************************************************************/
	public void streams() {
		int type = 0;
			type = JOptionPane.showConfirmDialog(null, "Would you like to host a game?", null, JOptionPane.YES_NO_OPTION);
		if (type == JOptionPane.YES_OPTION) {
			try {
					playerTurn = new JLabel("Host Player: White");
					isHost = true;

	            	serverSocket = new ServerSocket(5050);
	            	clientSocket = serverSocket.accept();
	            	System.out.println("Connection created");
	            	dos = new DataOutputStream(clientSocket.getOutputStream());
	            	br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
	            	writeMove = true;
	            }
	            catch(IOException ex){
	            	System.out.println("Cannot Setup server");
	            	System.exit(1);
	            }
		}
		else if(type == JOptionPane.NO_OPTION) {
			try {
					playerTurn = new JLabel("Client Player: Black");
					isHost = false;
					String ipAddress = "";
					ipAddress = JOptionPane.showInputDialog("Enter Host IP");
					clientSocket = new Socket(ipAddress, 5050);
 	            	System.out.println("Connection created");
 	            	dos = new DataOutputStream(clientSocket.getOutputStream());
 	            	br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
 	            	writeMove = false;
 	            }
 	            catch(IOException ex){
 	            	System.out.println("Cannot Setup server");
 	            	System.exit(1);
 	            }  
		}
		else if(type == JOptionPane.CLOSED_OPTION){
			System.exit(0);
		}
	}
 /**********************************************************************
 *onlineMoves sends the move to the opponent through the TCP connection
 * established in streams.
 **********************************************************************/
	// Sending move coordinates to board
	public void onlineMoves(int fromRow, int fromCol, int toRow, int toCol) {
		//board[1][1].doClick();
		//board[2][1].doClick();
		displayBoard();
		if (writeMove) {
			try {
			
				dos.writeBytes(fromRow + " " + fromCol + " " + toRow + " " + toCol + "\n");
				System.out.println(fromRow + " " + fromCol + " " + toRow + " " + toCol);
				writeMove = false;
				readMoves();
			} catch (IOException ex) {
				System.out.println("Not Sent");
			}
		}
	}
/**********************************************************************
 * playerQuit sends the quit message to the opponent.
 **********************************************************************/
	
	public void playerQuit() {
		
		try {
			dos.writeBytes("quit");
		} catch (IOException ex) {
			System.out.println("Not Sent");
		}
		
	}

	public void quit(){
		try {
			br.close();
			dos.close();
			clientSocket.close();
			try {
				serverSocket.close();
			}catch(NullPointerException ex){

			}
		}catch(IOException ex){

		}
	}
/**********************************************************************
 * readMoves reads in from the buffered stream the opponent's last
 * move.
 **********************************************************************/
	
	public void readMoves() {

		try {
			String[] coordinates = br.readLine().split("\\s");
			if (coordinates[0].equals("quit")){
				quit();
				frame.dispose();
				
			} else{

				int netFromRow = Integer.parseInt(coordinates[0]);
				int netFromCol = Integer.parseInt(coordinates[1]);
				int netToRow = Integer.parseInt(coordinates[2]);
				int netToCol = Integer.parseInt(coordinates[3]);
				if(isHost)
					model.userPlayer=black;
				else
					model.userPlayer=white;
				board[netFromRow][netFromCol].doClick();
				board[netToRow][netToCol].doClick();
				writeMove = true;
				if(isHost)
					model.userPlayer=white;
				else
					model.userPlayer=black;
			}
		} catch (IOException ex) {
			System.out.println("Not read");
			System.exit(1);
		}
		
	}
	
/**********************************************************************
 * Display board displays the images and updates them as they move 
 * around the board.
*********************************************************************/ 
	// method that updates the board
	private void displayBoard() {
		for(int col=0; col < dimensions; col++)
			for(int row=0; row < dimensions; row++)
			{
				if (model.pieceAt(row, col) == null)
				{
					board[row][col].setIcon(null);
				}
				else if (model.pieceAt(row, col).player()== black ){
					if (model.pieceAt(row, col).type().equals("rook")){
						board[row][col].setIcon(getScaledImage(
								rookB.getImage(), 50, 50));
					}
					else if (model.pieceAt(row, col).type().equals("king")){
						board[row][col].setIcon(getScaledImage(
								kingB.getImage(),50, 50));
					}
					else if (model.pieceAt(row, col).type().equals("queen")){
						board[row][col].setIcon(getScaledImage(
								queenB.getImage(),50, 50));
					}
					else if (model.pieceAt(row, col).type().equals("knight")){
						board[row][col].setIcon(getScaledImage(
								knightB.getImage(),50, 50));
					}
					else if (model.pieceAt(row, col).type().equals("bishop")){
						board[row][col].setIcon(getScaledImage(
								bishopB.getImage(),50, 50));
					}
					else if (model.pieceAt(row, col).type().equals("pawn")){
						board[row][col].setIcon(getScaledImage(
								pawnB.getImage(),50, 50));
					}
				}
				else if (model.pieceAt(row, col).player()== white ){
					if (model.pieceAt(row, col).type().equals("rook")){
						board[row][col].setIcon(getScaledImage(
								rookW.getImage(),50, 50));
					}
					else if (model.pieceAt(row, col)
							.type().equals("king")){
						board[row][col].setIcon(getScaledImage
								(kingW.getImage(),50, 50));
					}
					else if (model.pieceAt(row, col)
							.type().equals("queen")){
						board[row][col].setIcon(
								getScaledImage(queenW.getImage()
								,50, 50));
					}
					else if (model.pieceAt(row, col)
							.type().equals("knight")){
						board[row][col].setIcon(
								getScaledImage(knightW.getImage()
								,50, 50));
					}
					else if (model.pieceAt(row, col)
							.type().equals("bishop")){
						board[row][col].setIcon(
								getScaledImage(bishopW.getImage()
								,50, 50));
					}
					else if (model.pieceAt(row, col)
							.type().equals("pawn")){
						board[row][col].setIcon(
								getScaledImage(pawnW.getImage()
								,50, 50));
						north.remove(playerTurn);
						//playerTurn =
						//		new JLabel("It is "+ 
						//model.player +"'s turn.");
						north.add(playerTurn, BorderLayout.CENTER);
						north.repaint();
						north.revalidate();
					}
				}
			}
	}
/**********************************************************************
 * Method that resizes the shapes of the icons to fit inside of the
 * buttons in the center JPanel
 * @param scrImg - gets the Image to be resized
 * @param w - the new width of the icon
 * @param h - the new height of the icon
 * ********************************************************************/
	public static ImageIcon getScaledImage(Image scrImg, int w , int h){
		Image img = scrImg;
		BufferedImage bi = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(img, 0, 0, w, h, null, null);
		ImageIcon newIcon = new ImageIcon(bi);
		return newIcon;
	}
	// add other helper methods as needed

/**********************************************************************
 * The Button Listener is the listener for when a button is pressed.
*********************************************************************/ 
	// inner class that represents action listener for buttons
	private class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {

			// retrieve move and execute
			if(event.getSource() == connect){
				
				readMoves();

			}
			// send the move
//			else if(event.getSource() == redo){
//
//				int fRow = model.moves[model.getCounter()-1].fromRow;
//                int fCol = model.moves[model.getCounter()-1].fromColumn;
//                int tRow = model.moves[model.getCounter()-1].toRow;
//                int tCol = model.moves[model.getCounter()-1].toColumn;
//
//
//
//
//			}
			else if(event.getSource() == quit) {
				playerQuit();
				quit();
				frame.dispose();

				}
//			else if(event.getSource()==reset) {
//				resetBoard();
//			}
			else if(firstClick==true){
				for(int row=0; row<dimensions; row++)
					for(int col=0; col<dimensions; col++)
						if(event.getSource() == board[row][col]){
							if(model.pieceAt(row, col)!=null&&
									model.pieceAt(row, col).player()
											==model.player){
								firstClick=false;
							}
							fromRow=row;
							fromCol=col;
						}
			}
			else
			{
				firstClick=true;
				for(int row=0; row<dimensions; row++)
					for(int col=0; col<dimensions; col++)
						if(event.getSource() == board[row][col]){
							Move move1=(new Move(fromRow, fromCol
									, row, col));
							if(model.isValidMove(move1, model.userPlayer)){
								if(!model.causesCheck(move1)){
									model.killedPieces
											[model.getCounter()]=
											model.pieceAt(row, col);
									model.moves[model.getCounter()]
											=move1;
									if(model.isCastle(move1)){
										model.typeOfMove
												[model.getCounter()]=2;
										if(move1.toColumn==1)
											model.move(
													new Move(
															move1.fromRow,0,
															move1.fromRow,2));
										if(move1.toColumn==5)
											model.move(
													new Move(move1.fromRow,
															7,move1.fromRow,4));
									}
									else if(model.isEnPassant(move1)){
										model.typeOfMove
												[model.getCounter()]=3;
										if(move1.toRow==2){
											model.killedPieces
													[model.getCounter()]
													=model.pieceAt(
													move1.toRow+1,
													move1.toColumn);
											model.removePiece(
													move1.toRow+1,
													move1.toColumn);
										}
										if(move1.toRow==5){
											model.removePiece(
													move1.toRow-1,
													move1.toColumn);
										}
									}
									else{
										model.typeOfMove
												[model.getCounter()]=1;
									}

									model.setCounter
											(model.getCounter()+1);
									model.setHasMoved(move1, true);
									model.move(move1);
									model.nextPlayer();
									pawnUpgrade();
									onlineMoves(fromRow, fromCol, row, col);
								}
								else{
									JOptionPane.showMessageDialog(null,
											model.player+
													" would be in Check");
								}
							}
							else if(model.pieceAt(row, col) !=null){
								if(model.pieceAt(row, col).player()
										==model.player){
									firstClick=false;
									fromRow=row;
									fromCol=col;
								}
							}
//							sFromRow = fromRow;
//							sFromCol = fromCol;
//							sToRow = row;
//							sToCol = col;
							//performSend = true;
							//onlineMoves(fromRow, fromCol, row, col);
						}
				if(model.isComplete()){
					if(model.player ==Player.WHITE){
						player2W++;
					}else if(model.player==Player.BLACK){
						player1W++;
					}
					player1.setText("White wins: "+player1W);
					player2.setText("Black wins: "+player2W);
					displayBoard();
					int reply= JOptionPane.showConfirmDialog(null,
							model.player+" is now in Checkmate. "
									+"\n Would you like to play again?",
							null, JOptionPane.YES_NO_OPTION);

					if (reply == JOptionPane.YES_OPTION) {
						resetBoard();
					}
					else if(reply ==JOptionPane.NO_OPTION){
						System.exit(0);
					}
				}
				else if(model.inCheck(model.player)){
					if (count == 0){
						displayBoard();
						JOptionPane.showMessageDialog(null,model.player+
								" is in Check");
						firstClick=true;
						count++;
					}
				}
				else{
					count=0;
				}
				//onlineMoves(fromRow, fromCol, row, col);
				displayBoard();
			}
		}

	}

/**************************************************
 * PawnUpgrade checks if a pawn has made it across the board then 
 * displays a message asking for what new piece the current player
 * would want.
*********************************************************************/ 
	public void pawnUpgrade(){
		if(model.player != Player.WHITE){
			for(int col=0; col<dimensions; col++){
				if(model.pieceAt(0, col)!=null){
					try{
					if(model.pieceAt(0, col).type()=="pawn"){
						JList list = new JList(new String []{"Queen"
								,"Rook","Bishop", "Knight"});
						JOptionPane.showMessageDialog
						(null, list, "Pawn Promotion", 
								JOptionPane.PLAIN_MESSAGE);
						int[]numPieces=model.totalPieces(model.player);
						boolean noneLeft=true;
						if(numPieces[list.getSelectedIndex()]>0){
							model.pawnUpgrade(list.getSelectedIndex(),
									0, col);
						}
						else{
							for(int i =0; i<4; i++){
								if(numPieces[i]!=0){
									noneLeft=false;
								}
							}
							if(noneLeft == true ){
								JOptionPane.showMessageDialog(null,""
										+ "You have no piece available "
										+ "so you get an extra Queen");
								model.pawnUpgrade(0,
										0, col);
							}
							else{
								JOptionPane.showMessageDialog(null,""
										+ "You do not have that piece"
										+ " available");
								pawnUpgrade();
						}
					}
					}
					}
					catch(Throwable e){
						JOptionPane.showMessageDialog(null,""
								+ "You must select a piece");
						pawnUpgrade();
						}
				}
			}
		}
		else if(model.player != Player.BLACK){
			for(int col=0; col<dimensions; col++){
				if(model.pieceAt(7, col)!=null){
					if(model.pieceAt(7, col).type()=="pawn"){
						try{
						JList list = new JList(new String []{"Queen"
								,"Rook","Bishop", "Knight"});
						JOptionPane.showMessageDialog
						(null, list, "Which would "
								+ "you like?", 
								JOptionPane.PLAIN_MESSAGE);
						int[]numPieces=model.totalPieces(model.player);
						boolean noneLeft = true;
						
						if(numPieces[list.getSelectedIndex()]>0){
							model.pawnUpgrade(list.getSelectedIndex(),
									0, col);
						}
						else{
							
							for(int i =0; i<4; i++){
								if(numPieces[i]!=0){
									noneLeft=false;
								}
							}
							if(noneLeft ==true){
								JOptionPane.showMessageDialog(null,""
										+"You have no pieces available."
										+" So I am giving you a Queen");
								model.pawnUpgrade(0,
										7, col);
							}
							else{
								JOptionPane.showMessageDialog(null,""
										+ "You do not have that piece"
										+ " available");
								pawnUpgrade();
							}
						}
						}
						catch(Throwable e){
							JOptionPane.showMessageDialog(null,""
									+ "You must select a piece");
							pawnUpgrade();
						}
					}
				}
			}
		}
	}

}