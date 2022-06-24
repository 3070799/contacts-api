package com.ohol.pavel.contactsapi.exception;

import com.ohol.pavel.contactsapi.json.DateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;

/**
 * Error message container.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ErrorMessage {

    /**
     * Described a reason of error.
     */
    private ErrorDescription reason;

    /**
     * Error severity.
     */
    @Builder.Default
    private ErrorSeverity severity = ErrorSeverity.ERROR;

    /**
     * Contains any error payload.
     */
    private Object payload;

    /**
     * Origin error message.
     * Useful in catch blocks.
     */
    private String originError;

    /**
     * Origin error exception.
     * Useful in catch blocks.
     */
    private Throwable originException;

    /**
     * Time when error happened;
     */
    @Builder.Default
    @JsonSerialize(using = DateTimeSerializer.class)
    private OffsetDateTime timestamp = OffsetDateTime.now();
}
