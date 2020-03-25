package br.com.ottimizza.application.configuration.session.handlers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
// import org.springframework.session.FindByPrincipalNameSessionRepository;

public class SpringSessionPrincipalNameSuccessHandler implements AuthenticationSuccessHandler {

    private final String PRINCIPAL_NAME = "username";

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		HttpSession session = request.getSession();
		String username = authentication.getName();

		session.setAttribute(PRINCIPAL_NAME, username);
    }
    
}
