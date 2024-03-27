import javax.swing.*;//create label and jfram other gui elements//
import javax.swing.border.LineBorder;

import java.awt.*;//used for give color and otherstyling//
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;//can handle oporations on array
import java.util.Collections;//to shuffle the elements in java//


public class JumbledNumbersGame extends JFrame {
    private JButton[][] buttons;//button code//
    private Integer[] tiles;//numbers that will be displayed 
    private JLabel timerLabel;//label code//
    private Timer timer;//timer code//
    private int secondsPassed;//it stores the number of past time
    private String playerName;//player name//
    private int size;//stores the size of the board

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
                button.setBackground(Color.orange); // Change the background color here
    
                // Increase font size and set font to Times New Roman
                button.setFont(new Font("Times New Roman", Font.PLAIN, 20)); // Change font size and style here
                
                // Add colored stroke to the button
                button.setBorder(new LineBorder(Color.yellow, 2)); // You can customize the color and thickness
    
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
        final int totalTime = size == 3 ? 10 : 20; // Total time for 3x3 (10 seconds) and 4x4 (20 seconds)
    
        timer = new Timer(period, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondsPassed++;
                timerLabel.setText("Time: " + secondsPassed + " seconds");
                if (secondsPassed >= totalTime) {
                    timer.stop();
                    int option = JOptionPane.showConfirmDialog(JumbledNumbersGame.this, "Time's up! Do you want to try again?", "Time's Up!", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        resetGame();
                    } else {
                        String[] options = {"3x3 Matrix", "4x4 Matrix"};
                        int choice = JOptionPane.showOptionDialog(JumbledNumbersGame.this, "Choose matrix size", "Matrix Size", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        size = (choice == 0) ? 3 : 4;
                        initializeTiles();
                        updateUI();
                        startTimer();
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
        initializeTiles();
        this.secondsPassed = 0;
        this.timerLabel.setText("Time: 0 seconds");
        updateUI();
        startTimer();
    }
 
    

    public static void main(String[] args) {
        // Set UIManager properties for improved appearance
        UIManager.put("OptionPane.background", Color.lightGray);
        UIManager.put("Panel.background", Color.lightGray);
        UIManager.put("OptionPane.messageFont", new Font("Times New Roman", Font.PLAIN, 18));
        UIManager.put("Button.background", Color.yellow);
        UIManager.put("Button.foreground", Color.black);
        UIManager.put("Button.font", new Font("Times New Roman", Font.BOLD, 16));

        String[] playerNameHolder = new String[1]; // A holder array to store playerName

        while (true) {
            String playerName = JOptionPane.showInputDialog(null, "Enter your name:", "Player Name", JOptionPane.PLAIN_MESSAGE);
            if (playerName == null || playerName.trim().isEmpty()) {
                int option = JOptionPane.showConfirmDialog(null, "Name cannot be empty. Do you want to try again?", "Empty Name", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (option != JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, "Exiting the game.", "Goodbye!", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
            } else {
                playerNameHolder[0] = playerName; // Assign playerName to the holder array
                break; // Break the loop if playerName is not empty
            }
        }

        String[] options = {"3x3 Matrix", "4x4 Matrix"};
        int choice = JOptionPane.showOptionDialog(null, "Choose matrix size", "Matrix Size", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        int size = (choice == 0) ? 3 : 4;

        final String playerName = playerNameHolder[0]; // Retrieve playerName from the holder array

        SwingUtilities.invokeLater(() -> {
            new JumbledNumbersGame(playerName, size);
        });
    }
    
}
