package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import javax.annotation.Resource;
import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.dtos.models.invitation.InvitationDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.users.UserNotFoundException;
import br.com.ottimizza.application.domain.mappers.InvitationMapper;
import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.repositories.clients.OAuthClientRepository;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
/* ********************************** **
 * Entities
 * ********************************** */
import br.com.ottimizza.application.model.OAuthClientAdditionalInformation;
import br.com.ottimizza.application.model.OAuthClientDetails;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;

/* ********************************** **
 * Repositories
 * ********************************** */
import br.com.ottimizza.application.repositories.users.UsersRepository;
import br.com.ottimizza.application.repositories.UserOrganizationInviteRepository;

@Service // @formatter:off
public class InvitationService {

    @Inject
    UsersRepository userRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    UserOrganizationInviteRepository userOrganizationInviteRepository;
    
    @Inject
    MailServices mailServices;

    public UserOrganizationInvite invite(UserOrganizationInvite inviteDetails, Principal principal) 
            throws UserNotFoundException, OrganizationNotFoundException, Exception {
        User authenticated =  userRepository.findByUsername(principal.getName())
                                            .orElseThrow(() -> new UserNotFoundException(""));

        if (inviteDetails.getEmail() == null || inviteDetails.getEmail().equals("")) {
            throw new IllegalArgumentException("Informe o e-mail para enviar o convite!");
        }
        if (inviteDetails.getType() == null || (inviteDetails.getType() < 0 || inviteDetails.getType() > 2)) {
            throw new IllegalArgumentException("Informe o tipo de usuário para enviar o convite!");
        }
        
        if(inviteDetails.getType() == 1) {
        	UserOrganizationInvite invitedUser = findInviteByEmail(inviteDetails.getEmail());
        	User user = userRepository.findByEmail(inviteDetails.getEmail());
        	if(invitedUser != null) throw new IllegalArgumentException("Email nao valido convite");
        	
        	else if(user != null) 	throw new IllegalArgumentException("Email nao valido usuario");
        }
        else {
        	UserOrganizationInvite invitedUser = findInviteByEmailAndOrganizationId(inviteDetails.getEmail(), inviteDetails.getOrganization().getId());
        	User user = userRepository.findByEmail(inviteDetails.getEmail());
        	if(invitedUser != null) throw new IllegalArgumentException("Email nao valido convite");
        	
           	else if(user != null) throw new IllegalArgumentException("Email nao valido usuario");
        	
        }
       	
       
        
        /*if (authenticated.getType().equals(User.Type.ADMINISTRATOR)) {
            inviteDetails.setToken(UUID.randomUUID().toString());

            String email = inviteDetails.getEmail();
            Organization reference = findOrganization(inviteDetails.getOrganization());

            if (reference == null) {
                reference = authenticated.getOrganization();
                inviteDetails.setOrganization(reference);
            } else {
                inviteDetails.setOrganization(reference);
                // garante que se o usuario escolher uma empresa cliente, 
                // o usuario criado sera um cliente (Tipo 2).
                inviteDetails.setType(reference.getType()); 
            }

            // busca um convite existente.
            UserOrganizationInvite existent = findInviteByEmailAndOrganizationId(email, reference.getId());

            if (existent != null) {
                inviteDetails = existent; // se já existir usa a referencia.
            } else { // senao, cria um novo convite.
                inviteDetails = userOrganizationInviteRepository.save(inviteDetails);
            }

            // envia email. :)
            this.sendInvitation(inviteDetails, authenticated);

        }
        if (authenticated.getType().equals(User.Type.ACCOUNTANT)) {*/
        	
            inviteDetails.setToken(UUID.randomUUID().toString());
            
            if (inviteDetails.getOrganization() == null) {
                //inviteDetails.setType(User.Type.ACCOUNTANT);
                inviteDetails.setOrganization(authenticated.getOrganization());
            } else if (inviteDetails.getOrganization().getId() != null) {
                Organization organization = organizationRepository.fetchById(inviteDetails.getOrganization().getId());
                if (organization == null) {
                    throw new IllegalArgumentException("Não foi encontrada nenhuma empresa com o ID informado!");
                }
                //inviteDetails.setType(organization.getType());
                inviteDetails.setOrganization(organization);
            } else if (inviteDetails.getOrganization().getCnpj() != null && !inviteDetails.getOrganization().getCnpj().equals("")) {
                Organization organization = organizationRepository.fetchByCnpj(inviteDetails.getOrganization().getCnpj());
                if (organization == null) {
                    throw new IllegalArgumentException("Não foi encontrada nenhuma empresa com o CNPJ informado!");
                }
                //inviteDetails.setType(organization.getType());
                inviteDetails.setOrganization(organization);
            }
            List<UserOrganizationInvite> invites = userOrganizationInviteRepository.findByEmailAndOrganizationId(
                inviteDetails.getEmail(), inviteDetails.getOrganization().getId()
            );

            if (invites.size() == 0) {
                inviteDetails = userOrganizationInviteRepository.save(inviteDetails);
            } else {
                inviteDetails = invites.get(0);
            }

            this.sendInvitation(inviteDetails, authenticated);
        //}
            return inviteDetails;
    }
    
    public Page<UserOrganizationInvite> fetchInvitedUsers(String email, Integer pageIndex, Integer pageSize, Principal principal)
            throws Exception {
        User authenticated = userRepository.findByUsername(principal.getName())
                                            .orElseThrow(() -> new UserNotFoundException(""));
        return userOrganizationInviteRepository.fetchInvitedUsersByAccountingId(
            email, authenticated.getOrganization().getId(), PageRequest.of(pageIndex, pageSize));
    }

   

    private UserOrganizationInvite findInviteByEmailAndOrganizationId(String email, BigInteger organizationId) {
        List<UserOrganizationInvite> invites = userOrganizationInviteRepository.findByEmailAndOrganizationId(
            email, organizationId
        );
        if (invites.size() == 0) {
            return null;
        } else {
            return invites.get(0);
        }
    }
    
    private UserOrganizationInvite findInviteByEmail(String email) {
        List<UserOrganizationInvite> invites = userOrganizationInviteRepository.findByEmail(email);
        if (invites.size() == 0) {
            return null;
        } else {
            return invites.get(0);
        }
    }

    @Async
    private void sendInvitation(UserOrganizationInvite invite, User authorizedUser) {
        String accountingName = authorizedUser.getOrganization().getName();
        String subject = MessageFormat.format("Conta {0}.", accountingName);
        String template = mailServices.inviteCustomerTemplate(authorizedUser, invite.getToken());
        MailServices.Builder messageBuilder = new MailServices.Builder()
                .withName(accountingName)
                .withTo(invite.getEmail())
                .withCc(authorizedUser.getOrganization().getEmail())
                .withSubject(subject).withHtml(template);
        mailServices.send(messageBuilder); 
    }

    //
    //
    //
    //]
    //
    //
    //
    public InvitationDTO fetchInvitationByToken(String token) throws IllegalArgumentException, Exception {
        return InvitationMapper.fromEntity(
            userOrganizationInviteRepository.fetchByToken(token).orElseThrow(() 
                    -> new IllegalArgumentException("Convite não encontrado!"))
        );
    }

    public InvitationDTO inviteAccountant(InvitationDTO invitationDTO) 
                                            throws IllegalArgumentException, 
                                                    OrganizationAlreadyRegisteredException, 
                                                    OrganizationNotFoundException, 
                                                    Exception {
        UserOrganizationInvite invitation = InvitationMapper.fromDTO(invitationDTO);

        // Validação de convite.
        this.validateAccountantInvitation(invitation);

        invitation.setToken(UUID.randomUUID().toString());

        // Contabilidade relacionada.
        Organization accounting = invitation.getOrganization();
        accounting.setType(Organization.Type.ACCOUNTING);
        accounting.setCnpj(accounting.getCnpj().replace("\\D+", ""));
        invitation.setOrganization(accounting);

        // Produtos e Autoridades.
        invitation.setProducts("2");
        invitation.setAuthorities("READ;WRITE;ADMIN");

        invitation = userOrganizationInviteRepository.save(invitation);

        // Envio de E-mail.
        sendAccountantInvitation(invitation);

        return InvitationMapper.fromEntity(invitation);
    }

    private boolean validateAccountantInvitation(UserOrganizationInvite invitation) 
            throws IllegalArgumentException, Exception {
        List<Integer> tiposPermitidos = Arrays.asList(
            User.Type.ADMINISTRATOR, User.Type.ACCOUNTANT, User.Type.CUSTOMER
        );
        
        if (invitation.getEmail() == null || invitation.getEmail().equals("")) {
            throw new IllegalArgumentException("Informe o e-mail para enviar o convite!");
        }
        
        if (!tiposPermitidos.contains(invitation.getType())) {
            throw new IllegalArgumentException("Informe o tipo de usuário para enviar o convite!");
        }


        User existingUser = userRepository.findByEmail(invitation.getEmail());
        if(existingUser != null){
            throw new IllegalArgumentException("O email informado já está registrado!");
        }


        if(invitation.getType() == User.Type.ACCOUNTANT) {
        	UserOrganizationInvite existingInvitation = findInviteByEmail(invitation.getEmail());
            if (existingInvitation != null) {
                throw new IllegalArgumentException("Já existe um convite para esse endereço de email!");
            }
        } else {
        	UserOrganizationInvite existingInvitation = findInviteByEmailAndOrganizationId(
                invitation.getEmail(), invitation.getOrganization().getId()
            );
            if (existingInvitation != null) {
                throw new IllegalArgumentException("Já existe um convite para esse endereço de email!");
            }
        	
        }
        Organization accounting = invitation.getOrganization();
        if (accounting.getName() == null || accounting.getName().equals("")) {
            throw new IllegalArgumentException("Informe o nome da contabilidade!");
        }
        if (accounting.getCnpj() == null || accounting.getCnpj().equals("")) {
            throw new IllegalArgumentException("Informe o CNPJ da contabilidade!");
        }
        accounting.setCnpj(accounting.getCnpj().replace("\\D+", ""));
        if (organizationRepository.cnpjIsAlreadyRegistered(accounting.getCnpj(), Organization.Type.ACCOUNTING, null, null)) {
            throw new OrganizationAlreadyRegisteredException("Já existe uma contabilidade com o CNPJ informado!");
        }
        return true;
    }

    @Async
    private void sendAccountantInvitation(UserOrganizationInvite invitation) {
        String accountingName = invitation.getOrganization().getName();
        String subject = MessageFormat.format("Conta {0}.", accountingName);
        String template = mailServices.accountantInvitation(invitation.getToken());
        MailServices.Builder messageBuilder = new MailServices.Builder()
                .withName(accountingName)
                .withTo(invitation.getEmail())
                .withSubject(subject).withHtml(template);
        mailServices.send(messageBuilder); 
    }

}
