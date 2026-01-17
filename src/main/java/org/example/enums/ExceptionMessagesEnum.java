package org.example.enums;

public enum ExceptionMessagesEnum {
    HOMEPAGE_LOAD_ERROR("Error while loading Homepage view"),
    BEAN_FORMAT("Incorrect bean argument syntax"),
    DAO("An error occurred during data retrieval from the persistence layer"),
    EMAIL_FORMAT("Email address provided doesn't match with correct email format"),
    MISSING_AUTH("User doesn't have the authorization to execute the requested action"),
    OBJ_NOT_FOUND("Object requested is not present in the persistence strate"),
    USER_NOT_FOUND("No user has been found with this credentials"),
    CLIENT_NOT_FOUND("No cliente has been found with this credentials"),
    KEBABBARO_NOT_FOUND("No kebabbaro has been found with this credentials"),
    AMMINISTRATORE_NOT_FOUND("No amministratore has been found with this credentials"),
    WRONG_PASSWORD("Inserted email or password is incorrect"),
    UNEXPECTED_PROPERTY_NAME("Unexpected property name"),
    RESOURCE_NOT_FOUND("The requested resource has not been found"),
    UNRECOGNIZED_ROLE("Unrecognized user role value"),
    NUMBERS_DONT_MATCH("id and values number don't match"),
    ;

    public final String message;

    private ExceptionMessagesEnum(String message) {
        this.message = message;
    }
}
