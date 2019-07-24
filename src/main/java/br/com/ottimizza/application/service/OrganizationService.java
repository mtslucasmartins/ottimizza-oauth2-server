package br.com.ottimizza.application.service;

import java.math.BigInteger;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class OrganizationService {

    @Inject
    UsersRepository userRepository;

    @Inject
    OrganizationRepository organizationRepository;

    public Organization findById(BigInteger id) throws OrganizationNotFoundException, Exception {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
    }

    public Organization save(Organization organization) throws OrganizationAlreadyRegisteredException, Exception {
        organization.setExternalId(organization.getCnpj());

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
