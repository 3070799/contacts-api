package com.ohol.pavel.contactsapi.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Supplier;

/**
 * Basic class for all application level exceptions.
 */
@Getter
@Setter
@Builder
public class ApplicationException extends RuntimeException {

    /**
     * Contains additional information about error happened.
     */
    private final ErrorMessage errorMessage;

    /**
     * Accepts {@link ErrorMessage} as error information.
     * @param anErrorMessage {@link ErrorMessage}
     */
    public ApplicationException(final ErrorMessage anErrorMessage) {
        super(anErrorMessage.getReason().getMessage());
        errorMessage = anErrorMessage;
    }

    /**
     * Overrided to show error message from the error container.
     * @return error message string representation.
     */
    @Override
    public String toString() {
        return errorMessage.toString();
    }
}
