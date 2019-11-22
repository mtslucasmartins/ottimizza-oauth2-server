package br.com.ottimizza.application.domain.dtos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import br.com.ottimizza.application.domain.dtos.criterias.SearchCriteria;
import br.com.ottimizza.application.model.Organization;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// @formatter:off
@Builder
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

    public Organization toEntity(boolean removeNonDigitsFromCNPJ) {
        Organization organization = new Organization();
        
        if (removeNonDigitsFromCNPJ && this.cnpj != null) 
            this.cnpj = this.cnpj.replaceAll("\\D", "");

        organization.setId(this.id);
        organization.setExternalId(this.externalId);
        organization.setName(this.name);
        organization.setCnpj(this.cnpj);
        organization.setEmail(this.email);
        organization.setType(this.type);
        organization.setCodigoERP(this.codigoERP);
        organization.setAvatar(this.avatar);
        
        if (this.organizationId != null) {  
            Organization accounting = new Organization();
            accounting.setId(this.organizationId);
            organization.setOrganization(accounting);
        }

        return organization;
    }
    public Organization toEntity() {
        return this.toEntity(true);
    }

    public static OrganizationDTO fromEntity(Organization organization) {
        // @formatter:off
        OrganizationDTO dto = new OrganizationDTO()
            .withId(organization.getId())
            .withExternalId(organization.getExternalId())
            .withName(organization.getName())
            .withCnpj(organization.getCnpj())
            .withType(organization.getType())
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

    public static Pageable getPageRequest(SearchCriteria searchCriteria) {
        return PageRequest.of(searchCriteria.getPageIndex(), searchCriteria.getPageSize(), getSort(searchCriteria));
    }

    public static Sort getSort(SearchCriteria searchCriteria) {
        Sort sort = Sort.unsorted();

        if (searchCriteria.getSort() != null && searchCriteria.getSort().getOrder() != null
                && searchCriteria.getSort().getAttribute() != null) {
            String order = searchCriteria.getSort().getOrder();
            String attribute = searchCriteria.getSort().getAttribute();
            return getSort(attribute, order);
        }

        return sort;
    }

    public static Sort getSort(String attribute, String order) {
        Sort sort = Sort.unsorted();
        sort = Sort.by(attribute);
        if (order.equals("asc")) {
            sort = sort.ascending();
        } else if (order.equals("desc")) {
            sort = sort.descending();
        }
        return sort;
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
