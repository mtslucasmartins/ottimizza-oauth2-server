package br.com.ottimizza.application.model.user_organization;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserOrganizationID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "fk_users_id")
    private BigInteger userId;

    @Column(name = "fk_organizations_id")
    private BigInteger organizationId;

}