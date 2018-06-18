package checkers;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import main.*;
import resources.ResourceLoader;

import java.util.*;

public class Checkers extends Game
{
	//Player one is white, player two is black
	//inherits turn (true when player one's turn), oneWins, twoWins,
	//centerPanel (to place game), turnText and winText
	
	private CheckersPiece[][] pieces;
	
	private JLabel winDisplay;
	private JLabel turnDisplay;
	
	private JPanel holdsButtons;
	
	public Checkers()
	{
		super("Checkers");
		
		pieces = new CheckersPiece[8][8];
		
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
		
		board.setBorder(new EmptyBorder(85,200,85,200)); //makes board fit exactly
		
		OverlayLayout boardManager = new OverlayLayout(board);
		board.setLayout(boardManager);

		//add buttons first to ensure they are on top of the board
		board.add(makeButtons());
			
		JLabel boardImage = new JLabel(new ImageIcon(ResourceLoader.getImage("CheckersBoard.png")));
		board.add(boardImage);
		boardImage.setAlignmentX(CENTER_ALIGNMENT);
		boardImage.setAlignmentY(CENTER_ALIGNMENT);
		
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
		holdsButtons = new JPanel();
		holdsButtons.setLayout(new GridLayout(8,8));
		holdsButtons.setAlignmentX(CENTER_ALIGNMENT);
		holdsButtons.setAlignmentY(CENTER_ALIGNMENT);
		holdsButtons.setOpaque(false);
		
		for(int o = 0; o < 8; o++)
		{
			for(int i = 0; i < 8; i++)
			{
				CheckersPiece button = new CheckersPiece(this, o ,i);
				pieces[o][i] = button;
				holdsButtons.add(button);
			}
		}
		
		//place the starting set of pieces into empty piece objects
		for(int o = 0; o < 8; o++)
		{
			//alternate the rows
			int i = o%2==0 ? 1 : 0;
			
			if(o > 4) //add white pieces
			{
				for(; i < 8; i+=2)
					pieces[o][i].placePiece(true);
			}
			
			if(o < 3) // add black pieces
			{
				for(; i < 8; i+=2)
					pieces[o][i].placePiece(false);
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
		
		JLabel winTwo = new JLabel("W          B");
		winTwo.setFont(new Font("",Font.PLAIN, 64));
		winTwo.setBorder(new EmptyBorder(0,0,10,0));
		
		winDisplay = new JLabel(oneWins + "                         " + twoWins);
		winDisplay.setFont(new Font("",Font.PLAIN, 32));
		
		winText.add(winOne);
		winText.add(winTwo);
		winText.add(winDisplay);
		
		turnDisplay = new JLabel("It is W's turn"); //X will always take first turn on init
		turnDisplay.setFont(new Font("",Font.PLAIN,64));
		
		turnText.add(turnDisplay);
	}
	
	/**
	 * To be called whenever a piece is moved, ending a turn
	 * Switches who's turn it is, and handles all end game cases
	 */
	private void endTurn()
	{
		turn = !turn;
		
		if(turn)
			turnDisplay.setText("It is W's turn");
		else
			turnDisplay.setText("It is B's turn");
		
		setKings();
		
		int win = checkEnd();
		
		switch(win)
		{
		case 1:
			{
				new End("White");
				oneWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); //just keeps it centered up to 99 wins
				break;
			}
		case 2:
			{
				new End("Black");
				twoWins++;
				if(oneWins < 10)
					winDisplay.setText(oneWins + "                         " + twoWins);
				else
					winDisplay.setText(oneWins + "                        " + twoWins); 
				break;
			}
		}
		
		if(win != 0)
			resetGame();
	}
	
	/**
	 * Resets the board by clearing it and then replacing all buttons
	 * Sets the player to white, who should always go first
	 */
	private void resetGame()
	{
		holdsButtons.removeAll();
		//simply reset all of my buttons
		for(int o = 0; o < 8; o++)
		{
			for(int i = 0; i < 8; i++)
			{
				CheckersPiece button = new CheckersPiece(this, o ,i);
				pieces[o][i] = button;
				holdsButtons.add(button);
			}
		}
		
		for(int o = 0; o < 8; o++)
		{
			//alternate the rows
			int i = o%2==0 ? 1 : 0;
			
			if(o > 4) //add white pieces
			{
				for(; i < 8; i+=2)
					pieces[o][i].placePiece(true);
			}
			
			if(o < 3) // add black pieces
			{
				for(; i < 8; i+=2)
					pieces[o][i].placePiece(false);
			}
		}
		
		turn = true;
		turnDisplay.setText("It is W's turn");
	}
	
	/**
	 * Checks whether the game has ended, and if so who has won
	 * Winning conditions: clearing all opponents pieces OR opponent has no moves on their own turn
	 * 
	 * @return 0 = no win, 1 = white win, 2 = black win
	 */
	private int checkEnd() 
	{
		

		boolean hasWhite = false;
		boolean hasBlack = false;
		
		//check if the board does not contain any of a type
		for(CheckersPiece[] row : pieces)
		{
			for(CheckersPiece piece : row)
			{
				if(piece.hasPiece() && piece.isWhite())
					hasWhite = true;
				else if(piece.hasPiece())
					hasBlack = true;
			}
		}
		
		if(!hasWhite)
			return 2;
		else if(!hasBlack)
			return 1;
		
		boolean whiteCanMove = false;
		boolean blackCanMove = false;
		
		for(CheckersPiece[] row : pieces)
		{
			for(CheckersPiece piece : row)
			{
				if(piece.hasPiece() && piece.isWhite()) 
				{
					//if has at least one move
					if(getPotentials(piece).size() != 0 || isPotentialJump(piece, true, false) || isPotentialJump(piece, false, false))
						whiteCanMove = true;
					
				}
				
				if(piece.hasPiece() && !piece.isWhite()) 
				{
					//if has at least one move
					if(getPotentials(piece).size() != 0 || isPotentialJump(piece, true, false) || isPotentialJump(piece, false, false))
						blackCanMove = true;
					if(piece.isKing() && (isPotentialJump(piece, true, true) || isPotentialJump(piece, false, true)))
						blackCanMove = true;
				}
				
				
			}
		}
		
		if(whiteCanMove && blackCanMove)
			return 0;
		else //if one can't move
			return whiteCanMove ? 1 : 2; //if white can move, black can't, and vice versa
	}
	
	/**
	 * To be called on every move
	 * Checks if there are any pieces that must be kinged, and kings them
	 */
	private void setKings()
	{
		for(int i = 0; i < 8; i++)
		{
			if(pieces[0][i].hasPiece() && pieces[0][i].isWhite())
				pieces[0][i].setKing();
			if(pieces[7][i].hasPiece() && !pieces[7][i].isWhite())
				pieces[7][i].setKing();
		}
	}
	
	/**
	 * Sets all potential moves this piece can make and its toDelete if these moves are made
	 * Calls another method to setJumps
	 * 
	 * @param source : Piece to find potential moves from
	 */
	public void setPotentials(CheckersPiece source)
	{
		clearPotentials(); //first clear other, older potential moves
		
		boolean isKing = source.isKing();
		
		ArrayList<CheckersPiece> toDelete = new ArrayList<>();
		toDelete.add(source);
		
		for(CheckersPiece piece : getPotentials(source))
		{
			piece.setPotential(isKing, toDelete);
		}
			
		
		setPotentialJumps(source, false);
		if(source.isKing())
		{
			setPotentialJumps(source,true);
		}
			
	}
	
	/**
	 * Return all the potential moves this piece can make
	 * 
	 * @param source : Piece to find potential moves from
	 * 
	 * @return A list of all the potential moves
	 */
	private ArrayList<CheckersPiece> getPotentials(CheckersPiece source)
	{
		ArrayList<CheckersPiece> potentials = new ArrayList<>();
		
		int row;
		
		if(source.isWhite())
			row = source.getRow() - 1;
		else
			row = source.getRow() + 1;
		
		int col = source.getCol();
		
		//moves that can be made without jumping
		if(inBounds(row, col-1) && !hasPiece(row, col-1))
		{
			potentials.add(pieces[row][col-1]);
		}
			
		if(inBounds(row, col+1) && !hasPiece(row,col+1))
		{
			potentials.add(pieces[row][col+1]);
		}
		
		if(source.isKing())
		{
			//if piece is a king, check backwards potential moves too
			if(row < source.getRow())
				row = source.getRow() +1;
			else
				row = source.getRow() -1;
			
			if(inBounds(row, col-1) && !hasPiece(row, col-1))
			{
				potentials.add(pieces[row][col-1]);
			}
				
			if(inBounds(row, col+1) && !hasPiece(row,col+1))
			{
				potentials.add(pieces[row][col+1]);
			}
		}
		
		return potentials;
	}
	
	/**
	 * Return all the potential jumps this piece can make
	 * 
	 * @param source : Piece to find potential jumps from
	 * @param reverse : whether the row should be reversed, to be used with king
	 */
	private void setPotentialJumps(CheckersPiece source, boolean reverse)
	{
		//pieces are only allowed to double jump
		//kings can only double jump in one direction
		ArrayList<CheckersPiece> toDelete = new ArrayList<>();
		toDelete.add(source);
		
		//call reverse on kings, allowing to check backwards with same methods
		
		boolean isKing = source.isKing();
		
		int row = source.getRow();
		int col = source.getCol();
		
		int upOrDown = turn ? -1 : 1;
		
		upOrDown *= reverse ? -1 : 1; //if reverse, flip upOrDown
		
		//must clone every call so that the clears don't interfere
		
		//check left jump
		if(isPotentialJump(source,true, reverse))
		{
			toDelete.add(pieces[row+upOrDown][col-1]);
			pieces[row + upOrDown*2][col-2].setPotential(isKing, toDelete.clone());
			
			//check doubles
			if(isPotentialJump(pieces[row+upOrDown*2][col-2],true, reverse))
			{
				toDelete.add(pieces[row+upOrDown*3][col-3]);
				pieces[row+upOrDown*4][col-4].setPotential(isKing, toDelete.clone());
				
				toDelete.subList(2,toDelete.size()).clear();
			}
			
			if(isPotentialJump(pieces[row+upOrDown*2][col-2],false, reverse))
			{
				toDelete.add(pieces[row+upOrDown*3][col-1]);
				pieces[row+upOrDown*4][col].setPotential(isKing, toDelete.clone());
			}
			
			toDelete.subList(1,toDelete.size()).clear();
		}
		
		
		//check right
		if(isPotentialJump(source,false, reverse))
		{
			toDelete.add(pieces[row+upOrDown][col+1]);
			pieces[row + upOrDown*2][col+2].setPotential(isKing, toDelete.clone());
			
			//check doubles
			if(isPotentialJump(pieces[row+upOrDown*2][col+2],false, reverse))
			{
				toDelete.add(pieces[row+upOrDown*3][col+3]);
				pieces[row+upOrDown*4][col+4].setPotential(isKing, toDelete.clone());
				
				toDelete.subList(2,toDelete.size()).clear();
			}
			
			if(isPotentialJump(pieces[row+upOrDown*2][col+2],true, reverse))
			{
				toDelete.add(pieces[row+upOrDown*3][col+1]);
				pieces[row+upOrDown*4][col].setPotential(isKing, toDelete.clone());
			}
		}
	}
	
	/**
	 * Check if this direction is a potential jump for the source
	 * 
	 * @param source : Which piece to check
	 * @param isToLeft : Whether to check left or right
	 * @param reverse : Whether to use normal row movement, or reversed
	 * 
	 * @return Whether jump is valid
	 */
	private boolean isPotentialJump(CheckersPiece source, boolean isToLeft, boolean reverse)
	{
		
		int upOrDown = turn ? -1 : 1;
		int leftOrRight = isToLeft ? -1 : 1;
		
		upOrDown *= reverse ? -1 : 1; //if reverse, mutliply upOrDown by -1
		
		int row = source.getRow();
		int col = source.getCol();
		
		//if spaces in front of it are occupied, get valid places starting there
		return (inBounds(row + upOrDown, col + leftOrRight) && hasPiece(row + upOrDown, col + leftOrRight) && pieces[row + upOrDown][col + leftOrRight].isWhite() != turn)
				&& (inBounds(row + upOrDown * 2, col + leftOrRight * 2) && !hasPiece(row + upOrDown * 2, col + leftOrRight * 2));
	}
	
	/**
	 * Clear all potential moves, without placing any
	 */
	private void clearPotentials()
	{
		for(CheckersPiece[] row : pieces)
		{
			for(CheckersPiece piece : row)
			{
				piece.clearPotential();
			}
		}
	}
	
	/**
	 * Place a piece on source, and delete all pieces which should be deleted
	 * 
	 * @param source : Where a piece is being moved to
	 */
	public void movePiece(CheckersPiece source)
	{
		source.placePiece(turn);
		
		for(CheckersPiece piece : source.getToDelete())
			piece.removePiece();
		
		endTurn();
		
		clearPotentials();
	}
	
	private boolean inBounds(int r, int c)
	{
		return r < 8 && r > -1 && c < 8 && c > -1;
	}
	
	
	public boolean isWhiteTurn()
	{
		return turn;
	}
	public boolean hasPiece(int r, int c)
	{
		return(pieces[r][c].hasPiece());
	}
}
