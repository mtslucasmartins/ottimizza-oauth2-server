package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.UserAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.UserNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.repositories.UserOrganizationInviteRepository;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class UserService {

    @Inject
    MailServices mailServices;

    @Inject
    UsersRepository userRepository;

    @Inject
    UserOrganizationInviteRepository userOrganizationInviteRepository;

    public User findById(BigInteger id) throws UserNotFoundException, Exception {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User findByUsername(String username) throws UserNotFoundException, Exception {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    /**
     * Método para listar usuários com base no usuário logado e no filtro
     * especificado. Realiza paginação por padrão, página 1 com 10 itens por página.
     * 
     * @param filter    | Filtro - Classe com campos para filtro de usuário.
     * @param pageIndex | Paginação - Indíce da página atual.
     * @param pageSize  | Paginação - Quantidade de items por página.
     * @param principal | Segurança - Informações do usuário logado.
     * 
     * @return Objeto contendo informações de página e lista de usuarios.
     */
    public Page<UserDTO> fetchAll(UserDTO filter, int pageIndex, int pageSize, Principal principal)
            throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        if (authorizedUser.getType().equals(User.Type.CUSTOMER)) {
            // se usuario for do tipo cliente, visualiza apenas usuarios vinculados as
            // empresas da qual pertence.
            System.out.println("");
            System.out.println(">> " + filter.getUsername());
            System.out.println(">> " + filter.getUsername() == null);
            System.out.println("");
            System.out.println("");

            return userRepository
                    .fetchCustomersByCustomerId(authorizedUser.getId(), filter.getUsername(), filter.getEmail(),
                            filter.getFirstName(), filter.getLastName(), PageRequest.of(pageIndex, pageSize))
                    .map(UserDTO::fromEntityWithOrganization);
        }
        return userRepository.fetchAll(filter, PageRequest.of(pageIndex, pageSize), authorizedUser)
                .map(UserDTO::fromEntityWithOrganization);
    }

    public UserDTO create(UserDTO userDTO, Principal principal)
            throws OrganizationNotFoundException, UserAlreadyRegisteredException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        User user = userDTO.toEntity();
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        user.setOrganization(authorizedUser.getOrganization());
        checkIfEmailIsAlreadyRegistered(user);
        return UserDTO.fromEntity(userRepository.save(user));
    }

    public UserDTO patch(BigInteger id, UserDTO userDTO, User authorizedUser)
            throws OrganizationNotFoundException, OrganizationAlreadyRegisteredException, Exception {
        User current = findById(id);
        current = userDTO.patch(current);
        checkIfEmailIsAlreadyRegistered(current.getEmail(), current);
        return UserDTO.fromEntity(userRepository.save(current));
    }

    // @formatter:off
    public Page<UserDTO> findAllByAccountingId(String filter, int pageIndex, int pageSize, Principal principal)
            throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        return userRepository.findAllByAccountingId(
            "%" + filter + "%", authorizedUser.getOrganization().getId(), PageRequest.of(pageIndex, pageSize)
        ).map(UserDTO::fromEntity);
    } // @formatter:on

    // @formatter:off
    public Page<UserDTO> fetchCustomers(String email, int pageIndex, int pageSize, Principal principal)
            throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        return userRepository.findAllByEmailAndTypeAndAccountingId(
            "%" + email + "%", 2, authorizedUser.getOrganization().getId(), PageRequest.of(pageIndex, pageSize)
        ).map(UserDTO::fromEntity);
    } // @formatter:on

    //
    // INVITE
    //
    public Map<String, String> invite(Map<String, String> args, User authorizedUser)
            throws OrganizationNotFoundException, Exception {
        if (authorizedUser.getType().equals(User.Type.ACCOUNTANT)) {
            String token = UUID.randomUUID().toString();
            String email = args.getOrDefault("email", "");
            if (email.equals("")) {
                throw new IllegalArgumentException("Email cannot be blank.");
            }
            UserOrganizationInvite invite = new UserOrganizationInvite();
            List<UserOrganizationInvite> invites = userOrganizationInviteRepository.findByEmailAndOrganizationId(email,
                    authorizedUser.getOrganization().getId());
            if (invites.size() == 0) {
                invite.setEmail(email);
                invite.setToken(token);
                invite.setType(User.Type.ACCOUNTANT);
                invite.setOrganization(authorizedUser.getOrganization());
                // saves the token to database.
                invite = userOrganizationInviteRepository.save(invite);
            } else {
                invite = invites.get(0);
            }
            // sends the token to the invited user.
            sendInviteByEmail(invite, authorizedUser);
            args.put("token", token);
        }
        return args;
    }

    @Async
    private void sendInviteByEmail(UserOrganizationInvite invite, User authorizedUser) {
        String accountingName = authorizedUser.getOrganization().getName();
        String to = invite.getEmail();
        String subject = MessageFormat.format("Conta {0}.", accountingName);
        String template = mailServices.inviteCustomerTemplate(authorizedUser, invite.getToken());

        mailServices.send(accountingName, to, subject, template);
    }

    public Page<UserOrganizationInvite> fetchInvitedUsers(String email, int pageIndex, int pageSize,
            Principal principal) throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        return userOrganizationInviteRepository.fetchInvitedUsersByAccountingId(email,
                authorizedUser.getOrganization().getId(), PageRequest.of(pageIndex, pageSize));
    } // @formatter:on

    //
    //
    //
    //
    public boolean checkIfEmailIsAlreadyRegistered(User user) throws UserAlreadyRegisteredException {
        if (userRepository.emailIsAlreadyRegistered(user.getEmail())) {
            System.out.println("A user with that email address is already registered.");
            throw new UserAlreadyRegisteredException("A user with that email address is already registered.");
        }
        return true;
    }

    public boolean checkIfEmailIsAlreadyRegistered(String email, User user) throws UserAlreadyRegisteredException {
        if (userRepository.emailIsAlreadyRegistered(email, user.getId())) {
            System.out.println("A user with that email address is already registered.");
            throw new UserAlreadyRegisteredException("A user with that email address is already registered.");
        }
        return true;
    }

}
