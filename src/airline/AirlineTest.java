package airline;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AirlineTest {

    public static void main(String[] args) {

        try {
            Connection con = DBConnection.getConnection();

            // 1) Insert test customer
            String insert = "INSERT INTO customers (name, email, phone, passport_no, address) VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(insert);
            ps.setString(1, "Ashish Kumar");
            ps.setString(2, "ashish@gmail.com");
            ps.setString(3, "9876543210");
            ps.setString(4, "P987654");
            ps.setString(5, "Jaipur");

            int rows = ps.executeUpdate();
            System.out.println("✅ Customer Inserted: " + rows);

            // 2) Fetch all customers
            String select = "SELECT * FROM customers";
            ps = con.prepareStatement(select);
            ResultSet rs = ps.executeQuery();

            System.out.println("\n------ CUSTOMER LIST ------");
            while (rs.next()) {
                System.out.println(
                    rs.getInt("id") + " | " +
                    rs.getString("name") + " | " +
                    rs.getString("email") + " | " +
                    rs.getString("phone") + " | " +
                    rs.getString("passport_no") + " | " +
                    rs.getString("address")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
