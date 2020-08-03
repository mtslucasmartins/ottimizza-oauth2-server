package br.com.ottimizza.application.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.responses.GenericPageableResponse;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.services.InvitationService;

@RestController // @formatter:off
@RequestMapping("/api/v2/invitations")
public class InvitationsController {

    @Inject
    InvitationService invitationService;

    @GetMapping
    public ResponseEntity<?> fetch(@RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                   @RequestParam(name = "page_size", defaultValue = "10") int pageSize, 
                                   Principal principal) throws Exception {
        // validar se principal null > obrigar token arg
        if (principal == null) {
            return ResponseEntity.ok("{}");
        }
        return ResponseEntity.ok(new GenericPageableResponse<UserOrganizationInvite>(
                invitationService.fetchInvitedUsers("", pageIndex, pageSize, principal)));
    }

    @PostMapping
    public ResponseEntity<?> fetch(@RequestBody UserOrganizationInvite invitation, Principal principal)
            throws Exception {
        return ResponseEntity.ok("");
    }

}