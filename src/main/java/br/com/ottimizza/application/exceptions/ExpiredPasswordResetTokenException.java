package br.com.ottimizza.application.exceptions;

public class ExpiredPasswordResetTokenException extends Exception {

    static final long serialVersionUID = 1L;

    public ExpiredPasswordResetTokenException(String errorMessage) {
        super(errorMessage);
    }

}
