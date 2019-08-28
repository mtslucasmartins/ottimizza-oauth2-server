package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;

import java.util.Optional;
import java.util.List;
import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// @formatter:off
@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, String>, UsersRepositoryCustom {

    @Query("SELECT o FROM User o WHERE o.email like :filter AND o.organization.id = :accountingId")
    Page<User> findAllByAccountingId(@Param("filter") String filter, 
                                     @Param("accountingId") BigInteger accountingId,
                                     Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.email like :email AND u.type = :type AND u.organization.id = :accountingId")
    Page<User> findAllByEmailAndTypeAndAccountingId(@Param("email") String email,
                                                    @Param("type") Integer type, 
                                                    @Param("accountingId") BigInteger accountingId,
                                                    Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.organization.id = :accountingId AND u.type = 2")
    List<User> findCustomersByAccountingId(@Param("accountingId") BigInteger accountingId);

    @Query(value = " SELECT u.* FROM users_organizations uo       "
            + "   INNER JOIN users u                              "
            + "     ON (uo.fK-users_id = u.id)                    "
            + " WHERE uo.fk_organizations_id = :organizationId    "
            + " AND u.type = 2                                    ", nativeQuery = true)
    List<User> findCustomersByOrganizationId(@Param("organizationId") BigInteger organizationId);

    @Query("SELECT uo FROM UserOrganizationInvite uo WHERE uo.organization.id = :organizationId ")
    List<UserOrganizationInvite> findCustomersInvitedByOrganizationId(
            @Param("organizationId") BigInteger organizationId);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.password = :password WHERE LOWER(u.username) = LOWER(:username)")
    void updatePassword(@Param("password") String password, @Param("username") String username);


    /* ****************************************************************************************************************
     * USERS_AUTHORITIES
     * ************************************************************************************************************* */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_authorities (fk_users_id, fk_authorities_id) VALUES (:username, :authority)", nativeQuery = true)
    void addAuthority(@Param("username") String username, @Param("authority") String authority);

    /* ****************************************************************************************************************
     * USERS_ORGANIZATIONS
     * ************************************************************************************************************* */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations (fk_users_id, fk_organizations_id) VALUES (:userId, :organizationId)", nativeQuery = true)
    void addOrganization(@Param("userId") BigInteger userId, @Param("organizationId") BigInteger authority);

    /* ****************************************************************************************************************
     * VALIDATIONS
     * ************************************************************************************************************* */
    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean emailIsAlreadyRegistered(@Param("email") String email);

}
