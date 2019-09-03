package br.com.ottimizza.application.model.user_organization;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_organizations")
public class UserOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserOrganizationID id;

    @Getter
    @Setter
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_users_id", insertable = false, updatable = false)
    private User user;

    @Getter
    @Setter
    @ManyToOne(targetEntity = Organization.class)
    @JoinColumn(name = "fk_organizations_id", insertable = false, updatable = false)
    private Organization organization;

}