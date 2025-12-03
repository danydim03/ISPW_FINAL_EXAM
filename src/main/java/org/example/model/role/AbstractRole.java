package org.example.model.role;

import org.example.enums.ExceptionMessagesEnum;
import org.example.enums.UserRoleEnum;
import org.example.exceptions.MissingAuthorizationException;
import org.example.model.role.Cliente.Cliente;
import org.example.model.role.Kebabbaro.Kebabbaro;
//import it.uniroma2.dicii.ispw.gradely.model.role.secretary.Secretary;
//import it.uniroma2.dicii.ispw.gradely.model.role.student.Student;
import org.example.model.user.User;

public abstract class AbstractRole {
    protected User user;

    protected AbstractRole(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCodiceFiscale(){
        return user.getCodiceFiscale();
    }

    public Cliente getClienteRole() throws MissingAuthorizationException {
        throw new MissingAuthorizationException(ExceptionMessagesEnum.MISSING_AUTH.message);
    }

    public Kebabbaro getKebabbaroRole() throws MissingAuthorizationException {
        throw new MissingAuthorizationException(ExceptionMessagesEnum.MISSING_AUTH.message);
    }

    public Amministratore getAmministratoreRole() throws MissingAuthorizationException {
        throw new MissingAuthorizationException(ExceptionMessagesEnum.MISSING_AUTH.message);
    }

    public UserRoleEnum getRoleEnumType() throws MissingAuthorizationException {
        try {
            getClienteRole();
            return UserRoleEnum.CLIENTE;
        } catch (MissingAuthorizationException e) {
            try {
                getKebabbaroRole();
                return UserRoleEnum.PROFESSOR;
            } catch (MissingAuthorizationException ex) {
                getAmministratoreRole();
                return UserRoleEnum.AMMINISTRATORE;
            }
        }
    }
}
