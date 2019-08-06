package br.com.ottimizza.application.model.user;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.ottimizza.application.model.Authority;
import br.com.ottimizza.application.model.Organization;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter @Setter
    @Column(name = "username", updatable = false, nullable = false)
    private String username;

    @Getter @Setter
    @Column(name = "password")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Getter @Setter
    @Column(name = "email")
    private String email;

    @Getter @Setter
    @Column(name = "first_name")
    private String firstName;

    @Getter @Setter
    @Column(name = "last_name")
    private String lastName;

    @Getter @Setter
    @Column(name = "activated")
    private boolean activated;

    @Getter @Setter
    @Column(name = "activationkey")
    private String activationKey; 

    @Getter @Setter
    @Column(name = "avatar")
    private String avatar; 

    @Getter @Setter
    @ManyToMany
    @JoinTable(
        name = "user_authority", 
        joinColumns = @JoinColumn(name = "username"), 
        inverseJoinColumns = @JoinColumn(name = "authority"))
    private Set<Authority> authorities;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "fk_accounting_id", nullable = true)
    private Organization organization;

}