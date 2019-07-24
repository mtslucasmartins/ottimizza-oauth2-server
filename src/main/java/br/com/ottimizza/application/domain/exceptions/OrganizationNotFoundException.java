package br.com.ottimizza.application.domain.exceptions;

public class OrganizationNotFoundException extends Exception {

    static final long serialVersionUID = 1L;

    public OrganizationNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
