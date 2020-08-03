package br.com.ottimizza.application.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.application.SpringbootOauth2ServerApplication;
import br.com.ottimizza.application.domain.dtos.models.invitation.InvitationDTO;
import br.com.ottimizza.application.domain.exceptions.PasswordResetTokenInvalidException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.PasswordResetToken;
import br.com.ottimizza.application.model.user.User;

// @formatter:off
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootOauth2ServerApplication.class)
class InvitationServiceTest {

    @Inject
    InvitationService invitationService;

    @Test
    public void givenInvitationDTO_whenSaveAccountantInvitationAndRetreive_thenOK() throws Exception { 
        InvitationDTO invitationDTO = InvitationDTO.builder()
				.email("00000000000101")
				.type(User.Type.ACCOUNTANT)
				.userDetails(null)
				.organization(null)
				.build();

        InvitationDTO created = invitationService.inviteAccountant(invitationDTO);
        
		Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
	}
	// @Test
	// void inviteAccountant() {

	// 	String email = "user01@invitation.com.br";
        
    //     this.user = new User();
	// 	this.user.setUsername("test@ottimizza.com.br");
	// 	PasswordResetToken passwordResetToken = new PasswordResetToken(null, "token", user, new Date(2019, 2, 20));
	// 	try {
	// 		Assertions.assertTrue(
	// 				securityService.validatePasswordResetTokenForUser("test@ottimizza.com.br", passwordResetToken));
	// 	} catch (PasswordResetTokenInvalidException exception) {
	// 		fail("PasswordResetTokenInvalidException was thrown, but no exception should be expected.");
	// 	}
	// }

	// @Test
	// void whenTokenInvalidForUser() {
	// 	this.securityService = new SecurityService();

	// 	this.user = new User();
	// 	this.user.setUsername("test@ottimizza.com.br");
	// 	PasswordResetToken passwordResetToken = new PasswordResetToken(null, "token", user, new Date(2019, 2, 20));

	// 	Assertions.assertThrows(PasswordResetTokenInvalidException.class, () -> {
	// 		securityService.validatePasswordResetTokenForUser("test2@ottimizza.com.br", passwordResetToken);
	// 	});
	// }
}
