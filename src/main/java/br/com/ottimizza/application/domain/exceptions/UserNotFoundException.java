package br.com.ottimizza.application.domain.exceptions;

public class UserNotFoundException extends Exception {

    static final long serialVersionUID = 1L;

    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
