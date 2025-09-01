package reversi;

// Model
// This is the data storage for the board
//
public interface IModel
{
	/**
	 * Initialise the board to a specified size, and store references to the view and controller. Note that the basic model doesn't use these references!
	 * @param width Width, in squares. x positions are 0-based, so assume width 8, with columns 0 to 7 for reversi.
	 * @param height Height, in squares. y positions are 0-based, so assume height 8, with rows 0 to 7 for reversi.
	 * @param view the view to use - this will be stored in case it is needed later.
	 * @param controller the controller to use - this will be stored in case it is needed later.
	 */
	void initialise( int width, int height, IView view, IController controller );
	
	/**
	 * Only used by controllers which have a concept of current player. Set the current player number - if the controller uses a concept of player number.
	 * 1 = player White
	 * 2 = player Black
	 * @param player The player number, e.g. 1 or 2.
	 */
	void setPlayer(int player);
	
	/**
	 * Only used by controllers which have a concept of current player. Get the current player number - if the controller uses a concept of player number.
	 * 1 = player White
	 * 2 = player Black
	 * @return the player number, e.g. 1 or 2.
	 */
	int getPlayer();
	
	/**
	 * Determine whether the game has finished or not - as set by setFinished();
	 * @return true if game has finished, false otherwise.
	 */
	boolean hasFinished();
	
	/**
	 * Store whether the game has finished or not.
	 * @param finished true if game has finished, galse otherwise
	 */
	void setFinished( boolean finished );
	

	/**
	 * Clear the board, setting all squares to the specified value
	 * @param value the value to set squares to
	 */
	void clear( int value );
	
	/**
	 * Get the board width, in squares
	 * @return The board width
	 */ 
	int getBoardWidth();
	
	/**
	 * Get the board height, in squares
	 * @return The board height
	 */ 
	int getBoardHeight();

	/**
	 * Get the contents of a square of the board
	 * For reversi, use the values: 0 = no piece, 1 = white piece, 2 = black piece
	 * @param x The x position (column) to access. Assume 0 to 7 for reversi.
	 * @param y The y position (row) to access. Assume 0 to 7 for reversi.
	 * @return The value of that square, as set by clear() or setBoardContents()
	 */
	int getBoardContents( int x, int y );
	
	/**
	 * Set the contents of a square of the board to a specified value.
 	 * For reversi, use the values: 0 = no piece, 1 = white piece, 2 = black piece
	 * @param x The x position (column) to access. Assume 0 to 7 for reversi.
	 * @param y The y position (row) to access. Assume 0 to 7 for reversi.
	 * @param value the new value for this square
	 */
	void setBoardContents( int x, int y, int value );
}
