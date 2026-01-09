package org.example.beans_general;

import java.util.regex.Pattern;

import org.example.enums.UserRoleEnum;
import org.example.exceptions.ValidationException;

/**
 * Bean per il trasporto dei dati utente tra Boundary e Control.
 * 
 * Include validazione sintattica nei setter (Fail Fast principle).
 */
public class UserBean {

    // Pattern semplice per validazione email sintattica
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private String name;
    private String surname;
    private String codiceFiscale;
    private String email;
    private UserRoleEnum role;
    private String matricola;

    public UserBean(String name, String surname, String codiceFiscale, String email, UserRoleEnum role,
            String matricola) {
        setName(name);
        setSurname(surname);
        setCodiceFiscale(codiceFiscale);
        setEmail(email);
        setRole(role);
        this.matricola = matricola;
    }

    public String getName() {
        return name;
    }

    /**
     * Imposta il nome utente.
     * 
     * @param name il nome (non può essere null o vuoto)
     * @throws ValidationException se il nome è null o vuoto
     */
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("Il nome non può essere vuoto");
        }
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    /**
     * Imposta il cognome utente.
     * 
     * @param surname il cognome (non può essere null o vuoto)
     * @throws ValidationException se il cognome è null o vuoto
     */
    public void setSurname(String surname) {
        if (surname == null || surname.trim().isEmpty()) {
            throw new ValidationException("Il cognome non può essere vuoto");
        }
        this.surname = surname;
    }

    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il codice fiscale.
     * 
     * @param codiceFiscale il codice fiscale (non può essere null o vuoto)
     * @throws ValidationException se il codice fiscale è null o vuoto
     */
    public void setCodiceFiscale(String codiceFiscale) {
        if (codiceFiscale == null || codiceFiscale.trim().isEmpty()) {
            throw new ValidationException("Il codice fiscale non può essere vuoto");
        }
        this.codiceFiscale = codiceFiscale;
    }

    public String getEmail() {
        return email;
    }

    /**
     * Imposta l'email utente.
     * 
     * @param email l'email (deve avere formato valido)
     * @throws ValidationException se l'email è null, vuota o ha formato non valido
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("L'email non può essere vuota");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new ValidationException("Formato email non valido: " + email);
        }
        this.email = email;
    }

    public Integer getRole() {
        return this.role.type;
    }

    public UserRoleEnum getRoleEnum() {
        return this.role;
    }

    /**
     * Imposta il ruolo utente.
     * 
     * @param role il ruolo (non può essere null)
     * @throws ValidationException se il ruolo è null
     */
    public void setRole(UserRoleEnum role) {
        if (role == null) {
            throw new ValidationException("Il ruolo utente non può essere null");
        }
        this.role = role;
    }

    public String getMatricola() {
        return matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }
}
