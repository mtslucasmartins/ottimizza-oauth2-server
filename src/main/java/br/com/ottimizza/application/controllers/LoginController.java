package br.com.ottimizza.application.controllers;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.ottimizza.application.service.SecurityService;

@Controller
public class LoginController {

    @Inject
    SecurityService securityService;

    @GetMapping("/")
    public String indexPage() {
        return "index.html";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login.html";
    }

    @GetMapping("/maintenance")
    public String manteinencePage() {
        return "maintenance.html";
        // return "maintenance.html";
    }

}