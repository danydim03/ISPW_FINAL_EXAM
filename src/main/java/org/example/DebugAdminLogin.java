package org.example;

import org.example.dao_manager.DAOFactoryAbstract;
import org.example.model.user.User;
import org.example.model.role.Amministratore.Amministratore;

public class DebugAdminLogin {
    public static void main(String[] args) {
        try {
            System.out.println("DEBUG: Starting Admin Login Debug...");

            String email = "admin@ispw.it";
            System.out.println("DEBUG: Fetching user by email: " + email);

            User user = DAOFactoryAbstract.getInstance().getUserDAO().getUserByEmail(email);
            System.out.println(
                    "DEBUG: User found: " + user.getName() + " " + user.getSurname() + " (ID: " + user.getId() + ")");

            System.out.println("DEBUG: User Role Type: " + user.getRole().getRoleEnumType());

            if (user.getRole() instanceof Amministratore) {
                Amministratore admin = (Amministratore) user.getRole();
                System.out.println("DEBUG: Admin Role loaded successfully. Dept: " + admin.getDepartment());
            } else {
                System.out.println("DEBUG: Role is NOT Amministratore instance!");
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Exception caught!");
            e.printStackTrace();
        }
    }
}
