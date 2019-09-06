package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ottimizza.application.domain.Authorities;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.UserAlreadyRegisteredException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.repositories.UserOrganizationInviteRepository;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class SignUpService {

    @Inject
    UsersRepository userRepository;

    @Inject
    OrganizationService organizationService;

    @Inject
    UserService userService;

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserOrganizationInviteRepository userOrganizationInviteRepository;

    /**
     * Função para registrar um usuário novo no sistema.
     * 
     * @param user         | Usuario que será registrado.
     * @param organization | Organização (Contabilidade ou Cliente)
     * @return | O Usuário registrado.
     * @throws OrganizationAlreadyRegisteredException
     * @throws UserAlreadyRegisteredException
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(User user, Organization organization)
            throws OrganizationAlreadyRegisteredException, UserAlreadyRegisteredException, Exception {
        user.setUsername(user.getEmail());
        user.setType(User.Type.ACCOUNTANT);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        organization.setExternalId(UUID.randomUUID().toString());

        // Validations
        userService.checkIfEmailIsAlreadyRegistered(user);
        organizationService.checkIfOrganizationIsNotParentOfItself(organization);
        organizationService.checkIfOrganizationIsNotAlreadyRegistered(organization);

        // creates the organization.
        organization.setType(1);
        organization = organizationRepository.save(organization);

        // creates the user.
        user.setOrganization(organization);
        user = userRepository.save(user);

        userRepository.addAuthority(user.getUsername(), "ADMIN");

        return user;
    }

    public User register(User user, Organization organization, String token)
            throws OrganizationAlreadyRegisteredException, UserAlreadyRegisteredException, Exception {

        if (token.equals("")) {
            this.register(user, organization);
        }

        UserOrganizationInvite inviteTokenDetails = getInviteTokenDetails(token);

        user.setUsername(inviteTokenDetails.getEmail());
        user.setEmail(inviteTokenDetails.getEmail());

        user.setType(inviteTokenDetails.getType());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        if (inviteTokenDetails.getType().equals(User.Type.ACCOUNTANT)) {
            user.setOrganization(inviteTokenDetails.getOrganization());
        } else {
            user.setOrganization(inviteTokenDetails.getOrganization().getOrganization());
        }

        // Checking if email is already registered.
        userService.checkIfEmailIsAlreadyRegistered(user);

        // creates the userF.
        user = userRepository.save(user);

        if (inviteTokenDetails.getType().equals(User.Type.CUSTOMER)) {
            // Adiciona organizações ao usuário.
            migrateUsersOrganizationsFromInvitesByUser(user);
        }

        // Adiciona autoridades ao usuário.
        userRepository.addAuthority(user.getUsername(), Authorities.ADMIN.getName());
        userRepository.addAuthority(user.getUsername(), Authorities.WRITE.getName());
        userRepository.addAuthority(user.getUsername(), Authorities.READ.getName());

        return user;
    }

    public UserOrganizationInvite getInviteTokenDetails(String token) {
        return userOrganizationInviteRepository.findByToken(token);
    }

    /**
     * Função para buscar todos os convites enviados para um email, iterá-los e
     * relacionar as organizações vinculadas ao convite, ao usuário registrado.
     * 
     * @param email | E-mail para busca de convites.
     */
    @Async
    public void migrateUsersOrganizationsFromInvitesByUser(User registeredUser) {
        List<UserOrganizationInvite> invites = userOrganizationInviteRepository.findByEmail(registeredUser.getEmail());
        for (UserOrganizationInvite invite : invites) {
            try {
                userRepository.addOrganization(registeredUser.getId(), invite.getOrganization().getId());
                userOrganizationInviteRepository.delete(invite);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
