package reversi;

import javax.swing.*;
import java.awt.*;

public class GUIView implements IView {
    private IModel model;
    private IController controller;

    private JFrame frame1;
    private JFrame frame2;
    private JLabel topLabel1;
    private JLabel topLabel2;
    private BoardSquareButton[][] buttons;

    @Override
    public void initialise(IModel model, IController controller) {
        this.model = model;
        this.controller = controller;

        int width = model.getBoardWidth();
        int height = model.getBoardHeight();
        buttons = new BoardSquareButton[height][width];

        createPlayerWindow(1);
        createPlayerWindow(2);
    }

    private void createPlayerWindow(int playerNumber) {
        JFrame frame = new JFrame("Reversi - " + (playerNumber == 1 ? "black" : "white") + " player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JPanel labelPanel = new JPanel(new BorderLayout());
        JLabel topLabel = new JLabel("", SwingConstants.CENTER);
        topLabel.setFont(topLabel.getFont().deriveFont(Font.BOLD));
        labelPanel.add(topLabel, BorderLayout.CENTER);
        frame.add(labelPanel, BorderLayout.NORTH);

        if (playerNumber == 1) topLabel1 = topLabel;
        else topLabel2 = topLabel;

        int width = model.getBoardWidth();
        int height = model.getBoardHeight();
        JPanel boardPanel = new JPanel(new GridLayout(height, width));
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                BoardSquareButton btn = new BoardSquareButton(row, col, playerNumber);
                buttons[row][col] = btn;
                boardPanel.add(btn);
            }
        }

        frame.add(boardPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton aiButton = new JButton("Greedy AI (play " + (playerNumber == 1 ? "black" : "white") + ")");
        JButton restartButton = new JButton("Restart");

        aiButton.addActionListener(e -> controller.doAutomatedMove(playerNumber));
        restartButton.addActionListener(e -> controller.startup());

        bottomPanel.add(aiButton);
        bottomPanel.add(restartButton);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();
        if (playerNumber == 1) frame.setLocation(100, 100);
        else frame.setLocation(750, 100);

        frame.setVisible(true);

        if (playerNumber == 1) frame1 = frame;
        else frame2 = frame;
    }

    @Override
    public void refreshView() {
        int width = model.getBoardWidth();
        int height = model.getBoardHeight();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                buttons[row][col].repaint();
            }
        }

        int currentPlayer = model.getPlayer();
        if (topLabel1 != null) {
            if (currentPlayer == 1) {
                topLabel1.setText("Black player – choose where to put your piece");
            } else {
                topLabel1.setText("Black player – not your turn");
            }
        }

        if (topLabel2 != null) {
            if (currentPlayer == 2) {
                topLabel2.setText("White player – choose where to put your piece");
            } else {
                topLabel2.setText("White player – not your turn");
            }
        }

    }

    @Override
    public void feedbackToUser(int player, String message) {
        if (player == 1 && topLabel1 != null) {
            topLabel1.setText(message);
        } else if (player == 2 && topLabel2 != null) {
            topLabel2.setText(message);
        }
    }



    private class BoardSquareButton extends JButton {
        private final int row;
        private final int col;
        private final int ownerPlayer;

        public BoardSquareButton(int row, int col, int ownerPlayer) {
            this.row = row;
            this.col = col;
            this.ownerPlayer = ownerPlayer;

            setPreferredSize(new Dimension(60, 60));
            setFocusPainted(false);
            setContentAreaFilled(false);

            addActionListener(e -> controller.squareSelected(ownerPlayer, row, col));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0, 200, 0));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            int value = GUIView.this.model.getBoardContents(row, col);
            if (value != 0) {
                g.setColor(value == 1 ? Color.BLACK : Color.WHITE);
                int size = Math.min(getWidth(), getHeight()) - 10;
                int x = (getWidth() - size) / 2;
                int y = (getHeight() - size) / 2;
                g.fillOval(x, y, size, size);
            }
        }
    }
}

