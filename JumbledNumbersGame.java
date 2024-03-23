import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

public class JumbledNumbersGame extends JFrame {
    private JButton[][] buttons;
    private final int size; // Declaring size as final
    private Integer[] tiles;
    private JLabel timerLabel;
    private Timer timer;
    private int secondsPassed;
    private String playerName;
    private int round;

    public JumbledNumbersGame(int size, String playerName) {
        this.size = size;
        this.tiles = new Integer[size * size];
        this.playerName = playerName;
        this.round = 1; // Initialize round to 1
        initializeTiles();
        createUI();
        startTimer();
    }

    private void initializeTiles() {
        for (int i = 0; i < size * size - 1; i++) {
            tiles[i] = i + 1;
        }
        tiles[size * size - 1] = null; // represents empty space
        shuffleTiles();
    }

    private void shuffleTiles() {
        Collections.shuffle(Arrays.asList(tiles));
    }

    private void createUI() {
        JPanel panel = new JPanel(new GridLayout(size, size));

        buttons = new JButton[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                final int row = i;
                final int col = j;
                JButton button = new JButton(tiles[i * size + j] != null ? tiles[i * size + j].toString() : "");
                button.setBackground(Color.white); // Change button background color
                button.setPreferredSize(new Dimension(80, 80)); // Adjust button size
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moveTile(row, col);
                    }
                });
                buttons[i][j] = button;
                panel.add(button);
            }
        }

        timerLabel = new JLabel("Time: 0 seconds");
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(timerLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setTitle("Jumbled Numbers Game");
        setSize(400, 400); // Adjust game window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void moveTile(int row, int col) {
        if (isValidMove(row, col)) {
            if (tiles[row * size + col] == null) return;
            int emptyRow = -1, emptyCol = -1;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (tiles[i * size + j] == null) {
                        emptyRow = i;
                        emptyCol = j;
                        break;
                    }
                }
            }
            tiles[emptyRow * size + emptyCol] = tiles[row * size + col];
            tiles[row * size + col] = null;
            updateUI();
            if (isGameFinished()) {
                timer.stop();
                if (round == 1) {
                    JOptionPane.showMessageDialog(this, "Congratulations, " + playerName + "! You've solved the puzzle in " + secondsPassed + " seconds. Press Enter to start Round 2.");
                    round++;
                    timerLabel.setText("Press Enter to start Round 2");
                } else if (round == 2) {
                    JOptionPane.showMessageDialog(this, "Congratulations, " + playerName + "! You've completed Round 2. Press Enter to start Round 3.");
                    round++;
                    timerLabel.setText("Press Enter to start Round 3");
                } else if (round == 3) {
                    JOptionPane.showMessageDialog(this, "Congratulations, " + playerName + "! You've completed Round 3.");
                }
            }
        }
    }

    private boolean isValidMove(int row, int col) {
        int emptyRow = -1, emptyCol = -1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (tiles[i * size + j] == null) {
                    emptyRow = i;
                    emptyCol = j;
                    break;
                }
            }
        }
        return Math.abs(row - emptyRow) + Math.abs(col - emptyCol) == 1;
    }

    private void updateUI() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                buttons[i][j].setText(tiles[i * size + j] != null ? tiles[i * size + j].toString() : "");
            }
        }
    }

    private void startTimer() {
        int initialDelay = 0;
        int period = 1000;
        final int totalTime;
        if (round == 2) {
            totalTime = 180; // Increase time for round 2 (3 minutes)
        } else if (round == 3) {
            totalTime = 240; // Increase time for round 3 (4 minutes)
        } else {
            totalTime = 120; // Initial time for round 1 (2 minutes)
        }

        timer = new Timer(period, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                timerLabel.setText("Time: " + secondsPassed + " seconds");
                if (secondsPassed >= totalTime) {
                    timer.stop();
                    if (round == 1) {
                        int option = JOptionPane.showConfirmDialog(JumbledNumbersGame.this, "Time's up ! Do you want to try again?", "Time's Up", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            resetGame();
                        } else {
                            System.exit(0);
                        }
                    } else {
                        int option = JOptionPane.showConfirmDialog(JumbledNumbersGame.this, "Time's up! Do you want to try again?", "Time's Up", JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            resetGame();
                        } else {
                            System.exit(0);
                        }
                    }
                }
            }
        });
        timer.setInitialDelay(initialDelay);
        timer.start();
    }

    private boolean isGameFinished() {
        for (int i = 0; i < size * size - 1; i++) {
            if (tiles[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        round++;
        int gameSize = 3; // Default game size
        if (round == 2) {
            gameSize = 4; // Change to 4x4 for round 2
        } else if (round == 3) {
            gameSize = 5; // Change to 5x5 for round 3
        }
        int newSize = gameSize * gameSize;
        Integer[] newTiles = new Integer[newSize];
        for
        (int i = 0; i < Math.min(newSize, tiles.length); i++) {
            newTiles[i] = tiles[i];
        }
        this.tiles = newTiles;
        // Remove the following line since size is final and cannot be reassigned
        // this.size = gameSize;
        initializeTiles();
        this.secondsPassed = 0;
        this.timerLabel.setText("Time: 0 seconds");
    
        // Stop the old timer if it's running
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
    
        startTimer(); // Start a new timer
        updateUI(); // Update UI with new game size and tiles
    }
    
    

    public static void main(String[] args) {
        final String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }
        
        Object[] options = {"3x3 Matrix", "4x4 Matrix", "5x5 Matrix"};
        int choice = JOptionPane.showOptionDialog(null, "Select Matrix Size", "Matrix Size", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        
        int size;
        switch (choice) {
            case 0:
                size = 3;
                break;
            case 1:
                size = 4;
                break;
            case 2:
                size = 5;
                break;
            default:
                size = 3; // Default to 3x3 matrix if no option is chosen
                break;
        }
        
        SwingUtilities.invokeLater(() -> {
            new JumbledNumbersGame(size, playerName);
        });
    }
    
}    

