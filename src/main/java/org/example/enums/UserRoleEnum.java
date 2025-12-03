package org.example.enums;

import org.example.exceptions.UnrecognizedRoleException;

public enum UserRoleEnum {

    CLIENTE(1),

    KEBABBARO(2),

    AMMINISTRATORE(3);

    public final int type;

    UserRoleEnum(int type) {
        this.type = type;
    }

    public static UserRoleEnum getUserRoleByType(int type) throws UnrecognizedRoleException {
        for (UserRoleEnum userRole : values())
            if (userRole.type == type)
                return userRole;
        throw new UnrecognizedRoleException(ExceptionMessagesEnum.UNRECOGNIZED_ROLE.message);
    }
}
