package br.com.ottimizza.application.model.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import br.com.ottimizza.application.model.Authority;
import br.com.ottimizza.application.model.Organization;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;

@Entity
@Table(name = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter @Setter
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "users_sequence", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_sequence")
    private BigInteger id;

    @Getter @Setter
    @Column(name = "username", unique = true, updatable = false, nullable = false)
    private String username;

    @Getter @Setter
    @Column(name = "password")
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @Getter @Setter
    @Column(name = "email")
    private String email;

    @Getter @Setter
    @Column(name = "phone")
    private String phone;

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
    @Column(name = "type")
    private Integer type;

    @Getter @Setter
    @Column(name = "avatar")
    private String avatar; 

    @Getter @Setter
    @ManyToMany
    @JoinTable(
        name = "users_authorities", 
        joinColumns = @JoinColumn(name = "fk_users_id"), 
        inverseJoinColumns = @JoinColumn(name = "fk_authorities_id"))
    private Set<Authority> authorities;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "fk_accounting_id", nullable = true)
    private Organization organization;

    public static class Type {
        public static final Integer ACCOUNTANT = 1;
        public static final Integer CUSTOMER = 2;
    }

}