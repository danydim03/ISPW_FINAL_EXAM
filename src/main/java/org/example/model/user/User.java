package org.example.model.user;

import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.WrongPasswordException;
import org.example.model.role.AbstractRole;

import java.time.LocalDate;

public class User {
    private String id; // ID del database (es. CLI001)
    private String name;
    private String surname;
    private String codiceFiscale;
    private String email;
    private String password;
    private LocalDate registrationDate;
    private AbstractRole role;

    public User(String name, String surname, String codiceFiscale, String email, String password,
            LocalDate registrationDate) {
        this.name = name;
        this.surname = surname;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }


    public String getCodiceFiscale() {
        return codiceFiscale;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AbstractRole getRole() {
        return role;
    }

    public void setRole(AbstractRole role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }


    public Boolean checkPassword(String password) throws WrongPasswordException {
        if (this.password.equals(password))
            return true;
        else
            throw new WrongPasswordException(ExceptionMessagesEnum.WRONG_PASSWORD.message);
    }

    public void changePassword(String oldPass, String newPass) throws WrongPasswordException {
        if (this.password.equals(oldPass)) {
            this.password = newPass;
        } else
            throw new WrongPasswordException(ExceptionMessagesEnum.WRONG_PASSWORD.message);
    }

    public String getPassword() {
        return this.password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (!(obj instanceof User user))
            return false;

        return user.getRegistrationDate().equals(registrationDate)
                && user.getEmail().equals(email)
                && user.getName().equals(name)
                && user.getSurname().equals(surname)
                && user.getCodiceFiscale().equals(codiceFiscale)
                && user.getPassword().equals(password);
    }
}
