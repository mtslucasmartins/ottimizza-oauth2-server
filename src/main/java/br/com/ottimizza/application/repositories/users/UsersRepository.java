package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;

import java.util.Optional;
import java.util.List;
import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// @formatter:on
@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, BigInteger>, UsersRepositoryCustom {

    @Query(value = "                                                                  \n" // @formatter:off
            + " WITH organizations AS (                                               \n" 
            + " 	SELECT fk_organizations_id FROM users_organizations uo2       \n"
            + " 	WHERE uo2.fk_users_id = :customerId                           \n"
            + " )                                                                     \n"
            + " SELECT u.* FROM users u                                               \n"
            + " WHERE u.id IN (                                                       \n"
            + " 	SELECT fk_users_id FROM users_organizations uo1               \n"
            + " 	WHERE uo1.fk_organizations_id IN (                            \n"
            + " 		SELECT fk_organizations_id FROM organizations         \n"
            + " 	)                                                             \n"
            + " )                                                                     \n"
            + " AND (u.type = 2)                                                      \n"
            + " AND (:username is null OR u.username ILIKE CONCAT('%',:username,'%')) \n"
            + " AND (:email is null OR u.email ILIKE CONCAT('%',:email,'%'))          \n"
            + " AND (:firstName is null OR u.email ILIKE CONCAT('%',:firstName,'%'))  \n"
            + " AND (:lastName is null OR u.email ILIKE CONCAT('%',:lastName,'%') )   \n"
            + "                                                                       ", nativeQuery = true)
    Page<User> fetchCustomersByCustomerId(@Param("customerId") BigInteger customerId, 
                                          @Param("username") String username,
                                          @Param("email") String email,
                                          @Param("firstName") String firstName,
                                          @Param("lastName") String lastName,
                                          Pageable pageable);

    @Query(value = "                                                                   "
            + " SELECT * FROM users u                                                  "
            + " WHERE u.id IN (                                                        "
            + " 	SELECT fk_users_id FROM users_organizations uo1                "
            + " 	WHERE uo1.fk_organizations_id = :organizationId                "
            + " )                                                                      "
            + " AND (u.type = 2)                                                       "
            + " AND (:username is null OR u.username ILIKE CONCAT('%',:username,'%'))  "
            + " AND (:email is null OR u.email ILIKE CONCAT('%',:email,'%'))           "
            + " AND (:firstName is null OR u.email ILIKE CONCAT('%',:firstName,'%'))   "
            + " AND (:lastName is null OR u.email ILIKE CONCAT('%',:lastName,'%') )    "
            + "                                                             ", nativeQuery = true)
    Page<User> fetchCustomersByOrganizationId(@Param("organizationId") BigInteger organizationId, 
                                          @Param("username") String username,
                                          @Param("email") String email,
                                          @Param("firstName") String firstName,
                                          @Param("lastName") String lastName,
                                          Pageable pageable);

                                          
    @Query("SELECT o FROM User o WHERE o.email like :filter AND o.organization.id = :accountingId")
    Page<User> findAllByAccountingId(@Param("filter") String filter, @Param("accountingId") BigInteger accountingId,
            Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.email like :email AND u.type = :type AND u.organization.id = :accountingId")
    Page<User> findAllByEmailAndTypeAndAccountingId(@Param("email") String email, @Param("type") Integer type,
            @Param("accountingId") BigInteger accountingId, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.organization.id = :accountingId AND u.type = 2")
    List<User> findCustomersByAccountingId(@Param("accountingId") BigInteger accountingId);

    @Query(value = " SELECT u.* FROM users_organizations uo       "
            + "   INNER JOIN users u                              "
            + "     ON (uo.fk_users_id = u.id)                    "
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

    /*
     * *****************************************************************************
     * *********************************** USERS_AUTHORITIES
     * *****************************************************************************
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_authorities (fk_users_id, fk_authorities_id) VALUES (:username, :authority)", nativeQuery = true)
    void addAuthority(@Param("username") String username, @Param("authority") String authority);

    /*
     * *****************************************************************************
     * *********************************** USERS_ORGANIZATIONS
     * *****************************************************************************
     */
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations (fk_users_id, fk_organizations_id) VALUES (:userId, :organizationId)", nativeQuery = true)
    void addOrganization(@Param("userId") BigInteger userId, @Param("organizationId") BigInteger authority);

    /*
     * *****************************************************************************
     * *********************************** VALIDATIONS
     * *****************************************************************************
     */
    @Query("SELECT CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean emailIsAlreadyRegistered(@Param("email") String email);

}
