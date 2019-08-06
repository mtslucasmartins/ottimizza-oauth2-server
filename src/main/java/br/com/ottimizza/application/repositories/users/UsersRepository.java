package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.model.User;

import java.util.Optional;
import java.util.List;
import java.math.BigInteger;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<User, String> {

    @Query(value = " SELECT u.* FROM users_organizations uo       "
            + "   INNER JOIN users u                              "
            + "     ON (uo.username = u.username)                    "
            + " WHERE uo.fk_organizations_id = :organizationId    ", nativeQuery = true)
    List<User> findCustomersByOrganizationId(@Param("organizationId") BigInteger organizationId);

    @Query("SELECT u FROM User u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    User findByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u set u.password = :password WHERE LOWER(u.username) = LOWER(:username)")
    void updatePassword(@Param("password") String password, @Param("username") String username);

    @Query("SELECT " + " CASE WHEN (COUNT(*) > 0) THEN TRUE ELSE FALSE END "
            + " FROM User u WHERE LOWER(u.email) = LOWER(:email)")
    boolean emailIsAlreadyRegistered(@Param("email") String email);

}
