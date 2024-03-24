import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

public class JumbledNumbersGame extends JFrame {
    private JButton[][] buttons;
    private Integer[] tiles;
    private JLabel timerLabel;
    private Timer timer;
    private int secondsPassed;
    private String playerName;
    private int size;

    public JumbledNumbersGame(String playerName, int size) {
        this.playerName = playerName;
        this.size = size;
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
                int number = tiles[i * size + j] != null ? tiles[i * size + j] : 0;
                JButton button = new JButton(number != 0 ? String.valueOf(number) : "");
                button.setBackground(Color.white);
                button.setPreferredSize(new Dimension(80, 80));
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
        setSize(100 * size, 100 * size);
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
        final int totalTime = size == 3 ? 120 : 240; // Total time for 3x3 (2 minutes) and 4x4 (4 minutes)

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

        String[] options = {"3x3 Matrix", "4x4 Matrix"};
        int choice = JOptionPane.showOptionDialog(null, "Choose matrix size", "Matrix Size", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        int size = (choice == 0) ? 3 : 4;

        SwingUtilities.invokeLater(() -> {
            new JumbledNumbersGame(playerName, size);
        });
    }
}
