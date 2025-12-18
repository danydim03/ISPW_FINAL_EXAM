package org.example;

import org.example.dao_manager.DBConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckRoleTables {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.getInstance().getConnection();
            Statement stmt = conn.createStatement();

            System.out.println("--- CHECKING ADMIN TABLE ---");
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM AMMINISTRATORE")) {
                while (rs.next()) {
                    System.out.println("Admin ID: " + rs.getString("ID"));
                }
            }

            System.out.println("\n--- CHECKING CLIENTE TABLE ---");
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM CLIENTE")) {
                while (rs.next()) {
                    System.out.println("Cliente ID: " + rs.getString("ID"));
                }
            }

            System.out.println("\n--- CHECKING KEBABBARO TABLE ---");
            try (ResultSet rs = stmt.executeQuery("SELECT * FROM KEBABBARO")) {
                while (rs.next()) {
                    System.out.println("Kebabbaro ID: " + rs.getString("ID"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
