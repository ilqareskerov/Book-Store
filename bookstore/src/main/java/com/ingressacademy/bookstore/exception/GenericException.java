package com.ingressacademy.bookstore.exception;

import org.springframework.http.HttpStatus;

public class GenericException extends RuntimeException  {
    private HttpStatus httpStatus;
    private ErrorCode errorCode;
    private String errorMessage;

    public GenericException(HttpStatus httpStatus, ErrorCode errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public GenericException(HttpStatus httpStatus, ErrorCode errorCode) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public GenericException(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public GenericException(HttpStatus httpStatus, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorMessage = errorMessage;
    }

    public GenericException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
    public GenericException(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
