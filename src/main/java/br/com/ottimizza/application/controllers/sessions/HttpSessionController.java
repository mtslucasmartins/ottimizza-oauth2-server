package br.com.ottimizza.application.controllers.sessions;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.configuration.session.CustomSession;

@RestController
@RequestMapping("/api/v1/sessions")
public class HttpSessionController {

    @Inject
    FindByIndexNameSessionRepository<Session> sessionRepository;

    @GetMapping
    public ResponseEntity<?> fetch(OAuth2Authentication authentication) throws Exception {

        return ResponseEntity.ok(sessionRepository.findByPrincipalName(authentication.getName())
            .entrySet()
                .stream()
                    .map(e -> new AbstractMap.SimpleEntry<String, CustomSession>(
                        e.getKey(), new CustomSession(e.getValue())
                    ))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
    } 

}