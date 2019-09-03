package br.com.ottimizza.application.repositories.organizations;

import br.com.ottimizza.application.model.Organization;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface OrganizationRepository extends PagingAndSortingRepository<Organization, BigInteger> { // @formatter:off

  @Query("SELECT " 
      + "   CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END              " 
      + " FROM Organization o                                              " 
      + " WHERE o.cnpj = :cnpj                                             "
      + " AND (:organizationId is null OR o.id != :organizationId)         " 
      + " AND (:accountingId is null OR o.organization.id = :accountingId) ")
  boolean cnpjIsAlreadyRegistered(@Param("cnpj") String cnpj, 
                                  @Param("organizationId") BigInteger organizationId, 
                                  @Param("accountingId") BigInteger accountingId);
  


  @Query("SELECT o FROM Organization o WHERE o.externalId = :externalId ")
  Optional<Organization> findByExternalId(@Param("externalId") String externalId);



  @Query("SELECT o FROM Organization o WHERE LOWER(o.name) like LOWER(:filter) ")
  Page<Organization> findAll(@Param("filter") String filter, Pageable pageable);



  @Query("SELECT                                                          " 
      + "   o                                                             " 
      + " FROM Organization o                                             " 
      + " WHERE o.name like :filter                                       "
      + " AND o.organization.id = :accountingId                           ")
  Page<Organization> findAllByAccountingId(@Param("filter") String filter, 
                                           @Param("accountingId") BigInteger accountingId, 
                                           Pageable pageable);
    


  @Query(value = " SELECT o.* FROM users_organizations uo                 " 
      + "     INNER JOIN organizations o                                  " 
      + "         on o.id = uo.fk_organizations_id                        " 
      + "  WHERE uo.username = :username                                  "
      + "  AND o.fk_organizations_id = :accountingId                      "
      + "  AND LOWER(o.name) like LOWER(:filter)                          ", nativeQuery = true)
  Page<Organization> findAllByAccountingIdAndUsername(@Param("filter") String filter, 
                                                      @Param("accountingId") BigInteger accountingId, 
                                                      @Param("username") String username, 
                                                      Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations_invites      "
            + "        (email, token, fk_organizations_id)       "
            + "     VALUES (:email, :token, :organizationId)     ", nativeQuery = true)
    void saveCustomerInviteToken(@Param("organizationId") BigInteger organizationId,
                                 @Param("email") String email, 
                                 @Param("token") String token);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations (fk_users_id, fk_organizations_id) VALUES (:username, :organizationId)", nativeQuery = true)
    void addCustomer(@Param("username") String username, @Param("organizationId") BigInteger authority);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_organizations WHERE fk_users_id = :fk_users_id AND fk_organizations_id = :organizationId)", nativeQuery = true)
    void removeCustomer(@Param("fk_users_id") String username, @Param("organizationId") BigInteger authority);

}
