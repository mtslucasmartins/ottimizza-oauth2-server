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
import javax.persistence.SequenceGenerator;
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
    @Getter @Setter
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "users_organizations_invites_sequence", sequenceName = "users_organizations_invites_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_organizations_invites_sequence")
    private BigInteger id;

    @Getter
    @Setter
    @Column(name = "token", nullable = false)
    private String token;

    @Getter
    @Setter
    @Column(name = "email", nullable = false)
    private String email;

    @Getter
    @Setter
    @OneToOne(targetEntity = Organization.class)
    @JoinColumn(name = "fk_organizations_id", nullable = false)
    private Organization organization;

    @Getter
    @Setter
    @Column(name = "type")
    private Integer type;
    
    @Getter
    @Setter
    @Column(name = "authorities")
    private String authorities;

    @Getter
    @Setter
    @Column(name = "products")
    private String products;
}