package br.com.ottimizza.application.repositories.organizations;

import br.com.ottimizza.application.model.Organization;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrganizationRepository extends JpaRepository<Organization, BigInteger>, OrganizationRepositoryCustom { // @formatter:off

  @Query("SELECT " 
      + "   CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END "
      + " FROM Organization o " 
      + " WHERE o.cnpj = :cnpj " 
      + " AND (:organizationId is null OR o.organization.id = :organizationId)")
  boolean cnpjIsAlreadyRegistered(@Param("cnpj") String cnpj, @Param("organizationId") BigInteger organizationId);
  
  @Query("SELECT o FROM Organization o WHERE o.externalId = :externalId ")
  Optional<Organization> findByExternalId(@Param("externalId") String externalId);

}
