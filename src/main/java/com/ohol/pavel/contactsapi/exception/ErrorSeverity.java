package com.ohol.pavel.contactsapi.exception;

/**
 * Converts error severity to mnemonic interpretations.
 */
public enum ErrorSeverity {

    /**
     * Notification style.
     */
    WARNING,
    /**
     * Usual errors.
     */
    ERROR,
    /**
     * Critical errors, system is not able to work anymore.
     */
    FATAL
}
