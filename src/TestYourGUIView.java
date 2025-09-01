package reversi;


/**
 * Test program to test the GUI view.
 * You are going to do almost all of the work to check this yourself, and I give you a list of steps to check below.
 * This basically provides you with a dummy version of some methods, so you can see what is happening.
 * This is a very cut-down version of my test class, which will do a lot more checks, and do a lot of automatic checks.
 * 
 * When you are ready to test your view, find the line below that says TODO, in main(),
 * and change which of the following lines is commented out
 * Then run this class.
 * (I had to do this otherwise it would not compile until you create the GUIView)
 * 
 * In general the GUI is easier to test as there are not many things it does, however using this class will catch if you added any special code which relied upon your view and controller working together.
 * If this program cannot run your code, or your program crashes when you run this (e.g. class cast exception) then you tried to do something unusual - so please fix before submission.
 * 
 * Run up the program with this file as the main class.
 * 
 * Manual things to check before you start pressing anything:
 * Ensure that two frames appear.
 * Check that each frame title is correct.
 * Ensure that one is for player white and one for player black.
 * When you have played a few pieces, ensure that they are rotations of each other - i.e. top left for white is bottom right for black.
 * Ensure that pieces are oval with a different coloured outline on the outside.
 * Note: this program won't ensure you didn't use images from the file system, but my test harness will so please ensure that you do not access the file system even for standard images.
 * Ensure that there is a feedback label at the top. The steps below tell you what this will show so you can test it works.
 * Ensure that there are two buttons at the bottom, one above the other.
 * The top button should say Greedy AI (play black [or white])
 * The bottom button should say Restart
 *  
 * Now you can do more steps:
 * Manually use your GUI and check that the feedback messages come back to tell you that the features worked.
 * 1) Press greedy AI move for black and ensure that it puts between 6 and 10 pieces into the next blank squares. Starting 0,0.
 * You should get a feedback message to that player saying where the last piece was placed (i.e. check that your feedback messages appear)
 * The pieces should appear on the screen (i.e. check that your refreshView correctly showed the ovals on the buttons).
 * Repeat this a few times to ensure that they show.
 * 
 * 2) Now repeat this for player white. The pieces should carry on from where the others left off.
 * If you get to the end it should clear the board and start again.
 * Check that pieces appear and the feedback message appears saying where the move was played.
 * 
 * Ensure that if the AI player is used to fill the board:
 * You can still put pieces in manually.
 * Pressing the AI player again will clear the board. 
 * This is a test to ensure that your user interface is not checking for 'finished' - the controller should be the one implementing any logic.
 * i.e. even when the board is full, both the clicking in the board and the button to make an AI move still work.
 * 
 * 3) Ensure that you have a few pieces on the board, and press the Restart button on player black's frame.
 * Ensure that all pieces vanish from the board.
 * Ensure that the feedback message now says startup() was called successfully.
 * 
 * 4) Manually click a square as black.
 * Check that a black piece appears in that square.
 * Check that the feedback message says "You last played in " and the coordinates of the square which was clicked.
 * 
 * 5) Repeat this for player white.
 * Again check that the piece appears and that the feedback message is correct.
 * 
 * 6) Ensure that you have a few pieces on the board, and press the Restart button on player white's frame.
 * Ensure that all pieces vanish from the board.
 * Ensure that the feedback message now says startup() was called successfully.
 * 
 * If all of these tests were passed successfully then your GUIView should get all of the marks.
 * For testing, this is what how I intend to test it - by displaying it and checking these things.
 */
public class TestYourGUIView extends SimpleModel implements IController 
{
	public static void main(String[] args)
	{
		TestYourGUIView tester = new TestYourGUIView();
		
		//// Choose ONE of the models
		//model = new SimpleModel();
		IModel model = tester;

		// TODO - Choose one of the following only:
		IView view = new FakeTextView(); // Comment this out
		//IView view = new GUIView(); // Uncomment this out, so it is executed
		
		IController controller = tester;
		
		// Don't change the lines below here, which connect things together
		
		// Initialise everything...
		model.initialise(8, 8, view, controller);
		controller.initialise(model, view);
		view.initialise(model, controller);
		
		// Now start the game - set up the board
		controller.startup();
	}

	
	IModel model;
	IView view;
	
	java.util.Random rand = new java.util.Random();

	
	
	@Override
	public void initialise(IModel model, IView view)
	{
		this.model = model;
		this.view = view;
	}

	@Override
	public void startup()
	{
		// Initialise board
		int width = model.getBoardWidth();
		int height = model.getBoardHeight();
		for ( int x = 0 ; x < width ; x++ )
			for ( int y = 0 ; y < height ; y++ )
				model.setBoardContents(x, y, 0);
		// Consider setting up any initial pieces here in your own controller
		
		// Refresh all messages and frames
		view.feedbackToUser(1, "startup() was called successfully");
		view.feedbackToUser(2, "startup() was called successfully");
		view.refreshView();
	}

	
	
	@Override
	public void squareSelected(int player, int x, int y)
	{
		model.setBoardContents(x, y, player);		
		view.feedbackToUser(player, "You last played in location " + x + "," + y);
		view.refreshView();
	}
	
	
	@Override
	public void doAutomatedMove(int player)
	{
		view.feedbackToUser(player, "Automated move was pressed successfully - setting next location" );
		
		boolean doneOne = false;
		int repeat = rand.nextInt(6) + 5;
		
		for ( int y = 0 ; y < 8 ; y++ )
			for ( int x = 0 ; x < 8 ; x++ )
				if ( model.getBoardContents(x, y) == 0 )
				{
					model.setBoardContents(x, y, player);
					view.refreshView();
					view.feedbackToUser(player, "Automated move played in location " + x + "," + y );
					if ( --repeat == 0 )
						return;
					doneOne = true;
				}
		
		// If we filled at least one then let it end now
		if ( doneOne )
			return;
		
		model.clear(0);
		view.refreshView();
		view.feedbackToUser( player, "Automated move cleared board as it had nowhere to play" );
	}



	@Override
	public void update()
	{
		// Here we will set finished based upon whether there is any space on the board or not...
		boolean finished = true;
		for ( int x = 0 ; x < model.getBoardWidth() ; x++ )
			for ( int y = 0 ; y < model.getBoardHeight() ; y++ )
				if ( model.getBoardContents(x, y) == 0 )
					finished = false; // There is an empty square
		model.setFinished(finished);
		
		// We assume that something might have changed so update the labels accordingly, then tell view to update itself
		// In this controller though we don't use the finished flag or player number, so we probably just tell the user what was set, for debug purposes
		view.feedbackToUser(1, "I just updated: finished = " + model.hasFinished() + ", current player = " + model.getPlayer() );
		view.feedbackToUser(2, "I just updated: finished = " + model.hasFinished() + ", current player = " + model.getPlayer() );
		view.refreshView();
	}
}
