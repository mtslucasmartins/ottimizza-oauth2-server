package br.com.ottimizza.application.configuration.session.handlers;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class CompositeAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private List<AuthenticationSuccessHandler> handlers;

	public CompositeAuthenticationSuccessHandler(AuthenticationSuccessHandler... handlers) {
		super();
		this.handlers = Arrays.asList(handlers);
	}

	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		for(AuthenticationSuccessHandler handler : handlers) {
			handler.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
