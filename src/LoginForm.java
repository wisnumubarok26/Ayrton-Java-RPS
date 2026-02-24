import javax.swing.*;
import java.sql.*;

public class LoginForm extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;

    public LoginForm() {
        setTitle("Login");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("LOGIN");
        lblTitle.setBounds(150, 20, 100, 30);
        add(lblTitle);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(30, 70, 100, 20);
        add(lblUser);

        txtUser = new JTextField();
        txtUser.setBounds(120, 70, 150, 25);
        add(txtUser);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(30, 110, 100, 20);
        add(lblPass);

        txtPass = new JPasswordField();
        txtPass.setBounds(120, 110, 150, 25);
        add(txtPass);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(120, 150, 100, 30);
        add(btnLogin);

        JButton btnSignup = new JButton("Sign Up");
        btnSignup.setBounds(230, 150, 80, 30);
        add(btnSignup);

        btnLogin.addActionListener(e -> loginUser());
        btnSignup.addActionListener(e -> {
            new SignupForm();
            dispose();
        });

        setVisible(true);
    }

    private void loginUser() {
        try {
            Connection conn = Koneksi.connect();
            String sql = "SELECT * FROM users WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtUser.getText());
            stmt.setString(2, String.valueOf(txtPass.getPassword()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                JOptionPane.showMessageDialog(this, "Login successful!");
                new HomeForm(txtUser.getText());
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed: " + e.getMessage());
        }
    }
}