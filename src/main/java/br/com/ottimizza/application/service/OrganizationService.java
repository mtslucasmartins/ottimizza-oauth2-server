package br.com.ottimizza.application.service;

import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.Authorities;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class OrganizationService {

    @Inject
    UsersRepository userRepository;

    @Inject
    OrganizationRepository organizationRepository;

    public Organization findById(BigInteger id, User authorizedUser) throws OrganizationNotFoundException, Exception {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
    }

    public Organization findByExternalId(String externalId, User authorizedUser) throws OrganizationNotFoundException, Exception {
        return organizationRepository.findByExternalId(externalId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
    }

    public List<Organization> findAll(String filter, int pageIndex, int pageSize, User authorizedUser)
            throws OrganizationNotFoundException, Exception {
        // All authorities to string array.
        List<String> authorities = authorizedUser.getAuthorities().stream().map((authority) -> {
            return authority.getName();
        }).collect(Collectors.toList());

        // When user is an accountant.
        if (authorities.contains(Authorities.ACCOUNTANT_READ.getName())
                || authorities.contains(Authorities.ACCOUNTANT_WRITE.getName())
                || authorities.contains(Authorities.ACCOUNTANT_ADMIN.getName())) {
            return organizationRepository.findAllByAccountingId(filter, pageIndex, pageSize,
                    authorizedUser.getOrganization().getId());
        }
        // When user is an customer.
        if (authorities.contains(Authorities.CUSTOMER_READ.getName())
                || authorities.contains(Authorities.CUSTOMER_WRITE.getName())) {
            return organizationRepository.findAllByAccountingIdAndUsername(filter, pageIndex, pageSize,
                    authorizedUser.getOrganization().getId(), authorizedUser.getUsername());
        }

        return new ArrayList<>();
    }

    public Organization save(Organization organization, User authorizedUser)
            throws OrganizationAlreadyRegisteredException, Exception {
        organization.setExternalId(UUID.randomUUID().toString());
        // Checking if organization wont cause an loop
        if (organization.getOrganization() != null) {
            if (organization.getId().compareTo(organization.getOrganization().getId()) == 0) {
                System.out.println("A organization cannot be a parent of itself.");
                throw new Exception("A organization cannot be a parent of itself.");
            }
        } else {
            if (authorizedUser.getOrganization() != null && authorizedUser.getOrganization().getId() != null) {
                organization.setOrganization(authorizedUser.getOrganization());
                if (organization.getId() != null
                        && organization.getId().compareTo(organization.getOrganization().getId()) == 0) {
                    System.out.println("A organization cannot be a parent of itself.");
                    throw new Exception("A organization cannot be a parent of itself.");
                }
            }
        }

        // Checking if organization is already registered.
        BigInteger organizationId = organization.getOrganization() == null ? null
                : organization.getOrganization().getId();
        if (organizationRepository.cnpjIsAlreadyRegistered(organization.getCnpj(), organizationId)) {
            System.out.println("A organization with that cnpj is already registered.");
            throw new OrganizationAlreadyRegisteredException("A organization with that cnpj is already registered.");
        }

        // creates the organization.
        return organizationRepository.save(organization);
    }

    public Organization save(BigInteger id, Organization organization, User authorizedUser)
            throws OrganizationNotFoundException, OrganizationAlreadyRegisteredException, Exception {
        // checking if organizations exists.
        Organization current = findById(id, authorizedUser);

        organization.setId(current.getId());
        organization.setExternalId(current.getExternalId());

        // Checking if organization wont cause an loop
        if (organization.getOrganization() != null) {
            if (organization.getId().compareTo(organization.getOrganization().getId()) == 0) {
                System.out.println("A organization cannot be a parent of itself.");
                throw new Exception("A organization cannot be a parent of itself.");
            }
        }

        // Checking if organization is already registered.
        BigInteger organizationId = organization.getOrganization() == null ? null
                : organization.getOrganization().getId();
        if (organizationRepository.cnpjIsAlreadyRegistered(organization.getCnpj(), organizationId)) {
            System.out.println("A organization with that cnpj is already registered.");
            throw new OrganizationAlreadyRegisteredException("A organization with that cnpj is already registered.");
        }

        // creates the organization.
        return organizationRepository.save(organization);
    }

}
