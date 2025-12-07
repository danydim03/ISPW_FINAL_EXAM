package org.example.use_cases.login;

import org.example.beans_general.LoginBean;
import org.example.beans_general.UserBean;
import org.example.enums.ExceptionMessagesEnum;
import org.example.exceptions.*;
import org.example.model.user.User;
import org.example.model.user.UserLazyFactory;
import org.example.session_manager.SessionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginControl {
    public LoginBean login(String email, String password) throws UserNotFoundException, WrongPasswordException, DAOException, MissingAuthorizationException, PropertyException, ResourceNotFoundException, UnrecognizedRoleException, WrongListQueryIdentifierValue, ObjectNotFoundException {
        User user = UserLazyFactory.getInstance().getUserByEmail(email);
        user.checkPassword(password);
        String ID;
        switch (user.getRole().getRoleEnumType()) {
            case CLIENTE -> ID = user.getRole().getClienteRole().getID();
            case KEBABBARO -> ID = user.getRole().getKebabbaroRole().getID();
            case AMMINISTRATORE -> ID = user.getRole().getAmministratoreRole().getID();
            default -> ID = "";
        }
        return new LoginBean(
                SessionManager.getInstance().getSessionTokenKeyByUser(user),
                new UserBean(
                        user.getName(),
                        user.getSurname(),
                        user.getID(),
                        user.getEmail(),
                        user.getRole().getRoleEnumType(),
                        ID
                )
        );
    }

    public void emailMatches(String email) throws EmailFormatException {
        final Pattern emailPattern = Pattern.compile("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+){0,63}@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+){0,63}(\\.[A-Za-z]{2,})");
        final Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches())
            throw new EmailFormatException(ExceptionMessagesEnum.EMAIL_FORMAT.message);
    }
}
