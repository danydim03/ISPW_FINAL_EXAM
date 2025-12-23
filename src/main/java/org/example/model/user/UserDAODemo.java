package org.example.model.user;

import org.example.enums.ExceptionMessagesEnum; // Fixed: Added import
import org.example.exceptions.*;
import org.example.model.role.Amministratore.AmministratoreDAODemo;
import org.example.model.role.Cliente.ClienteDAODemo;
import org.example.model.role.Kebabbaro.KebabbaroDAODemo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDAODemo implements UserDAOInterface {

    private static final List<User> MOCK_USERS = new ArrayList<>();

    static {
        try {
            // Client User
            User client = new User("Daniele", "DiMeo", "Daniele", "cliente@gmail.com", "cliente", LocalDate.now());
            client.setId("CLI001");
            client.setRole(new ClienteDAODemo().getClienteByUser(client));
            MOCK_USERS.add(client);

            // Admin User
            User admin = new User("Admin", "System", "Admin", "admin@gmail.com", "admin", LocalDate.now());
            admin.setId("ADM001");
            admin.setRole(new AmministratoreDAODemo().getAmministratoreByUser(admin));
            MOCK_USERS.add(admin);

            // Kebabbaro User
            User kebabbaro = new User("Kebab", "Man", "Kebabbaro", "kebabbaro@gmail.com", "kebabbaro", LocalDate.now());
            kebabbaro.setId("KEB001");
            kebabbaro.setRole(new KebabbaroDAODemo().getKebabbaroByUser(kebabbaro));
            MOCK_USERS.add(kebabbaro);

            System.out.println("[DEMO MODE] Loaded " + MOCK_USERS.size() + " mock users into memory.");
            MOCK_USERS.forEach(u -> System.out.println(" - User: " + u.getEmail()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        return MOCK_USERS.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessagesEnum.USER_NOT_FOUND.message));
    }

    @Override
    public User getUserByCodiceFiscale(String codiceFiscale) throws UserNotFoundException {
        return MOCK_USERS.stream()
                .filter(u -> u.getCodiceFiscale().equalsIgnoreCase(codiceFiscale))
                .findFirst()
                .orElseThrow(() -> new UserNotFoundException(ExceptionMessagesEnum.USER_NOT_FOUND.message));
    }

    @Override
    public void insert(User user) {
        MOCK_USERS.add(user);
    }

    @Override
    public void delete(User user) {
        MOCK_USERS.remove(user);
    }

    @Override
    public void update(User user) {
        // Automatic
    }
}
