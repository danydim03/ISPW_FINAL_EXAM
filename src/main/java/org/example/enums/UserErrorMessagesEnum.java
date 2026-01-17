package org.example.enums;

public enum UserErrorMessagesEnum {

    /* === TITLES === */
    ROLE_ERROR_TITLE("Role error"),
    LOGIN_ERROR_TITLE("Login error"),
    DATA_RETRIEVAL_TITLE("Data retrieval error."),
    AUTHORIZATION_TITLE("Authorization error"),
    EXTERNAL_ERROR_TITLE("External error"),
    RESOURCE_LOADING_TITLE("Resource loading error"),
    PROPERTY_VALUE_TITLE("Property value error"),
    MISSING_VALUE_TITLE("Missing value"),
    OBJ_NOT_FOUND_TITLE("Not found"),
    OUT_OF_BOUND_TITLE("Out of bound"),

    /* === MESSAGES === */
    ROLE_ERROR_MSG("The role associated with this user is not recognized."),
    WRONG_PASSWORD_MSG("Wrong password."),
    USER_NOT_FOUND_MSG("The user associated with this credentials does not exist."),
    MALFORMED_EMAIL_MSG("Please insert an email with the correct format."),
    MISSING_AUTHORIZATION_MSG("No role is associated with this user."),
    DATA_RETRIEVAL_MSG("An error occurred while retrieving user data."),
    RESOURCE_LOADING_MSG("An error occurred while loading view resource."),
    PROPERTY_VALUE_MSG("Unexpected value of property name. Please check your configuration file."),
    OBJ_NOT_FOUND_MSG("The requested object does not exist."),
    NO_ENROLLMENTS_MSG("There are no enrollments for this exam."),
    OUT_OF_BOUND_MSG("Index out of bound")
    ;

    public final String message;

    UserErrorMessagesEnum(String message) {
        this.message = message;
    }
}
