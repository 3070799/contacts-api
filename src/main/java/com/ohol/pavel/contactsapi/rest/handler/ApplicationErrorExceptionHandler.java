package com.ohol.pavel.contactsapi.rest.handler;

import com.ohol.pavel.contactsapi.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.ohol.pavel.contactsapi.exception.ErrorDescription.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Pavel Ohol
 */
@ControllerAdvice
public class ApplicationErrorExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Logger instance.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(
            ApplicationErrorExceptionHandler.class
    );

    private static final Map<ErrorDescription, HttpStatus>
        ERROR_MAP = new HashMap<>() {
        {
            put(USER_NOT_FOUND, NOT_FOUND);
            put(JWT_NOT_VALID, UNAUTHORIZED);
            put(USER_ALREADY_EXIST, BAD_REQUEST);
        }
    };

    /**
     * Handles all custom {@link ApplicationException} based errors.
     * @param applicationException instance of {@link ApplicationException}.
     * @param request request.
     * @return http response entity.
     */
    @ExceptionHandler(ApplicationException.class)
    public final ResponseEntity<HttpErrorMessage> handleApplicationError(
            final ApplicationException applicationException,
            final WebRequest request) {

        ErrorMessage errorMessage = applicationException.getErrorMessage();
        HttpErrorMessage httpErrorMessage = HttpErrorMessage.
                fromErrorMessage(errorMessage);

        httpErrorMessage.setHttpStatus(ERROR_MAP.getOrDefault(
                errorMessage.getReason(),
                INTERNAL_SERVER_ERROR)
        );

        httpErrorMessage.setRequestId(obtainRequestId(request));
        httpErrorMessage.setHttpMethod(((ServletWebRequest) request)
                .getHttpMethod());
        httpErrorMessage.setRequestDescription(request.
                getDescription(true));

        LOGGER.error(httpErrorMessage.toString());
        return new ResponseEntity<>(httpErrorMessage,
                httpErrorMessage.getHttpStatus());
    }

    /**
     * Handles any authentication errors.
     * @param e {@link BadCredentialsException} error.
     * @param request request.
     * @return http response entity.
     */
    @ExceptionHandler({
            BadCredentialsException.class,
            LockedException.class,
            AccessDeniedException.class
    })
    public final ResponseEntity<HttpErrorMessage> authenticationError(
            final Exception e,
            final WebRequest request) {

        HttpErrorMessage httpErrorMessage = HttpErrorMessage
                .httpErrorBuilder()
                .httpStatus(UNAUTHORIZED)
                .reason(ErrorDescription.AUTHENTICATION_ERROR)
                .severity(ErrorSeverity.ERROR)
                .originError(e.getMessage())
                .timestamp(OffsetDateTime.now())
                .httpMethod(((ServletWebRequest) request).getHttpMethod())
                .requestDescription(request.getDescription(true))
                .requestId(obtainRequestId(request))
                .build();

        return new ResponseEntity<>(httpErrorMessage,
                httpErrorMessage.getHttpStatus());
    }

    /**
     * Handles all {@link ConversionFailedException} based errors.
     * @param exception instance of {@link ConversionFailedException}.
     * @param request request.
     * @return http response entity.
     */
    @ExceptionHandler(ConversionFailedException.class)
    public final ResponseEntity<HttpErrorMessage> handleConversionError(
            final ConversionFailedException exception,
            final WebRequest request) {

        if (exception.getCause() instanceof ApplicationException) {
            return handleApplicationError(
                    (ApplicationException) exception.getCause(),
                    request);
        } else {
            return anyError(exception, request);
        }
    }

    /**
     * Handles any throwable errors.
     * @param e any throwable error.
     * @param request request.
     * @return http response entity.
     */
    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<HttpErrorMessage> anyError(
            final Throwable e,
            final WebRequest request) {

        HttpErrorMessage httpErrorMessage = HttpErrorMessage
                .httpErrorBuilder()
                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                .reason(ErrorDescription.UNKNOWN_ERROR)
                .severity(ErrorSeverity.ERROR)
                .originError(e.getMessage())
                .timestamp(OffsetDateTime.now())
                .httpMethod(((ServletWebRequest) request).getHttpMethod())
                .requestDescription(request.getDescription(true))
                .requestId(obtainRequestId(request))
                .build();

        LOGGER.error(httpErrorMessage.toString(), e);
        return new ResponseEntity<>(httpErrorMessage,
                httpErrorMessage.getHttpStatus());
    }


    /**
     * Extracts requestId from given request.
     * @param request {@link WebRequest} request to extract request id.
     * @return requestId.
     */
    private static String obtainRequestId(final WebRequest request) {
        Object attribute = request.getAttribute("requestId",
                RequestAttributes.SCOPE_REQUEST);
        return attribute == null ? null : attribute.toString();
    }
}
