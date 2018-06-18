package checkers;

import javax.swing.*;

import resources.ResourceLoader;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class CheckersPiece extends JButton
{
	//pieces which should be unplaced if a piece is placed here (in this object)
	private ArrayList<CheckersPiece> toDelete;
	
	//All Images are stored as constants so they can be compared against
	private final static ImageIcon black = new ImageIcon(ResourceLoader.getImage("CheckersBlackPiece.png"));
	private final static ImageIcon white = new ImageIcon(ResourceLoader.getImage("CheckersWhitePiece.png"));
	private final static ImageIcon blackKing = new ImageIcon(ResourceLoader.getImage("CheckersBlackKing.png"));
	private final static ImageIcon whiteKing = new ImageIcon(ResourceLoader.getImage("CheckersWhiteKing.png"));
	
	private boolean potentialIsKing;
	
	private int row; 
	private int col;
	
	/**
	 * Constructs a blank piece space which can later have checker piece added to it as images
	 * 
	 * @param owner : The Checkers this piece is inside of
	 * @param row : row location of this piece
	 * @param col : column location of this piece
	 */
	public CheckersPiece(Checkers owner, int row, int col)
	{
		this.row = row;
		this.col = col;
		toDelete = new ArrayList<>();
		potentialIsKing = false;
		
		setBackground(Color.white);
		setOpaque(false);
		setBorderPainted(false); 
		setFocusPainted(false); //don't draw border around ImageIcon
		//image in center
		setVerticalAlignment(CENTER);
		setHorizontalAlignment(CENTER);
		
		addActionListener(new CheckersListener(owner));
	}
	
	/**
	 * Set image of this space to whatever piece should exist there
	 * 
	 * @param isWhite : whether the piece placed here should be white
	 */
	public void placePiece(boolean isWhite)
	{
		if(potentialIsKing)
		{
			if(isWhite)
				setIcon(whiteKing);
			else
				setIcon(blackKing);
		}
		else
		{
			if(isWhite)
				setIcon(white);
			else
				setIcon(black);
		}
		
	}
	public void removePiece()
	{
		setIcon(null);
	}
	/**
	 * Set this space to be displayed as a potential move
	 * Sets all data needed for when a piece is moved here
	 * 
	 * @param isKing : whether pieces which as placed here should be kings
	 * @param toDelete : all pieces which should be deleted if a piece is placed here
	 */
	public void setPotential(boolean isKing, Object toDelete)
	{
		//take in paremeter as object so can use cloned ArrayLists
		setBackground(Color.blue);
		setOpaque(true);
		this.toDelete = (ArrayList<CheckersPiece>)toDelete;
		potentialIsKing = isKing;
	}
	
	/**
	 * Make this piece no longer marked as a potentential move, without putting anything into it
	 */
	public void clearPotential()
	{
		if(getBackground() == Color.blue)
		{
			setBackground(Color.white);
			setOpaque(false);
			toDelete.clear();
			potentialIsKing = false;
		}
	}
	
	/**
	 * Change this piece into a king of its corresponding color
	 */
	public void setKing()
	{
		if(getIcon() == black)
			setIcon(blackKing);
		else if(getIcon() ==  white)
			setIcon(whiteKing);
	}
	
	public boolean isPotential()
	{
		return getBackground() == Color.blue;
	}
	
	public boolean hasPiece()
	{
		return getIcon() != null;
	}
	
	public boolean isWhite()
	{
		return getIcon() == white || getIcon() == whiteKing;
	}
	
	public boolean isKing()
	{
		if(isPotential())
			return potentialIsKing;
		else
			return getIcon() == blackKing || getIcon() == whiteKing;
	}
	
	
	public int getRow()
	{
		return row;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public ArrayList<CheckersPiece> getToDelete()
	{
		return toDelete;
	}
}


class CheckersListener implements ActionListener
{
	private Checkers owner;
	
	public CheckersListener(Checkers owner)
	{
		this.owner = owner;
	}
	
	/**
	 * Called every time a piece is clicked
	 * If called with a valid move on a piece, sets the potential moves it can make
	 * If called on a potential move, moves the piece there
	 */
	public void actionPerformed(ActionEvent e)
	{
		CheckersPiece source = (CheckersPiece)e.getSource();
		
		
		//if clicking a piece on their own turn
		if(source.hasPiece() && source.isWhite() == owner.isWhiteTurn())
		{
			owner.setPotentials(source);
		}
		
		//if clicking on a potential move
		if(source.isPotential())
			owner.movePiece(source);
	}
}