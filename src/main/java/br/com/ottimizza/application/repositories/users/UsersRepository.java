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

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// @formatter:off
public interface UsersRepository extends PagingAndSortingRepository<User, String> {

    @Query("SELECT                                "
        + "   o                                   "
        + " FROM User o                           "
        + " WHERE o.email like :filter            "
        + " AND o.organization.id = :accountingId ")
    Page<User> findAllByAccountingId(@Param("filter") String filter, 
                                     @Param("accountingId") BigInteger accountingId,
                                     Pageable pageable);
 
    @Query(value = " SELECT u.* FROM users_organizations uo       "
            + "   INNER JOIN users u                              "
            + "     ON (uo.username = u.username)                 "
            + " WHERE uo.fk_organizations_id = :organizationId    ", nativeQuery = true)
    List<User> findCustomersByOrganizationId(@Param("organizationId") BigInteger organizationId);

    @Query(value = " SELECT uo FROM UserOrganizationInvite uo       "
            + " WHERE uo.organization.id = :organizationId    ")
    List<UserOrganizationInvite> findCustomersInvitedByOrganizationId(
            @Param("organizationId") BigInteger organizationId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations      "
            + "    (email, token, fk_organizations_id)   "
            + " VALUES (:email, :token, :organizationId) ", nativeQuery = true)
   void inviteCustomer(@Param("organizationId") BigInteger organizationId,
                                                @Param("email") String email, 
                                                @Param("token") String token);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.password = :password WHERE LOWER(u.username) = LOWER(:username)")
    void updatePassword(@Param("password") String password, @Param("username") String username);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_authority (username, authority) VALUES (:username, :authority)", nativeQuery = true)
    void addAuthority(@Param("username") String username, @Param("authority") String authority);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users_organizations (username, fk_organizations_id) VALUES (:username, :organizationId)", nativeQuery = true)
    void addOrganization(@Param("username") String username, @Param("organizationId") BigInteger authority);

    @Query("SELECT " + " CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END "
            + " FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean emailIsAlreadyRegistered(@Param("email") String email);

}
