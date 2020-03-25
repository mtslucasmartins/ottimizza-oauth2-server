package br.com.ottimizza.application.controllers.sessions;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sessions")
public class HttpSessionController {

    @Inject
    FindByIndexNameSessionRepository<?> sessionRepository;

    @GetMapping
    public ResponseEntity<?> fetch(OAuth2Authentication authentication) throws Exception {
        return ResponseEntity.ok(sessionRepository.findByPrincipalName(authentication.getName()));
    }   

}