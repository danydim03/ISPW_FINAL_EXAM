package org.example.beans_general;

import org.example.exceptions.ValidationException;

/**
 * Bean per il trasporto dei dati di login tra Boundary e Control.
 * 
 * Include validazione sintattica (Fail Fast principle).
 */
public class LoginBean {

    private String tokenKey;
    private UserBean userBean;

    public LoginBean(String tokenKey, UserBean userBean) {
        setTokenKey(tokenKey);
        setUserBean(userBean);
    }

    public String getTokenKey() {
        return tokenKey;
    }

    /**
     * Imposta la chiave del token di sessione.
     * 
     * @param tokenKey la chiave del token (non può essere null o vuota)
     * @throws ValidationException se il token è null o vuoto
     */
    public void setTokenKey(String tokenKey) {
        if (tokenKey == null || tokenKey.trim().isEmpty()) {
            throw new ValidationException("Il token di sessione non può essere vuoto");
        }
        this.tokenKey = tokenKey;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * Imposta il bean utente.
     * 
     * @param userBean il bean utente (non può essere null)
     * @throws ValidationException se userBean è null
     */
    public void setUserBean(UserBean userBean) {
        if (userBean == null) {
            throw new ValidationException("UserBean non può essere null");
        }
        this.userBean = userBean;
    }
}
