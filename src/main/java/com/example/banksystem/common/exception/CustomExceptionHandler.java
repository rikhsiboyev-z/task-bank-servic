package com.example.banksystem.common.exception;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {
    public CustomErrorResponse buildErrorResponse(String message, HttpStatus status) {
        return buildErrorResponse(message, status, null);
    }

    public CustomErrorResponse buildErrorResponse(Map<String, Object> errors, HttpStatus status) {
        return buildErrorResponse(null, status, errors);
    }

    public CustomErrorResponse buildErrorResponse(String message, HttpStatus status, Map<String, Object> errors) {
        return new CustomErrorResponse(message, status, errors, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    public CustomErrorResponse handleExceptions(Exception e) {
        log.error(e.getMessage(), e);
        return buildErrorResponse("Something is wrong, please repeat later", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionSystemException(TransactionSystemException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CustomErrorResponse.builder()
                        .message("Баланс не может быть отрицательным")
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleEntityNotFoundException(EntityNotFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);

        String message = e.getCause() == null ? e.getMessage() : e.getCause().getMessage();
        message = e.getCause().getCause() == null ? message : e.getCause().getCause().getMessage();
        message = e.getCause().getCause().getCause() == null ? message : e.getCause().getCause().getCause().getMessage();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(buildErrorResponse(message, HttpStatus.CONFLICT));
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleNoResourceFoundException(NoResourceFoundException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CustomErrorResponse.builder()
                        .message("Wrong swagger-url entered")
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND)
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CustomErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(CustomErrorResponse.builder()
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.FORBIDDEN)
                        .build());
    }


    @ExceptionHandler(CustomExceptionThisUsernameOlReadyTaken.class)
    public ResponseEntity<CustomErrorResponse> handleSmsVerificationException(CustomExceptionThisUsernameOlReadyTaken e) {
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CustomErrorResponse.builder()
                        .message(e.getMessage())
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
    }

}