package org.example;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.model.user.User;
import org.example.model.role.AbstractRole;
import org.example.model.role.Cliente.Cliente;
import org.example.model.role.Amministratore.Amministratore;
import org.example.exceptions.MissingAuthorizationException;
import org.example.enums.UserRoleEnum;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.example.dao_manager.DBConnection;

public class DebugAuthorization {
    public static void main(String[] args) {
        try {
            System.out.println("DEBUG: Starting Authorization Debug...");

            // 1. Check Admin
            String emailAdmin = "admin@ispw.it";
            System.out.println("\n--- Testing Admin: " + emailAdmin + " ---");
            try {
                User admin = DAOFactoryAbstract.getInstance().getUserDAO().getUserByEmail(emailAdmin);
                System.out.println("User Role Class: " + admin.getRole().getClass().getName());
                Amministratore a = admin.getRole().getAmministratoreRole();
                System.out.println("SUCCESS: getAmministratoreRole() returned " + a);
            } catch (Exception e) {
                System.out.println("FAILURE Admin: " + e.getMessage());
                e.printStackTrace();
            }

            // 2. Check Client
            System.out.println("\n--- Searching for a Client User ---");
            // Find a user with role 1
            Connection conn = DBConnection.getInstance().getConnection();
            String sql = "SELECT email FROM USER WHERE role = 1 LIMIT 1";
            String clientEmail = null;
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                    ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    clientEmail = rs.getString("email");
                }
            }

            if (clientEmail != null) {
                System.out.println("Found Client email: " + clientEmail);
                try {
                    User client = DAOFactoryAbstract.getInstance().getUserDAO().getUserByEmail(clientEmail);
                    System.out.println("User Role Class: " + client.getRole().getClass().getName());
                    Cliente c = client.getRole().getClienteRole();
                    System.out.println("SUCCESS: getClienteRole() returned " + c);
                } catch (MissingAuthorizationException e) {
                    System.out.println("FAILURE: getClienteRole() threw MissingAuthorizationException");
                } catch (Exception e) {
                    System.out.println("FAILURE Client: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("No Client user found in DB. Please create one to test.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
