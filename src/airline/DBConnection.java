package airline;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
        "jdbc:mysql://127.0.0.1:3306/airline_db2?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";      // apna MySQL username
    private static final String PASSWORD = "Jay111";  // apna MySQL password

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {

                Class.forName("com.mysql.cj.jdbc.Driver");

                connection = DriverManager.getConnection(URL, USER, PASSWORD);

                System.out.println("✅ Connected to airline_db2 successfully!");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Database Connection Failed");
            e.printStackTrace();
        }
        return connection;
    }
}
