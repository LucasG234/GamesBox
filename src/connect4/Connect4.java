package connect4;

import main.*;
import resources.ResourceLoader;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class Connect4 extends Game
{
	//Player one is red, player two is yellow
	//inherits turn (true when player one's turn), oneWins, twoWins,
	//centerPanel (to place game), turnText and winText
	
	JLabel[][] pieces;
	
	private JLabel turnDisplay;
	private JLabel winDisplay;
	
	//member variable so can refresh all
	private Connect4Col[] cols;
	
	public Connect4()
	{
		super("Connect 4");
		
		pieces = new JLabel[6][7];
		cols = new Connect4Col[7];
		
		centerPane.add(makeBoard());
		
		setRightPane();
	}
	
	/**
	 * Construct a JPanel holding all buttons and an image background
	 * 
	 * @return the JPanel
	 */
	private JPanel makeBoard()
	{
		JPanel board = new JPanel();
		board.setBackground(Color.WHITE);
		board.setBorder(new EmptyBorder(175,200,175,200));
		
		OverlayLayout boardManager = new OverlayLayout(board);
		board.setLayout(boardManager);
		
		JLabel boardImage = new JLabel(new ImageIcon(ResourceLoader.getImage("Connect4Board.png")));
		board.add(boardImage, 0);
		boardImage.setAlignmentX(CENTER_ALIGNMENT);
		boardImage.setAlignmentY(CENTER_ALIGNMENT);
		
		board.add(makeCols(), 1);
		board.add(makePieces(), 2);
		
		return board;
	}
	
	/**
	 * Construct a JPanel holding all the clickable columns users will interact with
	 * 
	 * @return the JPanel
	 */
	private JPanel makeCols()
	{
		JPanel holdsCols = new JPanel();
		holdsCols.setLayout(new GridLayout(1,7));
		holdsCols.setAlignmentX(CENTER_ALIGNMENT);
		holdsCols.setAlignmentY(CENTER_ALIGNMENT);
		holdsCols.setOpaque(false);
		
		for(int i = 0; i < 7; i++)
		{
			Connect4Col col = new Connect4Col(this, i);
			cols[i] = col;
			holdsCols.add(col);
		}
		
		return holdsCols;
	}
	
	/**
	 * Constructs a JPanel holding empty JLabels, which will be used as unclickable pieces
	 * Adds all JLabels to the pieces array
	 * 
	 * @return The JPanel holding the JLabels
	 */
	private JPanel makePieces()
	{
		
		JPanel holdsPieces = new JPanel();
		holdsPieces.setPreferredSize(new Dimension(600,450));
		FlowLayout layout = new FlowLayout();
		layout.setHgap(0);
		layout.setVgap(0);
		holdsPieces.setLayout(layout);
		
		//add top, shorter row
		for(int i = 0; i < 7; i++)
		{
			JLabel piece = new JLabel();
			piece.setPreferredSize(new Dimension(82,66));
			piece.setBackground(Color.white);
			piece.setOpaque(true);
			
			holdsPieces.add(piece);
			pieces[0][i] = piece;
		}
		
		//add middle rows of larger pieces
		for(int o = 1; o < 5; o++)
		{
			for(int i = 0; i < 7; i++)
			{
				JLabel piece = new JLabel();
				piece.setPreferredSize(new Dimension(82,74));
				piece.setBackground(Color.white);
				piece.setOpaque(true);
				
				holdsPieces.add(piece);
				pieces[o][i] = piece;
			}
		}
		
		//add short bottom row
		for(int i = 0; i < 7; i++)
		{
			JLabel piece = new JLabel();
			piece.setPreferredSize(new Dimension(82,66));
			piece.setBackground(Color.white);
			piece.setOpaque(true);
			
			holdsPieces.add(piece);
			pieces[5][i] = piece;
		}
		
		return holdsPieces;
	}
	
	/**
	 * Sets up all swing components and configurations for the win and turn display
	 */
	private void setRightPane()
	{
		JLabel winOne = new JLabel("WINS");
		winOne.setFont(new Font("",Font.PLAIN, 100));
		winOne.setBorder(new EmptyBorder(0,0,20,0));
		
		JLabel winTwo = new JLabel("R          Y");
		winTwo.setFont(new Font("",Font.PLAIN, 64));
		winTwo.setBorder(new EmptyBorder(0,0,10,0));
		
		winDisplay = new JLabel(oneWins + "                         " + twoWins);
		winDisplay.setFont(new Font("",Font.PLAIN, 32));
		
		winText.add(winOne);
		winText.add(winTwo);
		winText.add(winDisplay);
		
		turnDisplay = new JLabel("It is R's turn"); //X will always take first turn on init
		turnDisplay.setFont(new Font("",Font.PLAIN,64));
		
		turnText.add(turnDisplay);
	}
	
	/**
	 * To be called upon a turn being taken
	 * Switches who's turn it is, and handles all end game cases
	 */
	public void refresh()
	{
		//switch turn and display
		turn = !turn;
		if(turn)
			turnDisplay.setText("It is R's turn");
		else
			turnDisplay.setText("It is Y's turn");
		
		//ensures that everything is drawn correctly
		invalidate();
		revalidate();
		repaint();
		
		int win = checkEnd();
		//handle end game cases and reset game
		switch(win)
		{
		case 1:
			{
				new End("Red");
				oneWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); //just keeps it centered up to 99 wins
				break;
			}
		case 2:
			{
				new End("Yellow");
				twoWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); 
				break;
			}
		case -1:
			{
				new End("No one");
				break;
			}
		}
		
		if(win != 0)
		{
			restartGame();
		}
	}
	
	/**
	 * Checks whether the game has ended, and if so who has won
	 * 
	 * @return -1 = tie, 0 = no win, 1 = X win, 2 = O win
	 */
	private int checkEnd()
	{
		
		//check for horizontal wins	
		for(int r = 0; r < 6; r++)
		{
			JLabel[] toCheck = new JLabel[7];
			for(int c = 0; c < 7; c++)
			{
				toCheck[c] = pieces[r][c];
			}
			
			int isWinner = winHere(toCheck);
			if(isWinner != 0)
				return isWinner;
		}
		
		//check for vertical wins
		for(int c = 0; c < 7; c++)
		{
			JLabel[] toCheck = new JLabel[6];
			for(int r = 0; r < 6; r++)
			{
				toCheck[r] = pieces[r][c];
			}
			
			int isWinner = winHere(toCheck);
			if(isWinner != 0)
				return isWinner;
		}
		
		//check for diagonal downward wins
		for(int r = 0; r < 3; r++)
		{
			for(int c = 0; c < 4; c++)
			{
				JLabel[] toCheck = {pieces[r][c], pieces[r+1][c+1], pieces[r+2][c+2], pieces[r+3][c+3]};
				
				int isWinner = winHere(toCheck);
				if(isWinner != 0)
					return isWinner;
			}
		}
		
		//check for diagonal upwards wins
		for(int r = 0; r <3 ; r++)
		{
			for(int c = 6; c > 2; c--)
			{
				JLabel[] toCheck = {pieces[r][c], pieces[r+1][c-1], pieces[r+2][c-2], pieces[r+3][c-3]};
				
				int isWinner = winHere(toCheck);
				if(isWinner != 0)
					return isWinner;
			}
		}
		
		//check if full
		for(JLabel[] row : pieces)
		{
			for(JLabel j : row)
			{
				if(j.getBackground() != Color.RED && j.getBackground() != Color.YELLOW)
					return 0;
			}
		}
		
		//if it gets through check, must be full
		return -1;
	}
	
	/**
	 * Scans through a given array of pieces, and checks if there is a win (4 in a row) within them
	 * 
	 * @param selection : Array holding pieces to be checked
	 * 
	 * @return 0 = no win in selection, 1 = red win, 2 = yellow win
	 */
	private int winHere(JLabel[] selection)
	{
		//return 0 if no win in selection, 1 if red win in selection, 2 if yellow win in selection
		int redInARow = 0;
		int yelInARow = 0;
		for(JLabel j : selection)
		{
			if(j.getBackground() == Color.red)
			{
				redInARow++;
				yelInARow = 0;
			}
			else if(j.getBackground() == Color.yellow)
			{
				yelInARow++;
				redInARow = 0;
			}
			else
			{
				redInARow = 0;
				yelInARow = 0;
			}
			
			if(redInARow == 4)
				return 1;
			if(yelInARow == 4)
				return 2;
		}
		return 0;
		
	}
	
	/**
	 * To be called upon a game ending
	 * Resets all pieces and columns
	 * Sets red back as first player
	 */
	private void restartGame()
	{
		for(JLabel[] row : pieces)
		{
			for(JLabel j : row)
				j.setBackground(Color.WHITE);
		}
		
		for(Connect4Col col : cols)
		{
			col.r = 5;
		}
		
		turn = true;
		turnDisplay.setText("It is R's turn");
		
		//redraw all to fix any errors
		invalidate();
		revalidate();
		repaint();
	}
	
	public boolean isRedTurn()
	{
		return turn;
	}
}
