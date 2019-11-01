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

public interface OrganizationRepository
        extends PagingAndSortingRepository<Organization, BigInteger>, OrganizationRepositoryCustom { // @formatter:

    @Query(value = " WITH organizations AS (                                               \n"
            + " 	SELECT fk_organizations_id FROM users_organizations uo2                \n"
            + " 	WHERE uo2.fk_users_id = :customerId                                    \n"
            + " )                                                                          \n"
            + " SELECT u.* FROM users u                                                    \n"
            + " WHERE u.id IN (                                                            \n"
            + " 	SELECT fk_users_id FROM users_organizations uo1                        \n"
            + " 	WHERE uo1.fk_organizations_id IN (                                     \n"
            + " 		SELECT fk_organizations_id FROM organizations                      \n"
            + " 	)                                                                      \n"
            + " )                                                                          \n"
            + " AND (u.type = 2)                                                           \n"
            + " AND (u.username LIKE CONCAT(:username) OR :username is null)                          \n"
            + " AND (u.email LIKE CONCAT('%',:email,'%') OR :email is null)               \n"
            + " AND (u.first_name LIKE CONCAT('%',:firstName,'%') OR :firstName is null)  \n"
            + " AND (u.last_name LIKE CONCAT('%',:lastName,'%') OR :lastName is null)     \n", nativeQuery = true)
    Page<Organization> fetchAllByAccountingId(@Param("customerId") BigInteger customerId,
            @Param("username") String username, @Param("email") String email, @Param("firstName") String firstName,
            @Param("lastName") String lastName, Pageable pageable);

    @Query("SELECT o FROM Organization o WHERE o.id = :id")
    Organization fetchById(@Param("id") BigInteger id);

    @Query("SELECT o FROM Organization o WHERE o.cnpj = :cnpj")
    Organization fetchByCnpj(@Param("cnpj") String cnpj);

    @Query("SELECT o FROM Organization o WHERE o.id = :id AND o.type = 1")
    Organization fetchAccountingById(@Param("id") BigInteger id);

    @Query("SELECT " + "   CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END              "
            + " FROM Organization o                                              "
            + " WHERE o.cnpj = :cnpj                                             "
            + " AND (:organizationId is null OR o.id != :organizationId)         "
            + " AND (:accountingId is null OR o.organization.id = :accountingId) ")
    boolean cnpjIsAlreadyRegistered(@Param("cnpj") String cnpj, @Param("organizationId") BigInteger organizationId,
            @Param("accountingId") BigInteger accountingId);

    @Query("SELECT o FROM Organization o WHERE o.externalId = :externalId ")
    Optional<Organization> findByExternalId(@Param("externalId") String externalId);

    @Query("SELECT o FROM Organization o WHERE o.cnpj = :cnpj AND o.type = 1 ")
    Optional<Organization> findAccountingByCnpj(@Param("cnpj") String cnpj);

    @Query("SELECT o FROM Organization o WHERE o.cnpj = :cnpj AND o.organization.id = :accountingId ")
    Optional<Organization> findOrganizationByCnpjAndAccountingId(@Param("cnpj") String cnpj,
            @Param("accountingId") BigInteger accountingId);

    @Query("SELECT o FROM Organization o WHERE LOWER(o.name) like LOWER(:filter) ")
    Page<Organization> findAll(@Param("filter") String filter, Pageable pageable);

    @Query("SELECT                                                          "
            + "   o                                                             "
            + " FROM Organization o                                             "
            + " WHERE o.name like :filter                                       "
            + " AND o.organization.id = :accountingId                           ")
    Page<Organization> findAllByAccountingId(@Param("filter") String filter,
            @Param("accountingId") BigInteger accountingId, Pageable pageable);

    @Query(value = " SELECT o.* FROM users_organizations uo                 "
            + "     INNER JOIN organizations o                                  "
            + "         on o.id = uo.fk_organizations_id                        "
            + "  WHERE uo.fk_users_id = :userId                                  "
            + "  AND o.fk_organizations_id = :accountingId                      "
            + "  AND LOWER(o.name) like LOWER(:filter)                          ", nativeQuery = true)
    Page<Organization> findAllByAccountingIdAndUserId(@Param("filter") String filter,
            @Param("accountingId") BigInteger accountingId, @Param("userId") BigInteger userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations_invites      "
            + "        (email, token, fk_organizations_id)       "
            + "     VALUES (:email, :token, :organizationId)     ", nativeQuery = true)
    void saveCustomerInviteToken(@Param("organizationId") BigInteger organizationId, @Param("email") String email,
            @Param("token") String token);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations (fk_users_id, fk_organizations_id) VALUES (:userId, :organizationId)", nativeQuery = true)
    void addCustomer(@Param("userId") BigInteger userId, @Param("organizationId") BigInteger authority);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users_organizations WHERE fk_users_id = :fk_users_id AND fk_organizations_id = :organizationId)", nativeQuery = true)
    void removeCustomer(@Param("fk_users_id") String username, @Param("organizationId") BigInteger authority);

}
