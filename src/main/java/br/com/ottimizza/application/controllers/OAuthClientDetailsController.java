package br.com.ottimizza.application.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.responses.ErrorResponse;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.model.OAuthClientAdditionalInformation;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.services.OAuthService;
import br.com.ottimizza.application.services.UserService;

@RestController // @formatter:off
@RequestMapping(value = "/api/oauth_clients")
public class OAuthClientDetailsController {

    @Inject
    OAuthService oAuthService;

    @Inject
    UserService userService;

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public HttpEntity<?> save(@RequestBody OAuthClientAdditionalInformation additionalInformation, Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(oAuthService.save(additionalInformation, authorizedUser));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("oauth_client_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

}
