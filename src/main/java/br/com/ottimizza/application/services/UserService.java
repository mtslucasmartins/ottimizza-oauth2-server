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

    public User findByUsername(String username) throws UserNotFoundException, Exception {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

    // @formatter:off
    public Page<UserDTO> findAllByAccountingId(String filter, int pageIndex, int pageSize, Principal principal)
            throws UserNotFoundException, Exception {
        User authorizedUser = findByUsername(principal.getName());
        return userRepository.findAllByAccountingId(
            "%" + filter + "%", authorizedUser.getOrganization().getId(), PageRequest.of(pageIndex, pageSize)
        ).map(UserDTO::fromEntity);
    } // @formatter:on

    public boolean checkIfEmailIsAlreadyRegistered(User user) throws UserAlreadyRegisteredException {
        if (userRepository.emailIsAlreadyRegistered(user.getEmail())) {
            System.out.println("A user with that email address is already registered.");
            throw new UserAlreadyRegisteredException("A user with that email address is already registered.");
        }
        return true; 
    }

}
