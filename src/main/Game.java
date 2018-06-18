package main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * Abstract class which acts as a basis for all games
 * 
 */
public abstract class Game extends JDialog
{
	protected JPanel centerPane; //the size of the center pane will be 1000 x 400
	protected JPanel winText;
	protected JPanel turnText;
	
	//keep track of who is winning and who's turn it is
	protected boolean turn = true;
	protected int oneWins = 0;
	protected int twoWins = 0;
	
	/**
	 * Construct a JFrame which will contain the game
	 * 
	 * @param title : title of the JFrame
	 */
	public Game(String title)
	{
		super(null, title, Dialog.ModalityType.APPLICATION_MODAL);
		setSize(1400, 800);
		setResizable(false);
		
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		
		centerPane = new JPanel(); 
		centerPane.setLayout(new BorderLayout());
		
		pane.add(centerPane, BorderLayout.CENTER);
		pane.add(rightPane(), BorderLayout.EAST);
		
		setLocationRelativeTo(null);
	}
	
	private JPanel rightPane()
	{
		JPanel rightPane = new JPanel();
		rightPane.setPreferredSize(new Dimension(400,0));
		rightPane.setBorder(new EmptyBorder(100,0,0,0));
		rightPane.setLayout(new BoxLayout(rightPane, BoxLayout.Y_AXIS));
		
		winText = new JPanel();
		winText.setBorder(new EmptyBorder(0,0,100,0));
		winText.setLayout(new BoxLayout(winText, BoxLayout.Y_AXIS));
		winText.setAlignmentX(CENTER_ALIGNMENT);
		
		turnText = new JPanel();
		turnText.setLayout(new BoxLayout(turnText, BoxLayout.Y_AXIS));
		turnText.setAlignmentX(CENTER_ALIGNMENT);
		
		rightPane.add(winText);
		rightPane.add(turnText);
		
		return rightPane;
	}
}
