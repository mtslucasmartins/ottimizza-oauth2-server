package br.com.ottimizza.application.exceptions;

public class UserAlreadyRegisteredException extends Exception {

    static final long serialVersionUID = 1L;

    public UserAlreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }

}
