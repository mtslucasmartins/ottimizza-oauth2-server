package br.com.ottimizza.application.domain.exceptions;

public class PasswordResetTokenExpiredException extends Exception {

    static final long serialVersionUID = 1L;

    public PasswordResetTokenExpiredException(String errorMessage) {
        super(errorMessage);
    }

}
