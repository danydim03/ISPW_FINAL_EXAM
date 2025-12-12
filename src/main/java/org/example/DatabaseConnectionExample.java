package org.example;

import java.sql.*;

public class DatabaseConnectionExample {

    public static void main(String[] args) {
        // Parametri di connessione
        String url = "jdbc:mysql://localhost:3306/ispwtwo";
        String username = "root";
        String password = "rootroot";

        String query = "SELECT ID, name, surname, codice_fiscale, email, password, registration_date, role FROM USER WHERE email = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Imposta il parametro della query
            statement.setString(1, "mario.rossi@example.com");

            // Esegui la query
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String id = rs.getString("ID");
                    String name = rs.getString("name");
                    String surname = rs.getString("surname");
                    String codiceFiscale = rs.getString("codice_fiscale");
                    String email = rs.getString("email");
                    String pwd = rs.getString("password");
                    Date registrationDate = rs.getDate("registration_date");
                    int role = rs.getInt("role");

                    System.out.println("ID: " + id);
                    System.out.println("Nome: " + name);
                    System.out.println("Cognome: " + surname);
                    System.out.println("Codice Fiscale: " + codiceFiscale);
                    System.out.println("Email: " + email);
                    System.out.println("Password: " + pwd);
                    System.out.println("Data Registrazione: " + registrationDate);
                    System.out.println("Ruolo: " + role);
                }
            }

        } catch (SQLException e) {
            System.err.println("Errore connessione DB: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
