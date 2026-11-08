
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TicTacToe {

    int boardWidth = 600;
    int boardHeight = 750; // 50px text panel on top + 100px control panel at the bottom

    JFrame frame = new JFrame("Tic-Tac-Toe _avi");//title for the program
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    // bottom panel: scoreboard + buttons
    JPanel bottomPanel = new JPanel();
    JLabel scoreLabel = new JLabel();
    JPanel buttonPanel = new JPanel();
    JButton restartButton = new JButton("Restart");
    JButton resetScoreButton = new JButton("Reset Scores");

    JButton[][] board = new JButton[3][3];
    String playerX = "X";
    String playerO = "O";
    String currentPlayer = playerX; //1st player is (playerX) by default

    boolean gameOver = false;
    int turns = 0;

    // score counters
    int scoreX = 0;
    int scoreO = 0;
    int ties = 0;

    public TicTacToe() {
        //Frame
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //opens window at center
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //Color and Text
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");//default text
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);//lekha take upore pathay dilam

        boardPanel.setLayout(new GridLayout(3, 3));//makes a 3*3 grid layout
        boardPanel.setBackground(Color.GRAY);
        frame.add(boardPanel);

        //Bottom panel: scoreboard + buttons
        scoreLabel.setBackground(Color.darkGray);
        scoreLabel.setForeground(Color.white);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 28));
        scoreLabel.setHorizontalAlignment(JLabel.CENTER);
        scoreLabel.setOpaque(true);
        updateScoreLabel();

        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setFocusable(false);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                restartGame();
            }
        });

        resetScoreButton.setFont(new Font("Arial", Font.BOLD, 20));
        resetScoreButton.setFocusable(false);
        resetScoreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                scoreX = 0;
                scoreO = 0;
                ties = 0;
                updateScoreLabel();
                restartGame();
            }
        });

        buttonPanel.setLayout(new GridLayout(1, 2));
        buttonPanel.add(restartButton);
        buttonPanel.add(resetScoreButton);

        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.add(scoreLabel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        //3*3 button (total 9 buttons)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                boardPanel.add(tile);

                //properties for style the buttons
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                //when click set to either X or O
                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        //if already over, dont update the tiles
                        if (gameOver) {
                            return;
                        }

                        JButton tile = (JButton) e.getSource(); //where the event is coming from (from a button)
                        //if empty space | so that no override
                        if (tile.getText().isEmpty()) {
                            tile.setText(currentPlayer);

                            turns++;

                            //check winner
                            checkWinner();
                            if (!gameOver) {
                                //alternate the current player
                                currentPlayer = currentPlayer.equals(playerX) ? playerO : playerX;
                                textLabel.setText(currentPlayer + " 's turn now");
                            }

                        }

                    }
                });
            }
        }

        frame.setVisible(true);
    }

    void checkWinner() {
        //horizontal
        for (int r = 0; r < 3; r++) {
            if (board[r][0].getText().isEmpty()) {
                continue;
            }
            //first tile = second tile = third tile
            if (board[r][0].getText().equals(board[r][1].getText())
                    && board[r][1].getText().equals(board[r][2].getText())) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[r][i]);
                }
                declareWinner();
                return;
            }

        }

        //vertical
        for (int c = 0; c < 3; c++) {
            if (board[0][c].getText().isEmpty()) {
                continue;
            }

            if (board[0][c].getText().equals(board[1][c].getText())
                    && board[1][c].getText().equals(board[2][c].getText())) {
                for (int i = 0; i < 3; i++) {
                    setWinner(board[i][c]);
                }
                declareWinner();
                return;
            }
        }

        //diagonally
        if (!board[0][0].getText().isEmpty()
                && board[0][0].getText().equals(board[1][1].getText())
                && board[1][1].getText().equals(board[2][2].getText())) {
            for (int i = 0; i < 3; i++) {
                setWinner(board[i][i]);
            }
            declareWinner();
            return;
        }

        //anti-diagonally
        if (!board[0][2].getText().isEmpty()
                && board[0][2].getText().equals(board[1][1].getText())
                && board[1][1].getText().equals(board[2][0].getText())) {

            for (int i = 0; i < 3; i++) {
                setWinner(board[i][2 - i]);
            }
            declareWinner();
            return;
        }

        if (turns == 9) {
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    setTie(board[r][c]);
                }
            }
            ties++;
            updateScoreLabel();
            gameOver = true;
        }
    }

    // called once per win: ends the game and adds a point to the current player
    void declareWinner() {
        gameOver = true;
        if (currentPlayer.equals(playerX)) {
            scoreX++;
        } else {
            scoreO++;
        }
        updateScoreLabel();
    }

    void setWinner(JButton tile) {
        tile.setForeground(Color.green);
        tile.setBackground(Color.gray);
        textLabel.setText(currentPlayer + " is the winner!");
    }

    void setTie(JButton tile) {
        tile.setBackground(Color.gray);
        tile.setForeground(Color.orange);
        textLabel.setText("Tie!");
    }

    void updateScoreLabel() {
        scoreLabel.setText("X: " + scoreX + "     Ties: " + ties + "     O: " + scoreO);
    }

    // clears the board for a new round (scores are kept)
    void restartGame() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setText("");
                board[r][c].setBackground(Color.darkGray);
                board[r][c].setForeground(Color.white);
            }
        }
        currentPlayer = playerX;
        gameOver = false;
        turns = 0;
        textLabel.setText("Tic-Tac-Toe");
    }
}
