package br.com.ottimizza.application.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.ottimizza.application.domain.exceptions.PasswordResetTokenExpiredException;
import br.com.ottimizza.application.domain.exceptions.PasswordResetTokenInvalidException;
import br.com.ottimizza.application.model.PasswordResetToken;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.PasswordRecoveryRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Service
public class SecurityService {

    @Inject
    UsersRepository userRepository;

    @Inject
    PasswordRecoveryRepository passwordRecoveryRepository;

    private void validatePasswordResetTokenForUser(String username, PasswordResetToken passwordRecoveryToken)
            throws PasswordResetTokenInvalidException {
        if (passwordRecoveryToken.getUser().getUsername().equals(username) || passwordRecoveryToken == null) {
            throw new PasswordResetTokenInvalidException("invalid");
        }
    }

    private void validatePasswordResetTokenExpiryDate(PasswordResetToken passwordRecoveryToken)
            throws PasswordResetTokenExpiredException {
        if (Calendar.getInstance().getTime().getTime() > passwordRecoveryToken.getExpiryDate().getTime()) {
            throw new PasswordResetTokenExpiredException("expired");
        }
    }

    public String validatePasswordRecoveryToken(String username, String token, HttpServletRequest request) { // @formatter:off
        PasswordResetToken passwordRecoveryToken = passwordRecoveryRepository.findByToken(token);
        try {
            validatePasswordResetTokenForUser(username, passwordRecoveryToken);  
            validatePasswordResetTokenExpiryDate(passwordRecoveryToken);
        } catch (PasswordResetTokenExpiredException expiredEx) {
            return expiredEx.getMessage();
        } catch (PasswordResetTokenInvalidException invalidEx) {
            return invalidEx.getMessage();
        }
        // this.authenticate(user, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")), request);
        return null;
    }
    
    // //@formatter:on
    // public void authenticate(String login, String password, HttpServletRequest
    // httpRequest) {
    // UsernamePasswordAuthenticationToken token = new
    // UsernamePasswordAuthenticationToken(login, password);
    // token.setDetails(new WebAuthenticationDetails(httpRequest));
    // ServletContext servletContext = httpRequest.getSession().getServletContext();
    // WebApplicationContext wac =
    // WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
    // AuthenticationManager authManager = wac.getBean(AuthenticationManager.class);
    // Authentication authentication = authManager.authenticate(token);
    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // }

    // //@formatter:off 
    // public void authenticate(User user, List<SimpleGrantedAuthority> authorities, HttpServletRequest httpRequest) {
    //     Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
    //             AuthorityUtils.createAuthorityList("CHANGE_PASSWORD_PRIVILEGE")
    //     );
    //     SecurityContextHolder.getContext().setAuthentication(authentication);
    // }

}
