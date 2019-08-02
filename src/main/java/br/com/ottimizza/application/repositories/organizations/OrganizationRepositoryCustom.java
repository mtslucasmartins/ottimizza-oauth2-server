package br.com.ottimizza.application.repositories.organizations;

import java.math.BigInteger;
import java.util.List;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;

public interface OrganizationRepositoryCustom {

    List<Organization> findAll(String filter, Integer pageIndex, Integer pageSize);

    List<Organization> findAllByAccountingId(String filter, Integer pageIndex, Integer pageSize, BigInteger accountingId);
    
    List<Organization> findAllByAccountingIdAndUsername(String filter, Integer pageIndex, Integer pageSize, BigInteger accountingId, String username);

}
