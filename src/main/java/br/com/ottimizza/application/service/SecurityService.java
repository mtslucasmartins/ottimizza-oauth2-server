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

import br.com.ottimizza.application.exceptions.ExpiredPasswordResetTokenException;
import br.com.ottimizza.application.exceptions.InvalidPasswordResetTokenException;
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

    private void validateTokenForUser(String username, PasswordResetToken passwordRecoveryToken)
            throws InvalidPasswordResetTokenException {
        if (passwordRecoveryToken.getUser().getUsername().equals(username) || passwordRecoveryToken == null) {
            throw new InvalidPasswordResetTokenException("invalid");
        }
    }

    private void validateTokenExpiryDate(PasswordResetToken passwordRecoveryToken)
            throws ExpiredPasswordResetTokenException {
        long currentTime = Calendar.getInstance().getTime().getTime();
        long passwordRecoveryTokenExpiryTime = passwordRecoveryToken.getExpiryDate().getTime();
        if (currentTime > passwordRecoveryTokenExpiryTime) {
            throw new ExpiredPasswordResetTokenException("expired");
        }
    }

    public String validatePasswordRecoveryToken(String username, String token, HttpServletRequest request) { // @formatter:off
        PasswordResetToken passwordRecoveryToken = passwordRecoveryRepository.findByToken(token);
        User user = passwordRecoveryToken.getUser();        

        try {
            validateTokenForUser(username, passwordRecoveryToken);  
            validateTokenExpiryDate(passwordRecoveryToken);
        } catch (ExpiredPasswordResetTokenException expiredEx) {
            return expiredEx.getMessage();
        } catch (InvalidPasswordResetTokenException invalidEx) {
            return invalidEx.getMessage();
        }

        this.authenticate(user, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")), request);
        // this.authenticate(user.getUsername(), "", request);
        
        // UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(
        //         user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"))
        // );//new UsernamePasswordAuthenticationToken (user, pass);
        
        // System.out.println("Teste0.1");
        // Authentication auth = authManager.authenticate(authReq);
        // System.out.println("Teste0.2");
        // SecurityContext sc = SecurityContextHolder.getContext();
        // System.out.println("Teste0.3");
        // sc.setAuthentication(auth);
        // System.out.println("Teste0.4");
        // // // cria um contexto de autenticação para o usuario com o scope para alteração de senha.
        // // 
        // // Authentication authentication = new UsernamePasswordAuthenticationToken(
        // //         user, null, Arrays.asList(new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE"))
        // // );
        // // SecurityContext securityContext = SecurityContextHolder.getContext();
        // // securityContext.setAuthentication(authentication);
        // System.out.println("Teste1");
        // // // Create a new session and add the security context.
        // // HttpSession session = request.getSession(true);
        // // session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        // HttpSession session = request.getSession(true);
        // session.setAttribute("SPRING_SECURITY_CONTEXT", sc);
        // System.out.println("Teste2");

        return null;
    }
    
    //@formatter:on
    public void authenticate(String login, String password, HttpServletRequest httpRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(login, password);
        token.setDetails(new WebAuthenticationDetails(httpRequest));
        ServletContext servletContext = httpRequest.getSession().getServletContext();
        WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        AuthenticationManager authManager = wac.getBean(AuthenticationManager.class);
        Authentication authentication = authManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    //@formatter:off 
    public void authenticate(User user, List<SimpleGrantedAuthority> authorities, HttpServletRequest httpRequest) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
                AuthorityUtils.createAuthorityList("CHANGE_PASSWORD_PRIVILEGE")
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
