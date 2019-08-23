package br.com.ottimizza.application.controllers;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.responses.ErrorResponse;
import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.services.OrganizationService;
import br.com.ottimizza.application.services.UserService;

@RestController // @formatter:off
@RequestMapping(value = "/api/organizations")
public class OrganizationController {

    @Inject
    UserService userService;

    @Inject
    OrganizationService organizationService;

    //
    @GetMapping
    public HttpEntity<?> findAll(@RequestParam(name = "filter", defaultValue = "") String filter,
                              @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                              @RequestParam(name = "page_size", defaultValue = "10") int pageSize, 
                              Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.findAll(
                filter, PageRequest.of(pageIndex, pageSize), authorizedUser)
            );
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    /********************************************************************************************* **
     * CREATE UPDATE PATCH
     * ******************************************************************************************* */
    @PostMapping
    public HttpEntity<?> create(@RequestBody OrganizationDTO organizationDTO,  
            Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.create(organizationDTO, authorizedUser));
        } catch (OrganizationAlreadyRegisteredException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResponse("organization_already_exists", ex.getMessage()));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PutMapping("/{id}")
    public HttpEntity<?> update(@PathVariable("id") BigInteger id, 
                                @RequestBody OrganizationDTO organizationDTO, 
                                Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());

            OrganizationDTO updated = organizationService.update(id, organizationDTO, authorizedUser);

            return ResponseEntity.ok(new GenericResponse<OrganizationDTO>( updated ));

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

    @PatchMapping("/{id}")
    public HttpEntity<?> patch(@PathVariable("id") BigInteger id, 
                               @RequestBody OrganizationDTO organizationDTO, 
                               Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());

            OrganizationDTO patched = organizationService.patch(id, organizationDTO, authorizedUser);

            return ResponseEntity.ok(new GenericResponse<OrganizationDTO>( patched ));

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

    //
    //
    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable("id") BigInteger id, 
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

    @GetMapping("/uuid/{externalId}")
    public HttpEntity<?> findByExternalId(@PathVariable("externalId") String externalId, 
                               Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.findByExternalId(externalId, authorizedUser));
        } catch (OrganizationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("organization_not_found", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    
    /********************************************************************************************* **
     * CUSTOMERS
     * ******************************************************************************************* */
    @GetMapping("/{id}/customers")
    public HttpEntity<?> fetchCustomers(@PathVariable("id") BigInteger id, Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());

            GenericResponse<UserDTO> response = new GenericResponse<UserDTO>( 
                organizationService.fetchCustomers(id, authorizedUser) 
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PostMapping("/{id}/customers")
    public HttpEntity<?> appendCustomer(@PathVariable("id") BigInteger id, 
                                                       @RequestBody UserDTO userDTO,
                                                       Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());

            GenericResponse<UserDTO> response = new GenericResponse<UserDTO>( 
                organizationService.appendCustomer(id, userDTO, authorizedUser) 
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }
    
    
    /********************************************************************************************* **
     * INVITED CUSTOMERS 
     * ******************************************************************************************* */
    @GetMapping("/{id}/customers/invited")
    public HttpEntity<?> fetchInvitedCustomers(@PathVariable("id") BigInteger id, Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            GenericResponse<UserOrganizationInvite> response = new GenericResponse<UserOrganizationInvite>( 
                organizationService.fetchInvitedCustomers(id, authorizedUser) 
            );
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PostMapping("/{id}/customers/invite")
    public HttpEntity<?> inviteCustomer(@PathVariable("id") BigInteger id,
                                        @RequestBody  Map<String, String> args, 
                                        Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            GenericResponse<Map<String, String>> response = new GenericResponse<Map<String, String>>( 
                organizationService.inviteCustomer(id, args, authorizedUser) 
            );
            return ResponseEntity.ok(response);
        } catch (OrganizationNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("organization_not_found", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("illegal_arguments", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }


    /********************************************************************************************* **
     * USERS
     * ******************************************************************************************* */
    @Deprecated
    @GetMapping("/{id}/users")
    public HttpEntity<?> findUsersByOrganizationId(@PathVariable("id") BigInteger id, Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            return ResponseEntity.ok(organizationService.fetchCustomers(id, authorizedUser));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

}
