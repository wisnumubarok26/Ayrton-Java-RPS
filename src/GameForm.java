import javax.swing.*;
import java.sql.*;
import java.util.Random;

public class GameForm extends JFrame {

    JLabel lblComp, lblResult;
    String username;

    public GameForm(String u) {
        username = u;

        setTitle("Rock Paper Scissors");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblChoose = new JLabel("Choose one:");
        lblChoose.setBounds(150, 20, 150, 20);
        add(lblChoose);

        JButton btnRock = new JButton("Rock");
        btnRock.setBounds(30, 60, 100, 30);
        add(btnRock);

        JButton btnPaper = new JButton("Paper");
        btnPaper.setBounds(150, 60, 100, 30);
        add(btnPaper);

        JButton btnScissors = new JButton("Scissors");
        btnScissors.setBounds(270, 60, 100, 30);
        add(btnScissors);

        lblComp = new JLabel("Computer chose: ");
        lblComp.setBounds(30, 120, 300, 30);
        add(lblComp);

        lblResult = new JLabel("Result: ");
        lblResult.setBounds(30, 150, 300, 30);
        add(lblResult);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(150, 200, 100, 30);
        add(btnBack);

        btnRock.addActionListener(e -> play("rock"));
        btnPaper.addActionListener(e -> play("paper"));
        btnScissors.addActionListener(e -> play("scissors"));

        btnBack.addActionListener(e -> {
            new HomeForm(username);
            dispose();
        });

        setVisible(true);
    }

    private void play(String player) {
        String[] choices = {"rock", "paper", "scissors"};
        String comp = choices[new Random().nextInt(3)];

        lblComp.setText("Computer chose: " + comp);

        String result;
        if (player.equals(comp)) {
            result = "Draw!";
        } else if (
            (player.equals("rock") && comp.equals("scissors")) ||
            (player.equals("paper") && comp.equals("rock")) ||
            (player.equals("scissors") && comp.equals("paper"))
        ) {
            result = "You win!";
            updateScore(username);
            System.out.println("Updating score for: " + username);
        } else {
            result = "You lose!";
        }

        lblResult.setText("Result: " + result);
    }

    private void updateScore(String username) {
        try {
            Connection conn = Koneksi.connect();
            String sql = "UPDATE users SET score = score + 1 WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.executeUpdate();
            System.out.println("Updating score for: " + username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}