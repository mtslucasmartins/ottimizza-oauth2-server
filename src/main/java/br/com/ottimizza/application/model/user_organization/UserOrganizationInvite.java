package br.com.ottimizza.application.model.user_organization;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.ottimizza.application.model.Organization;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_organizations_invites")
public class UserOrganizationInvite implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @Getter
    @Setter
    @Column(name = "token")
    private String token;

    @Getter
    @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Getter
    @Setter
    @OneToOne(targetEntity = Organization.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_organizations_id", nullable = false)
    private Organization organization;

}