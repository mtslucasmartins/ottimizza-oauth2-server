package br.com.ottimizza.application.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String firstName;

    @Getter
    @Setter
    private String lastName;

    @Getter
    @Setter
    private String avatar;

    @Getter
    @Setter
    private BigInteger organizationId;

    public static UserDTO fromEntity(User user) {
        // @formatter:off
        UserDTO dto = new UserDTO()
            .withUsername(user.getUsername())
            .withFirstName(user.getFirstName())
            .withLastName(user.getLastName())
            .withEmail(user.getEmail())
            .withAvatar(user.getAvatar())
            .withOrganizationId(user.getOrganization());
        // @formatter:on
        return dto;
    }

    public static List<UserDTO> fromEntities(List<User> users) {
        return users.stream().map(user -> fromEntity(user)).collect(Collectors.toList());
    }

    UserDTO withUsername(String username) {
        this.username = username;
        return this;
    }

    UserDTO withEmail(String email) {
        this.email = email;
        return this;
    }

    UserDTO withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    UserDTO withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    UserDTO withAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    UserDTO withOrganizationId(BigInteger organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    UserDTO withOrganizationId(Organization organization) {
        if (organization != null && organization.getId() != null) {
            this.organizationId = organization.getId();
        }
        return this;
    }

}
