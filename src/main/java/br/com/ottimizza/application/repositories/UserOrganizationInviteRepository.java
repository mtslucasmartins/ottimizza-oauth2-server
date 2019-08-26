package br.com.ottimizza.application.repositories;

import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository // @formatter:off
public interface UserOrganizationInviteRepository extends JpaRepository<UserOrganizationInvite, BigInteger> {

    UserOrganizationInvite findByToken(String name);

    List<UserOrganizationInvite> findByEmail(String email);

    @Query("SELECT i FROM UserOrganizationInvite i WHERE LOWER(i.email) = LOWER(:email) AND i.organization.id = :organizationId")
    List<UserOrganizationInvite> findByEmailAndOrganizationId(@Param("email") String email,
                                                              @Param("organizationId") BigInteger organizationId);

}
