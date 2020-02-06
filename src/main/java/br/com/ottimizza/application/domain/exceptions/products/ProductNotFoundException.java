package br.com.ottimizza.application.domain.exceptions.products;

public class ProductNotFoundException extends Exception {

    static final long serialVersionUID = 1L;

    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
