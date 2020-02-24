package br.com.ottimizza.application.repositories.organizations;

import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;

import java.math.BigInteger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrganizationRepositoryCustom { // @formatter:off OrganizationRepositoryImpl

    Page<Organization> fetchAll(OrganizationDTO filter, Pageable pageable, User authorizedUser);

    Page<Organization> fetchAllByAccountantId(OrganizationDTO filter, Pageable pageable, User authorizedUser);

    Page<Organization> fetchAllByCustomerId(BigInteger id, OrganizationDTO filter, Pageable pageable);

    Page<Organization> fetchAllByCustomerId(OrganizationDTO filter, Pageable pageable, User authorizedUser);
    
}
