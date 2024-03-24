import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

public class JumbledNumbersGame extends JFrame {
    private JButton[][] buttons;
    private final int size = 3; // Fixed size as 3x3 matrix
    private Integer[] tiles;
    private JLabel timerLabel;
    private Timer timer;
    private int secondsPassed;
    private String playerName;

    public JumbledNumbersGame(String playerName) {
        this.playerName = playerName;
        this.tiles = new Integer[size * size];
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
                int number = tiles[i * size + j] != null ? tiles[i * size + j] : 0; // Ensure numbers are between 1 and 8
                JButton button = new JButton(number != 0 ? String.valueOf(number) : "");
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
                JOptionPane.showMessageDialog(this, "Congratulations, " + playerName + "! You've solved the puzzle in " + secondsPassed + " seconds.");
                System.exit(0);
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
        final int totalTime = 120; // Total time for round (4 minutes)

        timer = new Timer(period, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                timerLabel.setText("Time: " + secondsPassed + " seconds");
                if (secondsPassed >= totalTime) {
                    timer.stop();
                    JOptionPane.showMessageDialog(JumbledNumbersGame.this, "Time's up! Try again.");
                    resetGame();
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
        initializeTiles();
        this.secondsPassed = 0;
        this.timerLabel.setText("Time: 0 seconds");

        // Reset the timer
        timer.stop();
        startTimer();

        updateUI();
    }

    public static void main(String[] args) {
        final String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            return;
        }

        SwingUtilities.invokeLater(() -> {
            new JumbledNumbersGame(playerName);
        });
    }
}
