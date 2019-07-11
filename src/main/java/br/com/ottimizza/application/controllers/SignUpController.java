package br.com.ottimizza.application.controllers;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.service.SecurityService;

@Controller
public class SignUpController {

    @Inject
    SecurityService securityService;

    @GetMapping("/register")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("organization", new Organization());

        return "signup.html";
    }

    @PostMapping("/register")
    public String signup(User user, Organization organization, Model model) {
        System.out.println(organization.getName());
        System.out.println(organization.getCnpj());

        System.out.println(user.getFirstName());
        System.out.println(user.getLastName());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());

        return "signup.html";
    }

}
