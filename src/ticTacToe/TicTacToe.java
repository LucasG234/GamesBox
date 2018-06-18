package ticTacToe;

import main.*; //import my own main package
import resources.ResourceLoader;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class TicTacToe extends Game
{
	//Player one is X, player two is O
	//inherits turn (true when player one's turn), oneWins, twoWins,
	//centerPanel (to place game), turnText and winText
	private TicTacToePiece[][] pieces;
	
	private JLabel turnDisplay;
	private JLabel winDisplay;

	public TicTacToe()
	{
		super("Tic Tac Toe");
		
		pieces = new TicTacToePiece[3][3];
		
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
		board.setBorder(new EmptyBorder(100,200,100,200)); //makes board fit exactly
		
		OverlayLayout boardManager = new OverlayLayout(board);
		board.setLayout(boardManager);
		
		JLabel boardImage = new JLabel(new ImageIcon(ResourceLoader.getImage("TicTacToeBoard.png")));
		board.add(boardImage);
		boardImage.setAlignmentX(CENTER_ALIGNMENT);
		boardImage.setAlignmentY(CENTER_ALIGNMENT);
		
		board.add(makeButtons());
		
		return board;
	}
	
	/**
	 * Constructs a JPanel which holds all of the clickable pieces
	 * Populates the pieces array with these TicTacToePieces
	 * 
	 * @return JPanel holding the buttons
	 */
	private JPanel makeButtons()
	{
		JPanel holdsButtons = new JPanel();
		holdsButtons.setLayout(new GridLayout(3,3));
		holdsButtons.setAlignmentX(CENTER_ALIGNMENT);
		holdsButtons.setAlignmentY(CENTER_ALIGNMENT);
		holdsButtons.setOpaque(false);
		
		for(int o = 0; o < 3; o++)
		{
			for(int i = 0; i < 3; i++)
			{
			TicTacToePiece button = new TicTacToePiece(this);
			pieces[o][i] = button;
			holdsButtons.add(button);
			}
		}
		
		return holdsButtons;
	}
	
	/**
	 * Sets up all swing components and configurations for the win and turn display
	 */
	private void setRightPane()
	{
		JLabel winOne = new JLabel("WINS");
		winOne.setFont(new Font("",Font.PLAIN, 100));
		winOne.setBorder(new EmptyBorder(0,0,20,0));
		
		JLabel winTwo = new JLabel("X          O");
		winTwo.setFont(new Font("",Font.PLAIN, 64));
		winTwo.setBorder(new EmptyBorder(0,0,10,0));
		
		winDisplay = new JLabel(oneWins + "                         " + twoWins);
		winDisplay.setFont(new Font("",Font.PLAIN, 32));
		
		winText.add(winOne);
		winText.add(winTwo);
		winText.add(winDisplay);
		
		turnDisplay = new JLabel("It is X's turn"); //X will always take first turn on init
		turnDisplay.setFont(new Font("",Font.PLAIN,64));
		
		turnText.add(turnDisplay);
	}
	
	/**
	 * To be called upon a turn being taken
	 * Switches who's turn it is, and handles all end game cases
	 */
	public void refresh()
	{	
		turn = !turn; //switch who's turn it is
		
		//change the turn display
		if(turn)
			turnDisplay.setText("It is X's turn");
		else
			turnDisplay.setText("It is O's turn");
		
		int win = checkEnd();
		
		//end the game and increment win Counter if someone wins
		switch(win)
		{
		case 1:
			{
				new End("X");
				oneWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); //just keeps it centered up to 99 wins
				break;
			}
		case 2:
			{
				new End("O");
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
		
		//reset the game if it has ended
		if(win != 0)
		{
			resetGame();
		}
	}
	
	/**
	 * Checks whether the game has ended, and if so who has won
	 * 
	 * @return -1 = tie, 0 = no win, 1 = X win, 2 = O win
	 */
	private int checkEnd() 
	{
		//impossible for both pieces to win on same turn
		
		int win = 0;
		
		for(int i = 0; i < 3; i++)
		{
			//check if win in this row
			if(isWin("X", pieces[i][0],pieces[i][1],pieces[i][2]))
				win = 1;
			if(isWin("O", pieces[i][0],pieces[i][1],pieces[i][2]))
				win = 2;
			
			//check if win in this column
			if(isWin("X", pieces[0][i],pieces[1][i],pieces[2][i]))
				win = 1;
			if(isWin("O", pieces[0][i],pieces[1][i],pieces[2][i]))
				win = 2;
		}
		
		//check diagonal wins
		if(isWin("X", pieces[0][0],pieces[1][1],pieces[2][2]))
			win = 1;
		if(isWin("O", pieces[0][0],pieces[1][1],pieces[2][2]))
			win = 2;
		
		if(isWin("X", pieces[0][2],pieces[1][1],pieces[2][0]))
			win = 1;
		if(isWin("O", pieces[0][2],pieces[1][1],pieces[2][0]))
			win = 2;
		
		
		if(win == 0 && isFilled())
			win = -1;
		
		return win;
	}
	
	/**
	 * Checks whether there is a win from these 3 spaces of the given type
	 * 
	 * @param type : either X or O, denoting what type of win is being looked for
	 * @param piece1 : first piece to check
	 * @param piece2 : second piece to check
	 * @param piece3 : third piece to check	
	 * 
	 * @return true if win of given type, false otherwise
	 */
	private boolean isWin(String type, TicTacToePiece piece1, TicTacToePiece piece2, TicTacToePiece piece3)
	{
		boolean lookForX = type == "X" ? true : false; //is true if given X, false if given O
		
		return piece1.isFilled() && piece2.isFilled() && piece3.isFilled() &&
				piece1.isX() == lookForX && piece2.isX() == lookForX && piece3.isX() == lookForX;
	}
	
	/**
	 * Checkers whether the board is completely filled or not
	 * 
	 * @return true if board if filled, false otherwise
	 */
	private boolean isFilled()
	{
		boolean filled = true;
		
		for(int o = 0; o < 3; o++)
			for(int i = 0; i < 3; i++)
			{
				if(!pieces[o][i].isFilled())
					filled = false;
			}
		
		return filled;
	}
	
	/**
	 * To be called upon a game ending 
	 * Resets all of the pieces on the board
	 */
	private void resetGame()
	{
		for(TicTacToePiece[] pArray : pieces)
		{
			for(TicTacToePiece p : pArray)
			{
				p.setText("");
			}
		}
	}
	
	public boolean isXTurn()
	{
		return turn;
	}
}
