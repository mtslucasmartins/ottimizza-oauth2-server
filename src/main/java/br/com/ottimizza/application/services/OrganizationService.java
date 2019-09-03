package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.Authorities;
import br.com.ottimizza.application.domain.OrganizationTypes;
import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.UserNotFoundException;
import br.com.ottimizza.application.domain.responses.GenericPageableResponse;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.repositories.UserOrganizationInviteRepository;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class OrganizationService {

    @Inject
    UsersRepository userRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserOrganizationInviteRepository userOrganizationInviteRepository;

    @Inject
    MailServices mailServices;

    public Organization findById(BigInteger id, User authorizedUser) throws OrganizationNotFoundException, Exception {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
    }

    public Organization findByExternalId(String externalId, User authorizedUser)
            throws OrganizationNotFoundException, Exception {
        return organizationRepository.findByExternalId(externalId)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
    }

    // @formatter:off
    public GenericPageableResponse<Organization> findAll(String filter, Pageable pageRequest, User authorizedUser)
            throws OrganizationNotFoundException, Exception {
        filter = "%" + filter + "%";
        // List<String> authorities = authorizedUser.getAuthorities().stream().map((authority) -> {
        //     return authority.getName();
        // }).collect(Collectors.toList());

        if (authorizedUser.getType().equals(User.Type.ACCOUNTANT)) {
            Page<Organization> page =  organizationRepository.findAllByAccountingId(
                    filter, authorizedUser.getOrganization().getId(), pageRequest
            );
            return new GenericPageableResponse<Organization>(page);
        }

        if (authorizedUser.getType().equals(User.Type.CUSTOMER)) {
            Page<Organization> page =  organizationRepository.findAllByAccountingIdAndUserId(
                    filter, authorizedUser.getOrganization().getId(), authorizedUser.getId(), pageRequest
            );
            return new GenericPageableResponse<Organization>(page);
        }

        return new GenericPageableResponse<>();
    }

    /* ****************************************************************************************************************
     * CUSTOMERS
     * ************************************************************************************************************* */
    public UserDTO appendCustomer(BigInteger id, UserDTO userDTO, User authorizedUser)
            throws OrganizationNotFoundException, UserNotFoundException, Exception {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
        User user = userRepository.findByUsername(userDTO.getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        organizationRepository.addCustomer(user.getUsername(), organization.getId());

        return UserDTO.fromEntity(user);
    }
    
    public UserDTO removeCustomer(BigInteger id, String username, User authorizedUser)
            throws OrganizationNotFoundException, UserNotFoundException, Exception {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        organizationRepository.removeCustomer(user.getUsername(), organization.getId());

        return UserDTO.fromEntity(user);
    }
    

    public List<UserDTO> fetchCustomers(User authorizedUser) {
        if (authorizedUser.getType().equals(User.Type.ACCOUNTANT)) {
            return UserDTO.fromEntities(userRepository.findCustomersByAccountingId(authorizedUser.getOrganization().getId()));
        }
        return new ArrayList<UserDTO>();
    }
    
    public List<UserDTO> fetchCustomers(BigInteger organizationId, User authorizedUser) {
        if (authorizedUser.getType().equals(User.Type.ACCOUNTANT)) {
            return UserDTO.fromEntities(userRepository.findCustomersByOrganizationId(organizationId));
        }
        return new ArrayList<UserDTO>();
    }

    /* ****************************************************************************************************************
     * INVITED CUSTOMERS
     * ************************************************************************************************************* */
    public List<UserOrganizationInvite> fetchInvitedCustomers(BigInteger id, User authorizedUser) {
        if (authorizedUser.getType().equals(User.Type.ACCOUNTANT)) {
            return userRepository.findCustomersInvitedByOrganizationId(id);
        }
        return new ArrayList<UserOrganizationInvite>();
    }
    
    public Map<String,String> inviteCustomer(BigInteger id, Map<String,String> args, User authorizedUser) 
                throws OrganizationNotFoundException, Exception {
        if (authorizedUser.getType().equals(User.Type.ACCOUNTANT)) {
            String token = UUID.randomUUID().toString();
            String email = args.getOrDefault("email", "");
            if (email.equals("")) {
                throw new IllegalArgumentException("Email cannot be blank.");
            }
            UserOrganizationInvite invite = new UserOrganizationInvite();
            List<UserOrganizationInvite> invites = userOrganizationInviteRepository.findByEmailAndOrganizationId(email, id);
            if (invites.size() == 0) {
                Organization organization = organizationRepository.findById(id)
                    .orElseThrow(() -> new OrganizationNotFoundException("Organization not found."));
                invite.setEmail(email);
                invite.setToken(token);
                invite.setOrganization(organization);
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

    /* ****************************************************************************************************************
     * CREATE - UPDATE - PATCH
     * ************************************************************************************************************* */
    public OrganizationDTO create(OrganizationDTO organizationDTO, User authorizedUser)
            throws OrganizationNotFoundException, OrganizationAlreadyRegisteredException, Exception {
        Organization organization = organizationDTO.toEntity();
        organization.setExternalId(UUID.randomUUID().toString());
        organization.setType(OrganizationTypes.CLIENT.getValue());

        if (organizationDTO.getOrganizationId() == null) {
            if (authorizedUser.getOrganization() != null 
                && authorizedUser.getOrganization().getId() != null) {
                organization.setOrganization(authorizedUser.getOrganization());
            }
        }

        checkIfOrganizationIsNotParentOfItself(organization);

        checkIfOrganizationIsNotAlreadyRegistered(organization);

        return OrganizationDTO.fromEntity(organizationRepository.save(organization));
    }

    public OrganizationDTO update(BigInteger id, OrganizationDTO organizationDTO, User authorizedUser)
            throws OrganizationNotFoundException, OrganizationAlreadyRegisteredException, Exception {
        Organization current = organizationDTO.patch(findById(id, authorizedUser));

        Organization organization = organizationDTO.toEntity();
        organization.setId(current.getId());
        organization.setExternalId(current.getExternalId());
        organization.setAvatar(current.getAvatar());
        organization.setType(current.getType());
        organization.setOrganization(current.getOrganization());

        if (organizationDTO.getOrganizationId() == null) {
            if (authorizedUser.getOrganization() != null 
                && authorizedUser.getOrganization().getId() != null) {
                organizationDTO.setOrganizationId(authorizedUser.getOrganization().getId());
            }
        }

        checkIfOrganizationIsNotParentOfItself(organization);

        checkIfOrganizationIsNotAlreadyRegistered(organization);

        return OrganizationDTO.fromEntity(organizationRepository.save(organization));
    }

    public OrganizationDTO patch(BigInteger id, OrganizationDTO organizationDTO, User authorizedUser)
            throws OrganizationNotFoundException, OrganizationAlreadyRegisteredException, Exception {
        Organization current = organizationDTO.patch(findById(id, authorizedUser));

        checkIfOrganizationIsNotAlreadyRegistered(current);

        return OrganizationDTO.fromEntity(organizationRepository.save(current));
    }


    /* ****************************************************************************************************************
     * VALIDATIONS
     * ************************************************************************************************************* */
    public boolean checkIfOrganizationIsNotAlreadyRegistered(Organization organization) 
            throws OrganizationAlreadyRegisteredException {
        BigInteger accountingId = organization.getOrganization() == null ? null : organization.getOrganization().getId();
        if (organizationRepository.cnpjIsAlreadyRegistered(organization.getCnpj(), organization.getId(), accountingId)) {
            throw new OrganizationAlreadyRegisteredException("A organization with that cnpj is already registered.");
        }
        return true;
    }

    public boolean checkIfOrganizationIsNotParentOfItself(Organization organization) 
            throws Exception {
        if (organization != null && organization.getOrganization() != null) {
            if (organization.getId() != null && organization.getOrganization().getId() != null
                && organization.getId().compareTo(organization.getOrganization().getId()) == 0) {
                System.out.println("A organization cannot be a parent of itself.");
                throw new Exception("A organization cannot be a parent of itself.");
            }
        }
        return true;
    }

}
