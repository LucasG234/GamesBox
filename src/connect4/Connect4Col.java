package connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Button which will add pieces whenever clicked on
 */
class Connect4Col extends JButton
{
	//package level access denoting where the next piece should be
	int r;
	int c;
	
	/**
	 * Constructs a column which acts a button 
	 * Sets the r value to the bottom row, 
	 * 
	 * @param owner : The Connect4 this column belongs in
	 * @param c : The placement of this column in the array
	 */
	public Connect4Col(Connect4 owner, int c)
	{
		setOpaque(false);
		setBackground(Color.white);
		setBorderPainted(false);
		addActionListener(new Connect4Listener(owner));
		this.c = c;
		r = 5;
		
		setFocusable(false);
		setFocusPainted(false);
	}
}

class Connect4Listener implements ActionListener
{
	private Connect4 owner;
	
	public Connect4Listener(Connect4 owner)
	{
		this.owner = owner;
	}
	
	/**
	 * Is called whenever a column is clicked
	 * If the click is valid, will set the next piece, and modify the collumn's r value
	 */
	public void actionPerformed(ActionEvent e)
	{
		Connect4Col source = (Connect4Col)e.getSource();
		if(source.r >= 0)
		{
			if(owner.isRedTurn())
			{
				owner.pieces[source.r][source.c].setBackground(Color.red);
				source.r--;
			}
			else
			{
				owner.pieces[source.r][source.c].setBackground(Color.yellow);
				source.r--;
			}
			
			owner.refresh();
		}
		
	}
}
