import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignupForm extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;

    public SignupForm() {
        setTitle("Sign Up");
        setSize(350, 250);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setBounds(120, 20, 200, 30);
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

        JButton btnSignUp = new JButton("Sign Up");
        btnSignUp.setBounds(120, 150, 100, 30);
        add(btnSignUp);

        btnSignUp.addActionListener(e -> registerUser());

        setVisible(true);
    }

    private void registerUser() {
        try {
            Connection conn = Koneksi.connect();
            String sql = "INSERT INTO users(username, password, score) VALUES (?, ?, 0)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, txtUser.getText());
            stmt.setString(2, String.valueOf(txtPass.getPassword()));
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Account created!");
            new LoginForm();
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed: " + e.getMessage());
        }
    }
}