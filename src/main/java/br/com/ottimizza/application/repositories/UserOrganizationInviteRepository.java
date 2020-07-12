package br.com.ottimizza.application.repositories;

import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository // @formatter:on
public interface UserOrganizationInviteRepository
        extends PagingAndSortingRepository<UserOrganizationInvite, BigInteger> {

    UserOrganizationInvite findByToken(String name);

    List<UserOrganizationInvite> findByEmail(String email);

    @Query("SELECT i FROM UserOrganizationInvite i WHERE LOWER(i.email) = LOWER(:email) AND i.organization.id = :organizationId")
    List<UserOrganizationInvite> findByEmailAndOrganizationId(@Param("email") String email,
            @Param("organizationId") BigInteger organizationId);
    

    @Query(value = "                                                                                    "
            + "  SELECT uoi.* FROM users_organizations_invites uoi                                      "
            + "    INNER JOIN organizations o                                                           "
            + "      ON o.id = uoi.fk_organizations_id                                                  "
            + "    WHERE (o.id = :organizationId or o.fk_organizations_id = :organizationId)            "
            + "    AND  (:email is null OR uoi.email ILIKE CONCAT('%', :email, '%'))                    "
            + "", nativeQuery = true)
    Page<UserOrganizationInvite> fetchInvitedUsersByAccountingId(@Param("email") String email,
            @Param("organizationId") BigInteger organizationId, Pageable pageable);

}
        