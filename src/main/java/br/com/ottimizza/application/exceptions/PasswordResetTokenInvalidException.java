package br.com.ottimizza.application.exceptions;

public class PasswordResetTokenInvalidException extends Exception {

    static final long serialVersionUID = 1L;

    public PasswordResetTokenInvalidException(String errorMessage) {
        super(errorMessage);
    }

}
