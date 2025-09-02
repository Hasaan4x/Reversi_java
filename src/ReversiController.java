package reversi;

public class ReversiController implements IController {
    private IModel model;
    private IView view;
    private int currentPlayer;

    @Override
    public void initialise(IModel model, IView view) {
        this.model = model;
        this.view = view;
        this.currentPlayer = 1; // Start with black
    }

    @Override
    public void squareSelected(int player, int x, int y) {
        if (model.hasFinished()) {
            return;
        }

        int current = model.getPlayer();

        // Reject move if not the current player's turn
        if (player != current) {
            if (player == 1) {
                view.feedbackToUser(1, "White player – not your turn");
            } else {
                view.feedbackToUser(2, "Black player - not your turn");
            }
            return;
        }


        // Check if selected square is a legal move
        if (!moveIsAllowed(x, y, player)) {
            view.feedbackToUser(player, "Invalid location to play a piece.");
            return;
        }

        // Execute move and flip opponent pieces
        placeAndFlipPieces(x, y, player);

        // Switch to other player
        int nextPlayer = (player == 1) ? 2 : 1;
        model.setPlayer(nextPlayer);

        // Refresh GUI
        view.refreshView();

        // Determine whether the next player has any valid move
        boolean nextPlayerCanMove = playerCanMove(nextPlayer);
        boolean currentPlayerCanMove = playerCanMove(player);

        if (!nextPlayerCanMove) {
            if (!currentPlayerCanMove) {
                model.setFinished(true);
                evaluateEndOfGame();
            } else {
                // Let current player play again
                model.setPlayer(player);
                view.feedbackToUser(player, "No moves for opponent – your turn again");
            }
        } else {
            // Prompt next player
            if (nextPlayer == 1) {
                view.feedbackToUser(1, "White player – choose where to put your piece");
                view.feedbackToUser(2, "Black player – not your turn");
            } else {
                view.feedbackToUser(2, "Black player – choose where to put your piece");
                view.feedbackToUser(1, "White player – not your turn");
            }

        }
    }

    @Override
    public void doAutomatedMove(int player) {
        int bestX = -1;
        int bestY = -1;
        int bestScore = Integer.MIN_VALUE;

        int boardW = model.getBoardWidth();
        int boardH = model.getBoardHeight();

        // Search all board positions for highest scoring move
        for (int col = 0; col < boardW; col++) {
            for (int row = 0; row < boardH; row++) {
                if (moveIsAllowed(col, row, player)) {
                    int gain = countFlipsFrom(col, row, player);
                    if (gain > bestScore) {
                        bestScore = gain;
                        bestX = col;
                        bestY = row;
                    }
                }
            }
        }

        // Make the move if a good option was found
        if (bestX >= 0 && bestY >= 0) {
            squareSelected(player, bestX, bestY);
        } else {
            view.feedbackToUser(player, "No valid moves for AI.");
        }
    }

    @Override
    public void startup() {
        int boardWidth = model.getBoardWidth();
        int boardHeight = model.getBoardHeight();

        model.clear(0);
        model.setFinished(false);

        int middleColumn = boardWidth / 2;
        int middleRow = boardHeight / 2;

        model.setBoardContents(middleColumn - 1, middleRow - 1, 1); // White (flipped)
        model.setBoardContents(middleColumn,     middleRow,     1); // White
        model.setBoardContents(middleColumn - 1, middleRow,     2); // Black
        model.setBoardContents(middleColumn,     middleRow - 1, 2); // Black


        model.setPlayer(1); // black always starts
        view.refreshView();

        // Send messages explicitly
        view.feedbackToUser(1, "White player – choose where to put your piece");
        view.feedbackToUser(2, "Black player – not your turn");
    }

    private boolean moveIsAllowed(int x, int y, int player) {
        // Square already occupied
        if (model.getBoardContents(x, y) != 0) {
            return false;
        }

        // Check all directions for a valid capture
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (offsetX == 0 && offsetY == 0) {
                    continue;
                }

                if (validFlipExists(x, y, offsetX, offsetY, player)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean validFlipExists(int startX, int startY, int dx, int dy, int player) {
        int enemy = (player == 1) ? 2 : 1;
        int checkX = startX + dx;
        int checkY = startY + dy;
        boolean opponentSeen = false;

        while (isInsideBoard(checkX, checkY)) {
            int piece = model.getBoardContents(checkX, checkY);

            if (piece == enemy) {
                opponentSeen = true;
            } else if (piece == player) {
                return opponentSeen;  // Only return true if there was at least one opponent piece before this
            } else {
                return false; // Empty square or invalid piece breaks chain
            }

            checkX += dx;
            checkY += dy;
        }

        return false;
    }

    private void placeAndFlipPieces(int x, int y, int player) {
        // Put the player's piece on the selected square
        model.setBoardContents(x, y, player);

        // Check and flip in all 8 directions
        for (int offsetX = -1; offsetX <= 1; offsetX++) {
            for (int offsetY = -1; offsetY <= 1; offsetY++) {
                if (offsetX == 0 && offsetY == 0) {
                    continue; // Skip current position
                }

                if (validFlipExists(x, y, offsetX, offsetY, player)) {
                    flipLine(x, y, offsetX, offsetY, player);
                }
            }
        }
    }

    private void flipLine(int startX, int startY, int dx, int dy, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int currentX = startX + dx;
        int currentY = startY + dy;

        while (isInsideBoard(currentX, currentY)) {
            int content = model.getBoardContents(currentX, currentY);
            if (content == opponent) {
                model.setBoardContents(currentX, currentY, player);
                currentX += dx;
                currentY += dy;
            } else {
                break;
            }
        }
    }


    private boolean playerCanMove(int player) {
        int boardWidth = model.getBoardWidth();
        int boardHeight = model.getBoardHeight();

        for (int col = 0; col < boardWidth; col++) {
            for (int row = 0; row < boardHeight; row++) {
                if (moveIsAllowed(col, row, player)) {
                    return true;  // Found a legal move
                }
            }
        }

        return false; // No valid moves found
    }


    private boolean isInsideBoard(int x, int y) {
        int maxX = model.getBoardWidth();
        int maxY = model.getBoardHeight();

        return (x >= 0 && x < maxX) && (y >= 0 && y < maxY);
    }

    private int countFlipsFrom(int x, int y, int player) {
        int totalFlips = 0;

        for (int deltaX = -1; deltaX <= 1; deltaX++) {
            for (int deltaY = -1; deltaY <= 1; deltaY++) {
                if (deltaX == 0 && deltaY == 0) {
                    continue;
                }

                totalFlips += countLine(x, y, deltaX, deltaY, player);
            }
        }

        return totalFlips;
    }

    private int countLine(int x, int y, int dx, int dy, int player) {
        int opponent = (player == 1) ? 2 : 1;
        int currentX = x + dx;
        int currentY = y + dy;
        int piecesToFlip = 0;

        while (isInsideBoard(currentX, currentY) &&
                model.getBoardContents(currentX, currentY) == opponent) {
            piecesToFlip++;
            currentX += dx;
            currentY += dy;
        }

        boolean endsOnOwnPiece = isInsideBoard(currentX, currentY) &&
                model.getBoardContents(currentX, currentY) == player;

        return endsOnOwnPiece ? piecesToFlip : 0;
    }

    private void evaluateEndOfGame() {
        int white = 0, black = 0;
        int w = model.getBoardWidth(), h = model.getBoardHeight();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int piece = model.getBoardContents(x, y);
                if (piece == 1) black++;
                else if (piece == 2) white++;
            }
        }

        if (!playerCanMove(1) && !playerCanMove(2)) {
            model.setFinished(true);
            String msg;
            if (black > white) {
                msg = "White won. White " + black + " to Black " + white + ". Reset the game to replay.";
            } else if (white > black) {
                msg = "Black won. Black " + white + " to White " + black + ". Reset the game to replay.";
            } else {
                msg = "Draw. Both players ended with " + black + " pieces. Reset the game to replay.";
            }


            view.feedbackToUser(1, msg);
            view.feedbackToUser(2, msg);
        }
    }

    @Override
    public void update() {
        view.refreshView();

        if (model.hasFinished() || (!playerCanMove(1) && !playerCanMove(2))) {
            model.setFinished(true);
            evaluateEndOfGame();
            return;
        }

        int current = model.getPlayer();
        if (playerCanMove(current)) {
            if (current == 1) {
                view.feedbackToUser(1, "White player – choose where to put your piece");
                view.feedbackToUser(2, "Black player – not your turn");
            } else {
                view.feedbackToUser(2, "Black player – choose where to put your piece");
                view.feedbackToUser(1, "White player – not your turn");
            }
        } else {
            int other = (current == 1) ? 2 : 1;
            if (playerCanMove(other)) {
                model.setPlayer(other);
                view.feedbackToUser(other, (other == 1 ? "White player" : "Black player") + " – choose where to put your piece");
                view.feedbackToUser(current, (current == 1 ? "White player" : "Black player") + " – not your turn");
            }
        }
    }



}

