package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class End extends JDialog
{
	/**
	 * Create a dialog window displaying the winner of the game
	 * 
	 * @param winName : name of the winner of the game
	 */
	public End(String winName)
	{
		super(null,"", Dialog.ModalityType.APPLICATION_MODAL);
		
		setAlwaysOnTop(true);
		
		setSize(900,300);
		setResizable(false);
		setLocationRelativeTo(null);
		
		JLabel text = new JLabel(winName.toUpperCase() + " HAS WON THE GAME");
		text.setHorizontalAlignment(JLabel.CENTER);
		text.setFont(new Font("Comic Sans MS", Font.PLAIN, 48));
		
		getContentPane().add(text);
		getContentPane().setBackground(Color.YELLOW);
		
		show();
		
	}
}
