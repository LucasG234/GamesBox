package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import resources.ResourceLoader;

/**
 * Class representing the first frame of the program
 * Holds buttons which construct games which can be played
 *
 */
class MainPanel extends JFrame
{
	/**
	 * Calls helper methods to construct a main panel, which other games can be selected from
	 */
	public MainPanel()
	{
		super("Game Chest");
		setSize(700, 600);
		setResizable(false); //makes it so the window's size cannot be changed by any other application
		
		//get contentPane and add components to it
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		pane.add(title(), BorderLayout.NORTH);
		pane.add(buttons(), BorderLayout.CENTER);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Constructs a JPanel which holds two JFrames
	 * JFrames represent both the title and author of the project
	 * 
	 * @return the JPanel
	 */
	private JPanel title()
	{
		JPanel titlePanel = new JPanel();
		titlePanel.setPreferredSize(new Dimension(0,100));
		
		JLabel title = new JLabel("Cool ComputerScience Games");
		title.setFont(new Font("Comic Sans MS", Font.PLAIN,48));
		title.setHorizontalAlignment(JLabel.CENTER);
		titlePanel.add(title);
		
		JLabel author = new JLabel("By: Lucas Gates");
		author.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		title.setHorizontalAlignment(JLabel.CENTER);
		titlePanel.add(author);
		
		return titlePanel;
	}
	
	/**
	 * Constructs a JPanel holds JButtons
	 * JButtons represent different selectable games, and each have their own icon
	 * 
	 * @return the JPanel
	 */
	private JPanel buttons()
	{
		JPanel buttons = new JPanel();
		FlowLayout layout = (FlowLayout)buttons.getLayout();
		layout.setHgap(100);
		layout.setVgap(20);
		
		JButton checkers = new JButton("Checkers", new ImageIcon(ResourceLoader.getImage("Checkers.png")));
		setButton(checkers, GameType.Checkers);
		
		JButton connect4 = new JButton("Connect Four", new ImageIcon(ResourceLoader.getImage("ConnectFour.png")));
		setButton(connect4, GameType.Connect4);
		
		JButton dotsAndBoxes = new JButton("Dots and Boxes", new ImageIcon(ResourceLoader.getImage("DotsAndBoxes.png")));
		setButton(dotsAndBoxes, GameType.DotsAndBoxes);
		
		JButton ticTacToe = new JButton("Tic Tac Toe", new ImageIcon(ResourceLoader.getImage("TicTacToe.png")));
		setButton(ticTacToe, GameType.TicTacToe);
		
		
		buttons.add(checkers);
		buttons.add(connect4);
		buttons.add(dotsAndBoxes);
		buttons.add(ticTacToe);
				
		return buttons;
	}
	
	/**
	 * Modifies provided button 
	 * Sets its alignments and constructs an ActionListener for it
	 * 
	 * @param but : The button which is to be modified
	 * @param t : which type of game the icon represents
	 */
	private void setButton(JButton but, GameType t)
	{
		but.setVerticalTextPosition(JButton.BOTTOM);
		but.setHorizontalTextPosition(JButton.CENTER);
		but.addActionListener(new MainListener(t));
	}
}

/**
 * Listener which is used to select games from the MainPanel's buttons
 */
class MainListener implements ActionListener
{
	private GameType type;
	
	/**
	 * Constructs a MainListener
	 * Sets the type of the Listener to the type of the Button using it
	 * 
	 * @param g : The game type of button
	 */
	public MainListener(GameType g)
	{
		type = g;
	}
	
	/**
	 * Constructs a game, depending on what type it was initialized with
	 */
	public void actionPerformed(ActionEvent e)
	{
		Game g = null;
		
		switch(type)
		{
		case TicTacToe:
			g = new ticTacToe.TicTacToe();
			break;
		case Connect4:
			g = new connect4.Connect4();
			break;
		case Checkers:
			g = new checkers.Checkers();
			break;
		case DotsAndBoxes:
			g = new dotsAndBoxes.DotsAndBoxes();
			break;
		}
		
		g.show();
	}
}

/**
 * Enumerable which holds all types of games
 * Used to assign Listeners and Buttons a game
 */
enum GameType
{
	TicTacToe, Connect4, Checkers, DotsAndBoxes;
}
