import java.sql.Connection;
import java.sql.DriverManager;

public class Koneksi {
    public static Connection connect() {
        Connection conn = null;

        try {
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Connect to the database
            conn = DriverManager.getConnection("jdbc:sqlite:rps.db");
            System.out.println("Connection successful!");

        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return conn;
    }
}
