package com.ohol.pavel.contactsapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Pavel Ohol
 */
@AllArgsConstructor
@Getter
public enum ErrorDescription {

    BAD_REQUEST(0, "Bad request body"),

    USER_NOT_FOUND(1, "User not found"),

    JWT_NOT_VALID(2, "Jwt token is expired or invalid"),

    USER_ALREADY_EXIST(3, "User with current login already exist"),

    UNKNOWN_ERROR(4, "Unknown error"),

    AUTHENTICATION_ERROR(5, "Authentication error"),

    CONTACT_NOT_FOUND(6, "Contact not found")

    ;

    private final Integer code;

    private final String message;

}
