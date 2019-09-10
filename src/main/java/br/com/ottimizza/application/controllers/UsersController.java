package br.com.ottimizza.application.controllers;

import java.math.BigInteger;
import java.security.Principal;
import java.util.Map;

import javax.inject.Inject;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationNotFoundException;
import br.com.ottimizza.application.domain.exceptions.UserNotFoundException;
import br.com.ottimizza.application.domain.responses.ErrorResponse;
import br.com.ottimizza.application.domain.responses.GenericPageableResponse;
import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.UserOrganizationInvite;
import br.com.ottimizza.application.services.OrganizationService;
import br.com.ottimizza.application.services.UserService;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController //@formatter:on 
@RequestMapping(value = "/api/v1/users")
public class UsersController {

    @Inject
    UserService userService;

    @Inject
    OrganizationService organizationService;

    /**
     * Método GET para listar usuários com base no usuário logado e no filtro
     * especificado. Realiza paginação por padrão, página 1 com 10 itens por página.
     * 
     * @param filter    | Filtro - Classe com campos para filtro de usuário.
     * @param pageIndex | Paginação - Indíce da página atual.
     * @param pageSize  | Paginação - Quantidade de items por página.
     * @param principal | Segurança - Informações do usuário logado.
     * 
     * @return Objeto contendo informações de página e lista de usuarios.
     */
    @GetMapping
    public HttpEntity<?> fetchAll(@ModelAttribute UserDTO filter,
            @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize, Principal principal) {
        try {
            GenericPageableResponse<UserDTO> response = new GenericPageableResponse<UserDTO>(
                    userService.fetchAll(filter, pageIndex, pageSize, principal));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    /**
     * Método GET para buscar um usuário específico com base no usuário logado.
     * 
     * @param id        | ID do usuário
     * @param principal | Segurança - Informações do usuário logado.
     * @return
     */
    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable("id") BigInteger id, Principal principal) {
        try {
            GenericResponse<UserDTO> response = new GenericResponse<UserDTO>(
                    UserDTO.fromEntityWithOrganization(userService.findById(id)));
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("user_not_found", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PatchMapping("/{id}")
    public HttpEntity<?> patch(@PathVariable("id") BigInteger id, @RequestBody UserDTO userDTO, Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());

            UserDTO patched = userService.patch(id, userDTO, authorizedUser);

            return ResponseEntity.ok(new GenericResponse<UserDTO>(patched));

        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("user_not_found", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PostMapping
    public HttpEntity<?> create(@RequestBody UserDTO userDTO, Principal principal) {
        try {
            GenericResponse<UserDTO> response = new GenericResponse<UserDTO>(userService.create(userDTO, principal));
            return ResponseEntity.ok(response);
        } catch (UserNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("user_not_found", ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }

        // Encrypts the Password
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        // Persists the User.
        user = userRepository.save(user);

        return ResponseEntity.ok(user);
    }

    /**
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     * 
     */
    @GetMapping("/invited")
    public HttpEntity<?> fetchAllInvitedUsers(@ModelAttribute UserDTO filter,
            @RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
            @RequestParam(name = "page_size", defaultValue = "10") int pageSize, Principal principal) {
        try {
            GenericPageableResponse<UserOrganizationInvite> response = new GenericPageableResponse<UserOrganizationInvite>(
                    userService.fetchInvitedUsers(filter.getEmail(), pageIndex, pageSize, principal));
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @PostMapping("/invite")
    public HttpEntity<?> invite(@RequestBody Map<String, String> args, Principal principal) {
        try {
            User authorizedUser = userService.findByUsername(principal.getName());
            GenericResponse<Map<String, String>> response = new GenericResponse<Map<String, String>>(
                    userService.invite(args, authorizedUser));
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

}
