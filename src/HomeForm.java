import javax.swing.*;

public class HomeForm extends JFrame {

    String username;

    public HomeForm(String u) {
        username = u;

        setTitle("Home");
        setSize(350, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblWelcome = new JLabel("Welcome, " + username);
        lblWelcome.setBounds(100, 20, 200, 30);
        add(lblWelcome);

        JButton btnPlay = new JButton("Play RPS Game");
        btnPlay.setBounds(100, 70, 150, 30);
        add(btnPlay);

        JButton btnPlayMemory = new JButton("Play Memory Card Game");
        btnPlayMemory.setBounds(100, 110, 150, 30);
        add(btnPlayMemory);

        JButton btnLeaderboard = new JButton("Leaderboard");
        btnLeaderboard.setBounds(100, 150, 150, 30);
        add(btnLeaderboard);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBounds(100, 190, 150, 30);
        add(btnLogout);

        btnPlay.addActionListener(e -> {
            new GameForm(username);
            dispose();
        });

        btnPlayMemory.addActionListener(e -> {
            new MemoryCardGame(username);
            dispose();
        });

        btnLeaderboard.addActionListener(e -> {
            new LeaderboardForm();
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginForm();
            dispose();
        });

        setVisible(true);
    }
}