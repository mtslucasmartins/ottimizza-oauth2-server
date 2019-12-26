package br.com.ottimizza.application.controllers;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Map;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.dtos.criterias.SearchCriteria;
import br.com.ottimizza.application.domain.responses.GenericPageableResponse;
import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.services.UserService;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController // @formatter:off
@RequestMapping(value = "/api/v1/users")
public class UsersController {

    @Inject
    UserService userService;

    @GetMapping 
    public HttpEntity<?> fetchAll(@ModelAttribute UserDTO filter,
                                  @ModelAttribute SearchCriteria criteria, 
                                  Principal principal) throws Exception {
        return ResponseEntity.ok(
            new GenericPageableResponse<UserDTO>(userService.fetchAll(filter, criteria, principal)));
    }

    @GetMapping("/{id}")
    public HttpEntity<?> fetchById(@PathVariable("id") BigInteger id, Principal principal) throws Exception {
        return ResponseEntity.ok(
            new GenericResponse<UserDTO>(UserDTO.fromEntityWithOrganization(userService.findById(id))));
    }

    @PostMapping 
    public HttpEntity<?> create(@RequestBody UserDTO userDTO, Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericResponse<UserDTO>(userService.create(userDTO, principal)));
    }

    @PatchMapping("/{id}")
    public HttpEntity<?> patch(@PathVariable("id") BigInteger id, @RequestBody UserDTO userDTO, Principal principal)
            throws Exception {
        return ResponseEntity.ok(new GenericResponse<UserDTO>(userService.patch(id, userDTO, principal)));
    }

    /* ********************************************************************************************* *
     *
     * Organizations
     * 
     * ********************************************************************************************* */
    @GetMapping("/{id}/organizations") 
    public HttpEntity<?> fetchOrganization(@PathVariable("id") BigInteger id, @ModelAttribute OrganizationDTO filter, 
                                           @ModelAttribute SearchCriteria criteria, Principal principal) throws Exception {
        return ResponseEntity.ok(
                new GenericPageableResponse<OrganizationDTO>(userService.fetchOrganizations(id, filter, criteria, principal))
        );
    }

    @PostMapping("/{id}/organizations") 
    public HttpEntity<?> appendOrganization(@PathVariable("id") BigInteger id,
            @RequestBody OrganizationDTO organizationDTO, Principal principal) throws Exception {
        return ResponseEntity.ok(
                new GenericResponse<OrganizationDTO>(userService.appendOrganization(id, organizationDTO, principal))
        );
    }

    /* ********************************************************************************************* *
     *
     * Invtations
     * 
     * ********************************************************************************************* */
    @GetMapping("/invited")
    public HttpEntity<?> fetchAllInvitedUsers(@ModelAttribute UserDTO filter,
            @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize, Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericPageableResponse<UserOrganizationInvite>(
                userService.fetchInvitedUsers(filter.getEmail(), pageIndex, pageSize, principal)));
    }

    @PostMapping("/invite")
    public HttpEntity<?> invite(@RequestBody Map<String, String> args, Principal principal) throws Exception {
        User authorizedUser = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(new GenericResponse<Map<String, String>>(userService.invite(args, authorizedUser)));
    }

    /**
     * 
     * IMPORTAÇÃO
     * 
     */
    @PostMapping("/import")
    public HttpEntity<?> upload(@RequestParam("file") MultipartFile file, Principal principal) throws Exception {
        return ResponseEntity.ok("");
    }

}
