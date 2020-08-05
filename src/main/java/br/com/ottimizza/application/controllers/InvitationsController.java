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

import br.com.ottimizza.application.domain.dtos.models.invitation.InvitationDTO;
import br.com.ottimizza.application.domain.dtos.responses.GenericResponse;
import br.com.ottimizza.application.domain.responses.GenericPageableResponse;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.services.InvitationService;

@RestController // @formatter:off
@RequestMapping("/api/v2/invitations")
public class InvitationsController {

    @Inject
    InvitationService invitationService;

    @GetMapping
    public ResponseEntity<?> fetch(@RequestParam(name = "page_index", required = false, defaultValue = "0") int pageIndex,
                                   @RequestParam(name = "page_size", required = false, defaultValue = "10") int pageSize, 
                                   @RequestParam(name = "token", required = false, defaultValue = "") String token, 
                                   Principal principal) throws Exception {
        // validar se principal null > obrigar token arg
        if (principal == null) {
            if (!token.equals("")) {
                return ResponseEntity.ok(new GenericResponse<InvitationDTO>(
                    this.invitationService.fetchInvitationByToken(token))
                );
            }
        }

        return ResponseEntity.ok(new GenericPageableResponse<UserOrganizationInvite>(
                invitationService.fetchInvitedUsers("", pageIndex, pageSize, principal)));
    }

    @PostMapping
    public ResponseEntity<?> sendInvitation(@RequestBody InvitationDTO invitation, Principal principal)
            throws Exception {
        if (principal == null) {
            return ResponseEntity.ok(new GenericResponse<InvitationDTO>(
                this.invitationService.inviteAccountant(invitation))
            );
        } else {
            return ResponseEntity.ok("{}");
        }
    }

}