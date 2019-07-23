package br.com.ottimizza.application.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.ottimizza.application.repositories.users.UsersRepository;

@Controller
public class IndexController {

    @Inject
    UsersRepository userRepository;

    @GetMapping("/")
    public String index(Principal principal, Model model) {
        // find user by username.
        model.addAttribute("authorizedUser", userRepository.findByEmail(principal.getName()));

        return "index.html";
    }

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