package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import br.com.ottimizza.application.client.ReceitaWSClient;
import br.com.ottimizza.application.domain.dtos.DadosReceitaWS;
import br.com.ottimizza.application.domain.dtos.ImportDataModel;
import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.UserAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.users.UserNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.repositories.UserOrganizationInviteRepository;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class UserService {

    @Autowired
    ReceitaWSClient receitaWSClient;

    @Inject
    MailServices mailServices;

    @Inject
    UsersRepository userRepository;

    @Inject
    UserOrganizationInviteRepository userOrganizationInviteRepository;

    @Inject
    OrganizationRepository organizationRepository;

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
            return userRepository
                    .fetchCustomersByCustomerId(authorizedUser.getId(), filter.getUsername(), filter.getEmail(),
                            filter.getFirstName(), filter.getLastName(), PageRequest.of(pageIndex, pageSize))
                    .map(UserDTO::fromEntityWithOrganization);
        }
        return userRepository.fetchAll(filter, PageRequest.of(pageIndex, pageSize), authorizedUser)
                .map(UserDTO::fromEntityWithOrganization);
    }

    public User create(User user) throws OrganizationNotFoundException, UserAlreadyRegisteredException, Exception {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        checkIfEmailIsAlreadyRegistered(user);
        return userRepository.save(user);
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

    public UserDTO patch(BigInteger id, UserDTO userDTO, Principal principal)
            throws OrganizationNotFoundException, OrganizationAlreadyRegisteredException, Exception {
        User authorizedUser = findByUsername(principal.getName());
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
        // @formatter:off
        String accountingName = authorizedUser.getOrganization().getName();
        MailServices.Builder messageBuilder = new MailServices.Builder()
                .withName(accountingName)
                .withTo(invite.getEmail())
                .withCc(authorizedUser.getOrganization().getEmail())
                .withSubject(MessageFormat.format("Conta {0}.", accountingName))
                .withHtml(mailServices.inviteCustomerTemplate(authorizedUser, invite.getToken()));
        mailServices.send(messageBuilder); 
        // @formatter:on
    }

    public Page<UserOrganizationInvite> fetchInvitedUsers(String email, int pageIndex, int pageSize,
            Principal principal) throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        return userOrganizationInviteRepository.fetchInvitedUsersByAccountingId(email,
                authorizedUser.getOrganization().getId(), PageRequest.of(pageIndex, pageSize));
    } // @formatter:on

    // public String importUsersFromCSV(MultipartFile file, Principal principal)
    // throws Exception {
    // InputStream is = file.getInputStream();

    // BufferedReader reader = new BufferedReader(new InputStreamReader(is));

    // while (reader.ready()) {
    // String line = reader.readLine();

    // //

    // }

    // return "{}";
    // }
    public String importUsersFromJSON(List<ImportDataModel> data, Principal principal) throws Exception { // @formatter:off
        User authorizedUser = findByUsername(principal.getName());

        Date lastCallToAPI = null;

        Organization lastAccounting = new Organization();
        Organization lastOrganization = new Organization();

        for (ImportDataModel object : data) {
            
            Organization accounting = Organization.builder()
                    .name(object.getAccountingName())
                    .cnpj(object.getAccountingCnpj().replaceAll("\\D", ""))
                    .type(Organization.Type.ACCOUNTING).build();
                    
            Organization organization = Organization.builder()
                    .name(object.getOrganizationName())
                    .cnpj(object.getOrganizationCnpj().replaceAll("\\D", ""))
                    .codigoERP(object.getOrganizationCode())
                    .organization(accounting)
                    .type(Organization.Type.CLIENT).build();

            User user = User.builder()
                    .firstName(object.getFirstName())
                    .email(object.getEmail())
                    .type(
                        organization.getCnpj() == null || organization.getCnpj().isEmpty() 
                            ? User.Type.ACCOUNTANT : User.Type.CUSTOMER
                    ).build();

            if (!organization.getCnpj().equals(lastOrganization.getCnpj())) {
                if (!accounting.getCnpj().equals(lastAccounting.getCnpj())) {

                    accounting = organizationRepository
                                    .findAccountingByCnpj(accounting.getCnpj())
                                    .orElse(accounting);

                    if (accounting.getId() == null) {
                        long timeout = lastCallToAPI == null ? 0 : 20000 - (new Date().getTime() - lastCallToAPI.getTime());

                        TimeUnit.MILLISECONDS.sleep(timeout);

                        DadosReceitaWS info = receitaWSClient.getInfo(accounting.getCnpj()).getBody();
                        lastCallToAPI = new Date();

                        if (info.getEmail() != null && !info.getEmail().isEmpty()) {
                            accounting.setEmail(info.getEmail());
                            organizationRepository.save(accounting);

                            User accountant = User.builder()
                                    .firstName(info.getNome())
                                    .email(info.getEmail())
                                    .username(info.getEmail())
                                    .password("ottimizza")
                                    .type(User.Type.ACCOUNTANT)
                                    .organization(accounting).build();

                            create(accountant);
                        }
                    } 
                } else {
                    accounting = lastAccounting;
                }
                organization = organizationRepository.findOrganizationByCnpjAndAccountingId(
                    organization.getCnpj(), accounting.getId()
                ).orElse(organization);
                if (organization.getId() == null) {
                    organizationRepository.save(organization);
                }
            } else {
                organization = lastOrganization;
            }

            // split(',')

            // iterate emails 

            System.out.println(MessageFormat.format("User Email   {0}", user.getEmail()));

            // adds the organization to user
            User u = userRepository.findByEmail(user.getEmail());

            if (u == null || u.getId() == null) {
                System.out.println("Creating New Customer User");
                // creates the user
            } else {
                user = u;
            }
            // check if organization is from last iteration...
            // 
            //     check if organization is from last iteration...
            //         check if accounting cnpj exists...
            //        
            //         if exists 
            //              get accounting by cnpj
            //         else 
            //              creates a new accounting...
            //              creates a admin...
            //
            //     check if organization cnpj exists...
            //    
            //     if exists 
            //          get organization by cnpj
            //     else 
            //          creates a new organization...
            // 
            // creates the user...
            lastAccounting = accounting.toBuilder().build();
            lastOrganization = organization.toBuilder().build();
        }

        return "{}";
    }

    //
    //
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
