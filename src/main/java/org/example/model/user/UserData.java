package org.example.model.user;

public class UserData {

    private String userName;
    private String userSurname;
    private String userEmail;
    private String userCodiceFiscale;
    private Integer userRole;
    private String sessionTokenKey;

    public UserData(String userName, String userSurname, String userEmail, String userCodiceFiscale, Integer userRole,
            String sessionTokenKey) {
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userCodiceFiscale = userCodiceFiscale;
        this.userRole = userRole;
        this.sessionTokenKey = sessionTokenKey;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSurname() {
        return userSurname;
    }

    public void setUserSurname(String userSurname) {
        this.userSurname = userSurname;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserCodiceFiscale() {
        return userCodiceFiscale;
    }

    public void setUserCodiceFiscale(String userCodiceFiscale) {
        this.userCodiceFiscale = userCodiceFiscale;
    }

    public Integer getUserRole() {
        return userRole;
    }

    public void setUserRole(Integer userRole) {
        this.userRole = userRole;
    }

    public String getSessionTokenKey() {
        return sessionTokenKey;
    }

    public void setSessionTokenKey(String sessionTokenKey) {
        this.sessionTokenKey = sessionTokenKey;
    }
}