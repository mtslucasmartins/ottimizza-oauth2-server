package br.com.ottimizza.application.model.user_organization;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_organizations")
public class UserOrganization implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UserOrganizationID id;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_users_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(targetEntity = Organization.class)
    @JoinColumn(name = "fk_organizations_id", insertable = false, updatable = false)
    private Organization organization;

}