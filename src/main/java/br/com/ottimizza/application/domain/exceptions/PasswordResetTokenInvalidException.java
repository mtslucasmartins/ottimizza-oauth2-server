package br.com.ottimizza.application.domain.exceptions;

public class PasswordResetTokenInvalidException extends Exception {

    static final long serialVersionUID = 1L;

    public PasswordResetTokenInvalidException(String errorMessage) {
        super(errorMessage);
    }

}
