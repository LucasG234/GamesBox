package dotsAndBoxes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class DbPiece extends JPanel
{
	private JButton button;
	private boolean isPlaced;
	private DotsAndBoxes owner;
	
	//denotes that this is the top part of an already filled square
	private boolean isFilled;
	
	/**
	 * 
	 * @param orientation : 1 = horizontal, 2 = vertical, 3 = right vertical edge
	 * @param owner : DotsAndBoxes this piece belongs to
	 */
	public DbPiece(int orientation, DotsAndBoxes owner)
	{
		isPlaced = false;
		isFilled = false;
		this.owner = owner;
		
		setLayout(new BorderLayout());
		setOpaque(false);
		
		button = new JButton();
		button.setBackground(Color.white);
		button.addActionListener(new DbListener(owner));
		
		//set all size concerns based on orientation
		switch(orientation)
		{
		case(1):
			setBorder(new EmptyBorder(0,0,145,28));
			button.setPreferredSize(new Dimension(142,25));
			break;
		case(2):
			setBorder(new EmptyBorder(0,0,32,145));
			button.setPreferredSize(new Dimension(25,136));
			break;
		case(3):
			setBorder(new EmptyBorder(0,0,32,0));
			button.setPreferredSize(new Dimension(25,136));
			break;
		}
		
		add(button);
	}
	
	/**
	 * Fills this piece based on whos turn it is
	 */
	public void place()
	{
		isPlaced = true;
		if(owner.isRedTurn())
			button.setBackground(Color.red);
		else
			button.setBackground(Color.blue);
	}
	
	public boolean isPlaced()
	{
		return isPlaced;
	}
	
	public void fill()
	{
		isFilled = true;
	}
	
	public boolean isFilled()
	{
		return isFilled;
	}
	
	public void unPlace()
	{
		isFilled = false;
		isPlaced = false;
		button.setBackground(Color.white);
	}
}

class DbListener implements ActionListener
{
	private DotsAndBoxes owner;
	
	public DbListener(DotsAndBoxes owner)
	{
		this.owner = owner;
	}
	
	/**
	 * Called whenever a piece is clicked
	 * Checks whether the source is empty, and if it is fills it and ends turn
	 */
	public void actionPerformed(ActionEvent e)
	{
		//extract just the piece from the ActionEvent
		DbPiece source = (DbPiece)((JButton)e.getSource()).getParent();
		
		if(!source.isPlaced())
		{
			source.place();
			owner.refresh();
		}
	}
}