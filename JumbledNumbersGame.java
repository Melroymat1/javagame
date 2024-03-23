import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;

public class JumbledNumbersGame extends JFrame {
    private JButton[][] buttons;
    private int size;
    private Integer[] tiles;
    private JLabel timerLabel;
    private Timer timer;
    private int secondsPassed;
    private String playerName;

    public JumbledNumbersGame(int size, String playerName) {
        this.size = size;
        this.tiles = new Integer[size * size];
        this.playerName = playerName;
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

        add(timerLabel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        setTitle("Jumbled Numbers Game");
        setSize(300, 300);
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
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                timerLabel.setText("Time: " + secondsPassed + " seconds");
            }
        });
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

    public static void main(String[] args) {
        final String playerName = JOptionPane.showInputDialog("Enter your name:");
        if (playerName == null || playerName.trim().isEmpty()) {
            
        }
        String[] options = {"Easy (3x3)", "Medium (4x4)", "Hard (5x5)"};
        int choice = JOptionPane.showOptionDialog(null, "Select difficulty level", "Difficulty",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        final int gameSize = (choice == 1) ? 4 : (choice == 2) ? 5 : 3;
        SwingUtilities.invokeLater(() -> {
            new JumbledNumbersGame(gameSize, playerName);
        });
    }
}
