package br.com.ottimizza.application.repositories;

import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import java.math.BigInteger;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrganizationInviteRepository extends JpaRepository<UserOrganizationInvite, BigInteger> {

    UserOrganizationInvite findByToken(String name);

    List<UserOrganizationInvite> findByEmail(String email);

}
