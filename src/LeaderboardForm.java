import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class LeaderboardForm extends JFrame {

    public LeaderboardForm() {

        setTitle("Leaderboard");
        setSize(400, 300);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("Leaderboard");
        lblTitle.setBounds(150, 10, 150, 20);
        add(lblTitle);

        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Username");
        model.addColumn("RPS");
        model.addColumn("Memory Card");
        model.addColumn("Snake Game");
        model.addColumn("Total Score");

        try {
            Connection conn = Koneksi.connect();
            String sql = "SELECT username, score, score_memory_card,score_snakegame,(score+score_memory_card+score_snakegame) as "+"total"+" FROM users ORDER BY " + "total"+" DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getString("username"),
                        rs.getInt("score"),
                        rs.getInt("score_memory_card"),
                        rs.getInt("score_snakegame"),
                        rs.getInt("total")
                });
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        table.setModel(model);

        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(50, 50, 300, 150);
        add(sp);

        JButton btnBack = new JButton("Back");
        btnBack.setBounds(150, 220, 100, 30);
        add(btnBack);

        btnBack.addActionListener(e -> {
            new LoginForm();
            dispose();
        });

        setVisible(true);
    }
}