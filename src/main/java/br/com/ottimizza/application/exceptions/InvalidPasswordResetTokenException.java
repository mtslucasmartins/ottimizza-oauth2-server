package br.com.ottimizza.application.exceptions;

public class InvalidPasswordResetTokenException extends Exception {

    static final long serialVersionUID = 1L;

    public InvalidPasswordResetTokenException(String errorMessage) {
        super(errorMessage);
    }

}
