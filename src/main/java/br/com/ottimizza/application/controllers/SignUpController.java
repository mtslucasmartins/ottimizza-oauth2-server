package br.com.ottimizza.application.controllers;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.domain.exceptions.UserAlreadyRegisteredException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.service.SignUpService;

@Controller
public class SignUpController {

    @Inject
    SignUpService signUpService;

    @GetMapping("/register")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("organization", new Organization());

        return "signup.html";
    }

    @PostMapping("/register")
    public String signup(User user, Organization organization, Model model) {
        try {
            // registering user
            user = signUpService.register(user, organization);

            // if no exceptions was thrown adds a success attribute.
            model.addAttribute("success", "true");

        } catch (UserAlreadyRegisteredException ex) {
            model.addAttribute("error_message", "Um usuário com este endereço de email já está cadastrado!");
        } catch (OrganizationAlreadyRegisteredException ex) {
            model.addAttribute("error_message", "Uma empresa com este CPF/CNPJ já está cadastrada!");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return "signup.html";
    }

}
