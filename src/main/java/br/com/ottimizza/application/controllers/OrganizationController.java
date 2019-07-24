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
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.responses.ErrorResponse;
import br.com.ottimizza.application.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.service.OrganizationService;

@RestController
@RequestMapping(value = "/api/organizations")
public class OrganizationController {

    @Inject
    OrganizationRepository organizationRepository;

    @Inject
    OrganizationService organizationService;

    @GetMapping
    public List<Organization> findAll(Model model, Principal user) {
        return organizationRepository.findAll();
    }

    @PostMapping
    public HttpEntity save(@RequestBody Organization organization, Principal user) {
        try {
            return ResponseEntity.ok(organizationService.save(organization));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "ex.getMessage()"));
        }
    }

    @GetMapping("/{id}")
    public HttpEntity findById(@PathVariable("id") BigInteger id, Model model, Principal user) {
        // return organizationRepository.findById(id).orElse(new Organization());
        try {
            return ResponseEntity.ok(organizationService.findById(id));
        } catch (OrganizationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("organization_not_found", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "ex.getMessage()"));
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HttpEntity save(@PathVariable("id") BigInteger id, @RequestBody Organization organization, Principal user) {
        try {
            return ResponseEntity.ok(organizationService.save(organization));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "ex.getMessage()"));
        }
    }

}
