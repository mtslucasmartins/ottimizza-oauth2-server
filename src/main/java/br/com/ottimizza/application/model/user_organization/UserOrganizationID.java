package br.com.ottimizza.application.model.user_organization;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class UserOrganizationID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Column(name = "fk_users_id")
    private BigInteger userId;

    @Getter
    @Setter
    @Column(name = "fk_organizations_id")
    private BigInteger organizationId;

}