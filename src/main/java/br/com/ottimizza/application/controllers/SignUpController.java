package br.com.ottimizza.application.controllers;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.ottimizza.application.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.exceptions.UserAlreadyRegisteredException;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;
import br.com.ottimizza.application.service.SignUpService;

@Controller
public class SignUpController {

    @Inject
    SignUpService signUpService;

    @Inject
    UsersRepository userRepository;

    @Inject
    OrganizationRepository organizationRepository;

    @GetMapping("/register")
    public String signupPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("organization", new Organization());

        return "signup.html";
    }

    @PostMapping("/register")
    public String signup(User user, Organization organization, Model model) {
        try {
            organization.setExternalId(organization.getCnpj());
            user.setUsername(user.getEmail());

            // registering user
            user = signUpService.register(user, organization);

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
