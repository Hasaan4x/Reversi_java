package reversi;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Same as the SimpleModel but adds a simple GUI with some buttons to test features
 * Change your model to this if you want a simple test interface to appear which will allow you to test various features.
 * The only difference between this and Simplemodel is the extra UI that appears.
 */
public class SimpleTestModel extends SimpleModel
{
	Random rand = new Random();
	
	public SimpleTestModel()
	{
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		testFrame.setTitle("TEST FACILITY - this window was created by SimpleTestModel. Change to SimpleModel to make it disappear.");
		
		testFrame.getContentPane().setLayout(new GridLayout(11,1));
		// Don't try to work out the following line until we have covered Lambda Expressions!
		JButton button1 = new JButton("Almost Fill Model, so no move can be made, clear finished flag and call update - which should set the flag again");
		button1.addActionListener( e->{ clear(1); setBoardContents( 0, 0, 0 ); setBoardContents( 1, 1, 0 ); setFinished(false); view.refreshView(); controller.update(); if ( !finished ) System.err.println("Error: update did not set finished flag when there is no move possible."); } );
		testFrame.getContentPane().add(button1);
		
		JButton button2 = new JButton("Almost fill, with ability to play, clear finished flag and call update");
		button2.addActionListener( e->{ clear(1); setBoardContents( 0, 0, 0 ); setBoardContents( 1, 1, 2 ); setFinished(false); view.refreshView(); controller.update(); if ( finished ) System.err.println("Error: this is a move possible so finished flag should not be set.");} );
		testFrame.getContentPane().add(button2);
		
		JButton button3 = new JButton("Clear totally, clear finished flag and call update, which should set finished flag again");
		button3.addActionListener( e->{ clear(0); setFinished(false); view.refreshView(); controller.update(); if ( !finished ) System.err.println("Error: update did not set finished flag when there is no move possible.");} ); 
		testFrame.getContentPane().add(button3);
		
		JButton button4 = new JButton("Fill black, so no move is possible, clear finished flag and call update");
		button4.addActionListener( e->{ clear(2); setFinished(false); view.refreshView(); controller.update(); if ( !finished ) System.err.println("Error: update did not set finished flag when there is no move possible.");} ); 
		testFrame.getContentPane().add(button4);
		
		JButton button5 = new JButton("Random fill white or black, so no move possible, clear finished flag and call update");
		button5.addActionListener( e->{ for ( int x = 0 ; x < getBoardWidth(); x++ ) for ( int y = 0 ; y < getBoardHeight(); y++ ) setBoardContents(  x, y, 1+rand.nextInt(2) ); setFinished(false); view.refreshView(); controller.update(); if ( !finished ) System.err.println("Error: update did not set finished flag when there is no move possible.");} );
		testFrame.getContentPane().add(button5);
		
		JButton button6 = new JButton("Totally random, empty/white/black, clear finished flag and call update - you can determine whether a move is possible");
		button6.addActionListener( e->{ for ( int x = 0 ; x < getBoardWidth(); x++ ) for ( int y = 0 ; y < getBoardHeight(); y++ ) setBoardContents(  x, y, rand.nextInt(3) ); setFinished(false); view.refreshView(); controller.update(); } );
		testFrame.getContentPane().add(button6);
		
		JButton button7 = new JButton("Test whether it will switch back to player 2 and/or end the game. Set player 1 and call update, clear finished flag and call update.");
		button7.addActionListener( e->{ setPlayer(1); setFinished(false); view.refreshView(); controller.update(); } );
		testFrame.getContentPane().add(button7);

		JButton button8 = new JButton("Test whether it will switch back to player 1 and/or end the game. Set player 2 and call update, clear finished flag and call update");
		button8.addActionListener( e->{ setPlayer(2); setFinished(false); view.refreshView(); controller.update(); } );
		testFrame.getContentPane().add(button8);

		JButton button9 = new JButton("Set finished flag and call update - ideally update clears or sets the finished flag appropriately");
		button9.addActionListener( e->{ setFinished(true); view.refreshView(); controller.update(); } );
		testFrame.getContentPane().add(button9);

		JButton button10 = new JButton("Set not finished and call update (to see whether it gets set again) - ideally update clears or sets the finished flag appropriately");
		button10.addActionListener( e->{ setFinished(false); view.refreshView(); controller.update(); } );
		testFrame.getContentPane().add(button10);
		
		JButton button11 = new JButton("Play AI to the end : keep playing alternating AI moves until the finished flag is set by your update. May loop forever if you don't set the flag!");
		button11.addActionListener( e->{ while( !hasFinished() ) { controller.doAutomatedMove(1); controller.doAutomatedMove(2); view.refreshView(); controller.update(); } } );
		testFrame.getContentPane().add(button11);
		
		testFrame.pack();
		testFrame.setVisible(true);
	}
}
