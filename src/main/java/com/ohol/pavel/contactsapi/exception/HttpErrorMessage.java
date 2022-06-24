package com.ohol.pavel.contactsapi.exception;

import lombok.*;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

import static java.util.Objects.nonNull;

/**
 * Contains additional error information for errors happened
 * during HTTP request processing.
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class HttpErrorMessage extends ErrorMessage {

    /**
     * Http status of error.
     */
    private HttpStatus httpStatus;

    /**
     * Common internal error code.
     */
    private Integer internalCode;

    /**
     * Request marker designed to be able to watch to request flow.
     */
    private String requestId;

    /**
     * Mnemonic description of request.
     */
    private String requestDescription;

    /**
     * Http method from which error thrown.
     */
    private HttpMethod httpMethod;

    /**
     * Error message happened.
     */
    private String message;

    /**
     * Lombok based constructor.
     * @param reason error description.
     * @param severity severity of error.
     * @param payload any error payload.
     * @param originError origin exception.
     * @param timestamp timestamp when error happened.
     * @param throwable origin exception.
     * @param httpStatus http response status.
     * @param requestId request id to flow watch.
     * @param requestDescription request description.
     * @param httpMethod http method throwed exception.
     */
    @Builder(builderMethodName = "httpErrorBuilder")
    public HttpErrorMessage(final ErrorDescription reason,
                            final ErrorSeverity severity,
                            final Object payload,
                            final String originError,
                            final OffsetDateTime timestamp,
                            final Throwable throwable,
                            final HttpStatus httpStatus,
                            final String requestId,
                            final String requestDescription,
                            final HttpMethod httpMethod) {
        super(reason, severity, payload, originError, throwable, timestamp);
        this.httpStatus = httpStatus;
        this.internalCode = reason.getCode();
        this.requestId = requestId;
        this.requestDescription = requestDescription;
        this.httpMethod = httpMethod;
        this.message = reason.getMessage();
    }

    /**
     * Converts given {@link ErrorMessage} error message
     * to {@link HttpErrorMessage}.
     * @param errorMessage {@link ErrorMessage} error message to convert.
     * @return instance of {@link HttpErrorMessage}.
     */
    public static HttpErrorMessage fromErrorMessage(
            final ErrorMessage errorMessage) {
        return HttpErrorMessage.httpErrorBuilder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason(nonNull(errorMessage.getReason()) ? errorMessage.getReason() : null)
                .severity(nonNull(errorMessage.getSeverity()) ? errorMessage.getSeverity() : null)
                .payload(nonNull(errorMessage.getPayload()) ? errorMessage.getReason() : null)
                .originError(nonNull(errorMessage.getOriginError()) ? errorMessage.getOriginError() : null)
                .throwable(nonNull(errorMessage.getOriginException()) ? errorMessage.getOriginException() : null)
                .timestamp(nonNull(errorMessage.getTimestamp()) ? errorMessage.getTimestamp() : null)
                .build();
    }
}
