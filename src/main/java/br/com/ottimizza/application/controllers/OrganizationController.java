package br.com.ottimizza.application.controllers;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.responses.ErrorResponse;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;
import br.com.ottimizza.application.service.OrganizationService;
import br.com.ottimizza.application.service.UserService;

@RestController // @formatter:off
@RequestMapping(value = "/api/organizations")
public class OrganizationController {

    @Inject
    UserService userService;

    @Inject
    OrganizationService organizationService;

    @GetMapping
    public HttpEntity findAll(@RequestParam(name = "filter", defaultValue = "") String filter,
                              @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                              @RequestParam(name = "page_size", defaultValue = "10") int pageSize, 
                              Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.findAll(filter, pageIndex, pageSize, authorizedUser));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PostMapping
    public HttpEntity save(@RequestBody Organization organization, 
                           Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.save(organization, authorizedUser));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @GetMapping("/{id}")
    public HttpEntity findById(@PathVariable("id") BigInteger id, 
                               Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.findById(id, authorizedUser));
        } catch (OrganizationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("organization_not_found", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity save(@PathVariable("id") BigInteger id, 
                           @RequestBody Organization organization,
                           Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.save(organization, authorizedUser));
        } catch (OrganizationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("organization_not_found", ex.getMessage()));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

}
