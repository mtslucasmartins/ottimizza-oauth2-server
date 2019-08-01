package br.com.ottimizza.application.service;

import java.math.BigInteger;
import java.security.Principal;

import javax.inject.Inject;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.UserNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class UserService {

    @Inject
    UsersRepository userRepository;

    public User findByUsername(String username) throws UserNotFoundException, Exception {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User not found."));
    }

}
