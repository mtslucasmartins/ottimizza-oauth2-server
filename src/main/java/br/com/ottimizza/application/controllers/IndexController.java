package br.com.ottimizza.application.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.repositories.users.UsersRepository;

@Controller
public class IndexController {

    @Inject
    UsersRepository userRepository;

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        // find user by username.
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));

        model.addAttribute("organization", new Organization());

        return "index.html";
    }

    /**
     * ******************************************************************************************
     * ** EMPRESAS
     * *******************************************************************************************
     */
    @GetMapping(value = { "/empresas", "/organizations" })
    public String organizations(Principal principal, Model model) {
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));
        return "organizations/organizations.html";
    }

    @GetMapping("/organizations/{externalId}")
    public String organization(@PathVariable("externalId") String externalId, Principal principal, Model model) {
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));
        return "organizations/organization.html";
    }

    /**
     * ********************************************************************************
     * ** USUARIOS
     * *********************************************************************************
     */
    @GetMapping(value = { "/usuarios", "/users" })
    public String users(Principal principal, Model model) {
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));
        return "users/users.html";
    }

    @GetMapping(value = { "/usuarios/{id}", "/users/{id}" })
    public String user(@PathVariable("id") String externalId, Principal principal, Model model) {
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));
        return "users/user.html";
    }

    /**
     * ********************************************************************************
     * ** USUARIOS
     * *********************************************************************************
     */
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        // find user by username.
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));

        return "profile/profile.html";
    }

    @GetMapping("/profile/security")
    public String profileSecurity(Principal principal, Model model) {
        // find user by username.
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));

        return "profile/security.html";
    }

}