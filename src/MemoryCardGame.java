import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import javax.swing.Timer;

public class MemoryCardGame extends JFrame {

    private JButton[] buttons = new JButton[16];
    private Integer[] cardValues = new Integer[16];
    private JButton firstSelected = null;
    private JButton secondSelected = null;
    private Timer flipBackTimer;

    String username;
    int score = 0;
    JLabel labelTimer;

    public MemoryCardGame(String u) {
        username = u;

        setTitle("Memory Card Game");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // top panel with title, instructions, timer, reset button, and back button
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Memory Card Game", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(lblTitle);

        JLabel lblInstructions = new JLabel("Find all matching pairs!", SwingConstants.CENTER);
        lblInstructions.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(lblInstructions);

        labelTimer = new JLabel("Time: 0s", SwingConstants.CENTER);
        labelTimer.setFont(new Font("Arial", Font.PLAIN, 16));
        topPanel.add(labelTimer);

        JButton btnReset = new JButton("Reset Game");
        btnReset.addActionListener(e -> resetGame());
        topPanel.add(btnReset);

        // Back button
        JButton btnBack = new JButton("Back to Home");
        btnBack.addActionListener(e -> {
            dispose(); // Tutup game
            new HomeForm(username); 
        });
        topPanel.add(btnBack);

        add(topPanel, BorderLayout.NORTH);

        // timer 
        Timer gameTimer = new Timer(1000, new ActionListener() {
            int seconds = 0;
            public void actionPerformed(ActionEvent e) {
                seconds++;
                labelTimer.setText("Time: " + seconds + "s");
            }
        });
        gameTimer.start();

        // game panel with 4x4 grid of buttons
        JPanel gamePanel = new JPanel(new GridLayout(4, 4));
        add(gamePanel, BorderLayout.CENTER);

        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);

        for (int i = 0; i < 16; i++) {
            cardValues[i] = values.get(i);

            JButton btn = new JButton("");
            btn.setFont(new Font("Arial", Font.BOLD, 24));
            btn.addActionListener(new CardButtonListener());
            buttons[i] = btn;
            gamePanel.add(btn);
        }

        flipBackTimer = new Timer(800, e -> {
            firstSelected.setText("");
            secondSelected.setText("");
            firstSelected = null;
            secondSelected = null;
        });
        flipBackTimer.setRepeats(false);

        setVisible(true);
    }

    private class CardButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JButton clicked = (JButton) e.getSource();

            if (flipBackTimer.isRunning()) return;
            if (!clicked.getText().equals("")) return;

            int index = Arrays.asList(buttons).indexOf(clicked);
            clicked.setText(String.valueOf(cardValues[index]));

            if (firstSelected == null) {
                firstSelected = clicked;
            } else if (secondSelected == null) {
                secondSelected = clicked;

                if (firstSelected.getText().equals(secondSelected.getText())) {
                    firstSelected.setEnabled(false);
                    secondSelected.setEnabled(false);

                    firstSelected = null;
                    secondSelected = null;

                    if (checkWin()) {
                        JOptionPane.showMessageDialog(null, "Congratulations! You Win!");
                        updateScore(username);
                        resetGame();
                    }
                } else {
                    flipBackTimer.start();
                }
            }
        }
    }

    private boolean checkWin() {
        for (JButton btn : buttons) {
            if (btn.isEnabled()) return false;
        }
        return true;
    }

    private void resetGame() {
        ArrayList<Integer> values = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            values.add(i);
            values.add(i);
        }
        Collections.shuffle(values);

        for (int i = 0; i < 16; i++) {
            cardValues[i] = values.get(i);
            buttons[i].setText("");
            buttons[i].setEnabled(true);
        }
    }

    private void updateScore(String username) {
        try {
            Connection conn = Koneksi.connect();
            String sql = "UPDATE users SET score_memory_card = score_memory_card + 10 WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
            System.out.println("Updating score for: " + username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}