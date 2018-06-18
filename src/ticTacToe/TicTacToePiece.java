package ticTacToe;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class TicTacToePiece extends JButton
{
	/**
	 * Constructs the piece and adds an ActionListener
	 * 
	 * @param owner : The TicTacToe object which contains the piece
	 */
	public TicTacToePiece(TicTacToe owner)
	{
		setOpaque(false);
		addActionListener(new TicTacToeListener(owner));
		setBackground(Color.white);
		setBorderPainted(false);
		setFont(new Font("Serif",Font.BOLD, 100));
		setFocusPainted(false);
	}
	
	public boolean isFilled()
	{
		return getText() != "";
	}
	
	public boolean isX()
	{
		return getText() == "X";
	}
}

class TicTacToeListener implements ActionListener
{
	private TicTacToe owner;
	
	public TicTacToeListener(TicTacToe owner)
	{
		this.owner = owner;
	}
	
	/**
	 * Called whenever a piece is clicked
	 * Will check if a valid move, then change the piece's value and end the turn
	 */
	public void actionPerformed(ActionEvent e)
	{
		TicTacToePiece source = (TicTacToePiece)e.getSource();
		
		if(!source.isFilled())
		{
			if(owner.isXTurn())
				source.setText("X");
			else
				source.setText("O");
			
			owner.refresh();
		}
	}
}
