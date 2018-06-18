package dotsAndBoxes;

import main.*;
import resources.ResourceLoader;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class DotsAndBoxes extends Game
{
	//Player one is red, player two is yellow
	//inherits turn, oneWins, twoWins, centerPanel, turnText, winText
	private DbPiece[][] pieces;
	//rows go: horizontal, vertical, horizontal, etc
	
	private JLabel[][] boxes;
	
	private JLabel winDisplay;
	private JLabel turnDisplay;
	
	public DotsAndBoxes()
	{
		super("Dots and Boxes");
		
		pieces = new DbPiece[7][4];
		boxes = new JLabel[3][3];
		
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
		
		JLabel boardImage = new JLabel(new ImageIcon(ResourceLoader.getImage("DotsAndBoxesBoard.png")));
		board.add(boardImage);
		boardImage.setAlignmentX(CENTER_ALIGNMENT);
		boardImage.setAlignmentY(CENTER_ALIGNMENT);
		
		board.add(makeBoxes());
		board.add(makeButtons());
		
		return board;
	}
	
	/**
	 * Creates a JPanel which holds empty JLabels to act as boxes
	 * These boxes will be filled whenever the pieces around them are filled
	 * 
	 * @return The JPanel holding the boxes
	 */
	private JPanel makeBoxes()
	{
		JPanel holdsBoxes = new JPanel();
		
		holdsBoxes.setLayout(new FlowLayout(FlowLayout.LEADING));
		holdsBoxes.setBorder(new EmptyBorder(40,45,0,0));
		holdsBoxes.setOpaque(false);
		
		for(int o = 0; o < 3; o++)
		{
			for(int i = 0; i < 3; i++)
			{
				//Give the box an empty border to properly fit them in layout
				JPanel boxWithBorder = new JPanel();
				
				JLabel box = new JLabel();
				box.setBackground(Color.white);
				box.setOpaque(true);
				box.setPreferredSize(new Dimension(142,138));
				boxes[o][i] = box;
				
				boxWithBorder.setLayout(new BorderLayout());
				boxWithBorder.add(box);
				boxWithBorder.setBorder(new EmptyBorder(0,0,31,27));
				boxWithBorder.setOpaque(false);
				
				holdsBoxes.add(boxWithBorder);
			}
		}
		
		return holdsBoxes;
	}
	
	/**
	 * Creates a JPanel which holds all of the pieces
	 * Actually filled with other, smaller JPanels which hold selections of the pieces
	 * 
	 * @return A JPanel holding JPanels of the pieces
	 */
	private JPanel makeButtons()
	{
		JPanel holdsButtons = new JPanel();
		//use a OverlayLayout to stack all of the piece JPanels on top of each other
		holdsButtons.setLayout(new OverlayLayout(holdsButtons));
		holdsButtons.setBorder(new EmptyBorder(10,16,5,0)); //fit exactly around board
		
		holdsButtons.setBackground(Color.white);
		holdsButtons.setOpaque(true);
		
		holdsButtons.add(makeVerticalButtons());
		holdsButtons.add(makeHorizontalButtons());
		holdsButtons.add(makeVerticalEdge());
		
		return holdsButtons;
	}
	
	/**
	 * Construct a JPanel holding all horizontal pieces
	 * 
	 * @return The JPanel
	 */
	private JPanel makeHorizontalButtons()
	{
		JPanel horizontalButtons = new JPanel();
		
		horizontalButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
		horizontalButtons.setBorder(new EmptyBorder(0,28,0,0));
		horizontalButtons.setOpaque(false);
		
		for(int o = 0; o < 7; o += 2)
		{
			for(int i = 0; i < 3; i++)
			{
				DbPiece tempPiece = new DbPiece(1,this);
				horizontalButtons.add(tempPiece);
				pieces[o][i] = tempPiece;
			}
		}
		
		return horizontalButtons;
	}
	
	/**
	 * Construct a JPanel holding the first three columns of vertical buttons
	 * 
	 * @return The JPanel
	 */
	private JPanel makeVerticalButtons()
	{
		JPanel verticalButtons = new JPanel();
		
		verticalButtons.setLayout(new FlowLayout(FlowLayout.LEADING));
		verticalButtons.setBorder(new EmptyBorder(30,0,0,0));
		verticalButtons.setOpaque(false);
		
		//add the first 3 columns of vertical buttons
		for(int o = 1; o < 6; o += 2)
		{
			for(int i = 0; i < 3; i++)
			{
				DbPiece tempPiece = new DbPiece(2,this);
				verticalButtons.add(tempPiece);
				pieces[o][i] = tempPiece;
			}
		}
		
		return verticalButtons;
	}
	
	/**
	 * Construct a JPanel holding the last column of vertical buttons
	 * 
	 * @return The JPanel
	 */
	private JPanel makeVerticalEdge()
	{
		//need different case for right edge vertical because of margin concerns
		JPanel verticalEdge = new JPanel();
		
		verticalEdge.setLayout(new FlowLayout(FlowLayout.LEADING));
		verticalEdge.setBorder(new EmptyBorder(30,522,0,0));
		verticalEdge.setOpaque(false);
		
		for(int i = 1; i < 6; i += 2)
		{
			DbPiece tempPiece = new DbPiece(3,this);
			verticalEdge.add(tempPiece);
			pieces[i][3] = tempPiece;
		}
		
		return verticalEdge;
	}
	
	/**
	 * Sets up all swing components and configurations for the win and turn display
	 */
	private void setRightPane()
	{
		JLabel winOne = new JLabel("WINS");
		winOne.setFont(new Font("",Font.PLAIN, 100));
		winOne.setBorder(new EmptyBorder(0,0,20,0));
		
		JLabel winTwo = new JLabel("R          B");
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
	 * To be called upon a valid click being made
	 * Switches who's turn it is if applicable, and handles all end game cases
	 * If any boxes are filled during turn, turn does not switch
	 */
	public void refresh()
	{
		//filled() method both fills any boxes and returns whether it had to fill a box
		if(!filled()) 
		{
			//turn switching only occurs if no boxes were filled
			turn = !turn; 
			
			if(turn)
				turnDisplay.setText("It is R's turn");
			else
				turnDisplay.setText("It is B's turn");
		}
		
		int win = checkEnd();
		
		switch(win)
		{
		case 1:
			{
				new End("RED");
				oneWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); //just keeps it centered up to 99 wins
				break;
			}
		case 2:
			{
				new End("BLUE");
				twoWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); 
				break;
			}
		}
		
		if(win != 0)
		{
			resetBoard();
		}
	}
	
	/**
	 * Checks whether the game has ended, and if so who has won
	 * 
	 * @return 0 = no win, 1 = X win, 2 = O win (Ties are not possible)
	 */
	private int checkEnd()
	{
		int win = 0;
		
		//count up how many boxes each player has
		int redCounter = 0;
		int blueCounter = 0;
		
		for(JLabel[] boxRow : boxes)
		{
			for(JLabel box : boxRow)
			{
				if(box.getBackground() == Color.red)
					redCounter++;
				else if(box.getBackground() == Color.blue)
				blueCounter++;
			}
		}
		
		//if all boxes are filled, choose winner as whoever has the most
		if(redCounter + blueCounter == 9) 
		{
			if(redCounter > blueCounter)
				win = 1;
			else
				win = 2;
		}
		
		
		return win;
	}
	
	/**
	 * Fills all boxes which are surrounded by filled pieces, based on who's turn it is
	 * 
	 * @return Whether any boxes have been filled this turn
	 */
	private boolean filled()
	{
		boolean hasBeenFilled = false;
		
		//array goes through all UPPER HORIZONTAL buttons
		for(int o = 0; o < 5; o+= 2)
		{
			for(int i = 0; i < 3; i++)
			{
				if(!pieces[o][i].isFilled() && pieces[o][i].isPlaced() && pieces[o+1][i].isPlaced() && pieces[o+1][i+1].isPlaced() && pieces[o+2][i].isPlaced())
				{
					pieces[o][i].fill();
					hasBeenFilled = true;
					if(turn)
						boxes[o/2][i].setBackground(Color.red);
					else
						boxes[o/2][i].setBackground(Color.blue);
				}
			}
		}
		
		return hasBeenFilled;
	}
	
	/**
	 * Reset all rows and boxes to beginning game values
	 * Set turn to red, who always goes first
	 */
	private void resetBoard()
	{
		for(DbPiece[] row : pieces)
		{
			for(DbPiece piece : row)
			{
				if(piece instanceof DbPiece) //avoid nulls
				{
					piece.unPlace();
				}
			}
		}
		
		for(JLabel[] row : boxes)
		{
			for(JLabel box : row)
			{
				box.setBackground(Color.white);
			}
		}
		
		turn = true;
		turnDisplay.setText("It is R's turn");
	}
	
	public boolean isRedTurn()
	{
		return turn;
	}
}
