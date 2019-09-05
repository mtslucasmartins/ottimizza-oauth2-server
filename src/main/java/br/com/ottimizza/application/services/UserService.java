package br.com.ottimizza.application.services;

import java.math.BigInteger;
import java.security.Principal;

import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import antlr.collections.List;
import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.UserAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.UserNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class UserService {

    @Inject
    UsersRepository userRepository;

    public User findById(BigInteger id) throws UserNotFoundException, Exception {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    public User findByUsername(String username) throws UserNotFoundException, Exception {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    // @formatter:off
    public Page<UserDTO> fetchAll(UserDTO filter, int pageIndex, int pageSize, Principal principal)
            throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());

        
        if (authorizedUser.getType().equals(User.Type.CUSTOMER)) {
            // se usuario for do tipo cliente, visualiza apenas usuarios
            // vinculados as empresas da qual pertence.
            return userRepository.fetchCustomersByCustomerId(
                authorizedUser.getId(), filter.getUsername(), filter.getEmail(), 
                filter.getFirstName(), filter.getLastName(), PageRequest.of(pageIndex, pageSize)
            ).map(UserDTO::fromEntity);
        }
        

        return userRepository.fetchAll(
            filter, PageRequest.of(pageIndex, pageSize), authorizedUser
        ).map(UserDTO::fromEntity);
    } // @formatter:on

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
    ///
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
