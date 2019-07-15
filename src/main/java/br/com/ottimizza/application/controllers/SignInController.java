package br.com.ottimizza.application.controllers;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.ottimizza.application.service.SecurityService;

@Controller
public class SignInController {

    @Inject
    SecurityService securityService;

    String DEFAULT_LOGO_URL = "https://ottimizza.com.br/wp-content/themes/ottimizza/images/logo-inverse.png";

    private SavedRequest getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null)
            return new HttpSessionRequestCache().getRequest(request, response);
        return null;
    }

    private void getCustomAttributes(Model model, HttpServletRequest request, HttpServletResponse response) {
        String logoURL = DEFAULT_LOGO_URL;
        SavedRequest savedRequest = getSavedRequest(request, response);
        if (savedRequest != null) {
            try {
                String logoParameterValue = savedRequest.getParameterMap().get("logo")[0];
                if (logoParameterValue != null && !logoParameterValue.equals(""))
                    logoURL = logoParameterValue;
            } catch (Exception ex) {
            }
        }
        model.addAttribute("logo", logoURL);
    }

    @GetMapping("/")
    public String indexPage() {
        return "index.html";
    }

    @GetMapping("/login")
    public String loginPage(Model model, HttpServletRequest request, HttpServletResponse response) {
        // sets custom attributes such as logo.
        getCustomAttributes(model, request, response);
        // returns the template.
        return "login.html";
    }

    @GetMapping("/maintenance")
    public String manteinencePage() {
        return "maintenance.html";
        // return "maintenance.html";
    }

}