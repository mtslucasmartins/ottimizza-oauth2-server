package br.com.ottimizza.application.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import br.com.ottimizza.application.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @formatter:off
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO implements Serializable {

    static final long serialVersionUID = 1L;
    
    @Getter @Setter
    private BigInteger id;

    @Getter @Setter
    private String externalId;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private Integer type;

    @Getter @Setter
    private String cnpj;

    @Getter @Setter
    private String codigoERP;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String avatar;

    @Getter @Setter
    private BigInteger organizationId;

    public Organization toEntity() {
        Organization organization = new Organization();

        Organization accounting = new Organization();
        accounting.setId(this.organizationId);

        organization.setId(this.id);
        organization.setExternalId(this.externalId);
        organization.setName(this.name);
        organization.setCnpj(this.cnpj);
        organization.setEmail(this.email);
        organization.setCodigoERP(this.codigoERP);
        organization.setAvatar(this.avatar);
        organization.setOrganization(accounting);

        return organization;
    }

    public static OrganizationDTO fromEntity(Organization organization) {
        // @formatter:off
        OrganizationDTO dto = new OrganizationDTO()
            .withId(organization.getId())
            .withExternalId(organization.getExternalId())
            .withName(organization.getName())
            .withCnpj(organization.getCnpj())
            .withCodigoERP(organization.getCodigoERP())
            .withEmail(organization.getEmail())
            .withAvatar(organization.getAvatar())
            .withOrganizationId(organization.getOrganization());
        // @formatter:on
        return dto;
    }

    public static List<OrganizationDTO> fromEntities(List<Organization> organizations) {
        return organizations.stream().map(organization -> fromEntity(organization)).collect(Collectors.toList());
    }

    public Organization patch(Organization organization) {
        if (this.name != null && !this.name.equals(""))
            organization.setName(this.name);

        if (this.cnpj != null && !this.cnpj.equals(""))
            organization.setCnpj(this.cnpj);

        if (this.codigoERP != null && !this.codigoERP.equals(""))
            organization.setCodigoERP(this.codigoERP);

        if (this.avatar != null && !this.avatar.equals(""))
            organization.setAvatar(this.avatar);

        if (this.email != null && !this.email.equals(""))
            organization.setEmail(this.email);

        return organization;
    }

    OrganizationDTO withId(BigInteger id) {
        this.id = id;
        return this;
    }

    OrganizationDTO withExternalId(String externalId) {
        this.externalId = externalId;
        return this;
    }

    OrganizationDTO withName(String name) {
        this.name = name;
        return this;
    }

    OrganizationDTO withType(Integer type) {
        this.type = type;
        return this;
    }

    OrganizationDTO withCnpj(String cnpj) {
        this.cnpj = cnpj;
        return this;
    }

    OrganizationDTO withCodigoERP(String codigoERP) {
        this.codigoERP = codigoERP;
        return this;
    }

    OrganizationDTO withEmail(String email) {
        this.email = email;
        return this;
    }

    OrganizationDTO withAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    OrganizationDTO withOrganizationId(BigInteger organizationId) {
        this.organizationId = organizationId;
        return this;
    }

    OrganizationDTO withOrganizationId(Organization organization) {
        if (organization != null && organization.getId() != null) {
            this.organizationId = organization.getId();
        }
        return this;
    }

}
