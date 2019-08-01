package br.com.ottimizza.application.domain;

public enum Authorities { // @formatter:off

    ADMIN("ADMIN"),
    CUSTOMER_READ("CUSTOMER_READ"),
    CUSTOMER_WRITE("CUSTOMER_WRITE"),
    ACCOUNTANT_READ("ACCOUNTANT_READ"),
    ACCOUNTANT_WRITE("ACCOUNTANT_WRITE"),
    ACCOUNTANT_ADMIN("ACCOUNTANT_ADMIN");

    private String name;

    private Authorities(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
