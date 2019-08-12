package br.com.ottimizza.application.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import br.com.ottimizza.application.domain.exceptions.PasswordResetTokenInvalidException;
import br.com.ottimizza.application.model.PasswordResetToken;
import br.com.ottimizza.application.model.user.User;

class SecurityServiceTest {

	SecurityService securityService;

	User user;

	@BeforeAll
	void setup() {
		this.securityService = new SecurityService();

		this.user = new User();
		this.user.setUsername("test@ottimizza.com.br");
	}

	@Test
	void whenTokenValidForUser() {
		PasswordResetToken passwordResetToken = new PasswordResetToken(null, "token", user, new Date(2019, 2, 20));
		try {
			Assertions.assertTrue(
					securityService.validatePasswordResetTokenForUser("test@ottimizza.com.br", passwordResetToken));
		} catch (PasswordResetTokenInvalidException exception) {
			fail("PasswordResetTokenInvalidException was thrown, but no exception should be expected.");
		}
	}

	@Test
	void whenTokenInvalidForUser() {
		PasswordResetToken passwordResetToken = new PasswordResetToken(null, "token", user, new Date(2019, 2, 20));

		Assertions.assertThrows(PasswordResetTokenInvalidException.class, () -> {
			securityService.validatePasswordResetTokenForUser("test2@ottimizza.com.br", passwordResetToken);
		});
	}
}
