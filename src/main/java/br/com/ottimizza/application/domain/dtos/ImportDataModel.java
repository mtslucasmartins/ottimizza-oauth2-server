package br.com.ottimizza.application.domain.dtos;

import java.io.Serializable;
import lombok.Data;

@Data // @formatter:off
public class ImportDataModel implements Serializable {

    static final long serialVersionUID = 1L;

    private String email;

    private String firstName;
   
    // 
    private String organizationName;

    private String organizationCnpj;

    private String organizationCode;
    
    //
    private String accountingName;

    private String accountingCnpj;

    ImportDataModel withEmail(String email) {
        this.email = email; return this;
    }
    ImportDataModel withFirstName(String firstName) {
        this.firstName = firstName; return this;
    }

    ImportDataModel withOrganizationName(String organizationName) {
        this.organizationName = organizationName; return this;
    }
    ImportDataModel withOrganizationCnpj(String organizationCnpj) {
        this.organizationCnpj = organizationCnpj; return this;
    }
    ImportDataModel withOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode; return this;
    }

    ImportDataModel withAccountingName(String accountingName) {
        this.accountingName = accountingName; return this;
    }
    ImportDataModel withAccountingCnpj(String accountingCnpj) {
        this.accountingCnpj = accountingCnpj; return this;
    }

}
