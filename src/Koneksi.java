import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {

    private static Connection conn;

    public static Connection connect() {
        try {
            if (conn == null || conn.isClosed()) {

                Class.forName("org.sqlite.JDBC");

                conn = DriverManager.getConnection("jdbc:sqlite:rps.db");

                // Set busy timeout to 5000 milliseconds (5 seconds)
                conn.createStatement().execute("PRAGMA busy_timeout = 5000;");

                System.out.println("Connection successful!");
            }
        } catch (Exception e) {
            System.out.println("Connection failed: " + e.getMessage());
        }

        return conn;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }
}