package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

    public Page<UserDTO> fetchAll(UserDTO filter, int pageIndex, int pageSize, Principal principal)
            throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        if (authorizedUser.getType().equals(User.Type.CUSTOMER)) {
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

    public UserDTO create(UserDTO userDTO, Principal principal) // @formatter:off
            throws OrganizationNotFoundException, UserAlreadyRegisteredException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        User user = userDTO.toEntity()
                            .toBuilder()
                                .organization(authorizedUser.getOrganization())
                            .build();
        return UserDTO.fromEntity(create(user));
    }

    public UserDTO patch(BigInteger id, UserDTO userDTO, Principal principal)
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

            if (!organization.getCnpj().equals(lastOrganization.getCnpj())) {
                if (!accounting.getCnpj().equals(lastAccounting.getCnpj())) {
                    
                    accounting = organizationRepository
                                    .findAccountingByCnpj(accounting.getCnpj())
                                    .orElse(accounting);
                                    
                    if (accounting.getId() == null) {

                        System.out.println("\n --- Nova Contabilidade --- ");
                        System.out.println(MessageFormat.format(" Nome: {0} ", accounting.getName()));

                        long timeout = lastCallToAPI == null ? 0 : 20000 - (new Date().getTime() - lastCallToAPI.getTime());

                        try {
                            TimeUnit.MILLISECONDS.sleep(timeout);

                            DadosReceitaWS info = receitaWSClient.getInfo(accounting.getCnpj()).getBody();
                            lastCallToAPI = new Date();

                            if (info.getEmail() != null && !info.getEmail().isEmpty()) {
                                accounting.setEmail(info.getEmail());
                            } else {
                                accounting.setEmail(MessageFormat.format("c{0}@ottimizza.com.br", accounting.getCnpj()));
                            }
                            accounting = organizationRepository.save(accounting);

                            User accountant = User.builder()
                                    .firstName(info.getNome())
                                    .email(info.getEmail())
                                    .username(accounting.getEmail())
                                    .password("ottimizza")
                                    .type(User.Type.ACCOUNTANT)
                                    .organization(accounting).build();

                            accountant = create(accountant);
                        } catch (Exception e) {
                            continue;
                        }
                    } else {
                        System.out.println("\n --- Contabilidade --- ");
                        System.out.println(MessageFormat.format(" Id: {0} ", accounting.getId()));
                        System.out.println(MessageFormat.format(" Nome: {0} ", accounting.getName()));
                    }

                } else {
                    accounting = lastAccounting;
                }
                organization.setOrganization(accounting);
                organization = organizationRepository.findOrganizationByCnpjAndAccountingId(
                    organization.getCnpj(), accounting.getId()
                ).orElse(organization);
                if (organization.getId() == null) {
                    System.out.println("\n\t --- Nova Empresa --- ");
                    System.out.println(MessageFormat.format("\t Nome: {0} ", organization.getName()));

                    organization = organizationRepository.save(organization);
                } else {
                    System.out.println("\n\t --- Epresa --- ");
                    System.out.println(MessageFormat.format("\t Id: {0} ", organization.getId()));
                    System.out.println(MessageFormat.format("\t Nome: {0} ", organization.getName()));
                }
            } else {
                organization = lastOrganization;
                accounting = lastAccounting;
            }
            
            List<String> emails = Arrays.asList(object.getEmail());
            if (object.getEmail().contains(",")) {
                emails = Arrays.asList(object.getEmail().split(","));
            }
            
            for (String email : emails) {
                email = email.toLowerCase().trim();

                if (email == null || email.isEmpty()) {
                    continue;
                }

                User user = User.builder()
                    .firstName(email)
                    .username(email)
                    .email(email)
                    .organization(accounting)
                    .type(
                        organization.getCnpj() == null || organization.getCnpj().isEmpty() 
                            ? User.Type.ACCOUNTANT : User.Type.CUSTOMER
                    ).build();

                System.out.println("\n\t\t --- Novo usuário --- ");
                System.out.println(MessageFormat.format("\t\t E-mail: {0} ", email));

                try {
                    user.setId(null);
                    user.setFirstName(email);
                    user.setEmail(email);
                    user.setUsername(email);

                    // verificando se o usuario já está registrado no sistema
                    if (userRepository.emailIsAlreadyRegistered(email)) {
                        User existing = findByUsername(email);

                        // verificando se pertence a mesma contabilidade.
                        if (existing.getOrganization() == null) {
                            user = userRepository.save(
                                UserDTO.fromEntity(user).patch(existing)
                                    .toBuilder()
                                        .email(email)
                                        .username(email)
                                        .type(user.getType())
                                        .organization(accounting)
                                    .build()
                            );
                            try {
                                userRepository.addOrganization(user.getId(), organization.getId());
                            }catch (Exception e) {}
                        } else if (existing.getOrganization().equals(user.getOrganization())) { 
                            user = userRepository.save(
                                UserDTO.fromEntity(user).patch(existing)
                                    .toBuilder()
                                        .email(email)
                                        .username(email)
                                        .type(user.getType())
                                    .build()
                            );
                            try {
                                userRepository.addOrganization(user.getId(), organization.getId());
                            }catch (Exception e) {}
                        } else {
                            throw new Exception("");
                        } 
                    } else {
                        // caso não exista, cria um novo usuário.
                        user.setPassword("ottimizza");
                        user = create(user);

                        try {
                            userRepository.addOrganization(user.getId(), organization.getId());
                        }catch (Exception e) {}
                    }
                } catch (Exception ex) {
                    System.out.println("\n>>> Exception\n");
                    ex.printStackTrace();
                }

                try {
                    userRepository.addOrganization(user.getId(), organization.getId());
                }catch (Exception e) {}

            }

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
