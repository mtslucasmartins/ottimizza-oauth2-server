package br.com.ottimizza.application.controllers;

import java.security.Principal;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.model.PasswordResetToken;
import br.com.ottimizza.application.model.User;

import br.com.ottimizza.application.service.SecurityService;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

// Repositories
import br.com.ottimizza.application.repositories.PasswordRecoveryRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@RestController //@formatter:off 
@RequestMapping(value = "/user")
public class UserController {

    @Inject
    SecurityService securityService;

    @Inject
    UsersRepository userRepository;

    @Inject
    PasswordRecoveryRepository passwordRecoveryRepository;

    @RequestMapping("/info")
    public Principal getCurrentLoggedInUser(Principal user) {
        return user;
    }

    @RequestMapping(value = "/info/username", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> save(Principal principal) {
        return ResponseEntity.ok(principal.getName());
    }

    @RequestMapping(value = { "", "/" }, method = RequestMethod.POST)
    public ResponseEntity<User> save(@RequestBody User user, Principal principal) {

        // Encrypts the Password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        // Persists the User.
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

}
