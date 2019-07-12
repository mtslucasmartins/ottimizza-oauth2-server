package br.com.ottimizza.application.exceptions;

public class OrganizationAlreadyRegisteredException extends Exception {

    static final long serialVersionUID = 1L;

    public OrganizationAlreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }

}
