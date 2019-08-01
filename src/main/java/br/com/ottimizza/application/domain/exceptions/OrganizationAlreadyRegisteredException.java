package br.com.ottimizza.application.domain.exceptions;

public class OrganizationAlreadyRegisteredException extends Exception {

    static final long serialVersionUID = 1L;

    public OrganizationAlreadyRegisteredException(String errorMessage) {
        super(errorMessage);
    }

}
