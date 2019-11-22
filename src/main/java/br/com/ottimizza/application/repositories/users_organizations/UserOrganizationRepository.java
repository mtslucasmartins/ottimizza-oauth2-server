package br.com.ottimizza.application.repositories.users_organizations;

import br.com.ottimizza.application.model.user_organization.UserOrganization;
import br.com.ottimizza.application.model.user_organization.UserOrganizationID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrganizationRepository extends JpaRepository<UserOrganization, UserOrganizationID> {

}
