package br.com.ottimizza.application.controllers;

import java.io.IOException;
import java.security.Principal;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import br.com.ottimizza.application.services.SecurityService;

@Controller
public class SignInController {

    @Inject
    SecurityService securityService;

    // String DEFAULT_LOGO_URL = "https://ottimizza.com.br/wp-content/themes/ottimizza/images/logo-inverse.png";
    String DEFAULT_LOGO_URL = "/assets/img/logos/tareffa-white.png";


    private String getSignInLogo() {
        return System.getenv("SIGNIN_LOGO");
    }
    private String getSignInBackground() {
        return System.getenv("SIGNIN_BACKGROUND");
    }
    private String getSignInTitle() {
        return System.getenv("SIGNIN_TITLE");
    }

    private SavedRequest getSavedRequest(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null)
            return new HttpSessionRequestCache().getRequest(request, response);
        return null;
    }

    private void getCustomAttributes(Model model, HttpServletRequest request, HttpServletResponse response) {
        String logoURL = getSignInLogo();
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
        model.addAttribute("background", getSignInBackground());
        model.addAttribute("title", getSignInTitle());
    }

    @GetMapping("/login")
    public String loginPage(Model model, Principal principal, HttpServletRequest request, HttpServletResponse response) throws IOException {
        getCustomAttributes(model, request, response);

        String client_id = request.getParameter("client_id"); 
        String response_type = request.getParameter("response_type"); 
        String redirect_uri = request.getParameter("redirect_uri"); 

        System.out.println("client_id: " + client_id);
        System.out.println("response_type: " + response_type);
        System.out.println("redirect_uri: " + redirect_uri);

        // SavedRequest savedRequest = getSavedRequest(request, response);

        // if (savedRequest != null) {
        //     try {
        //         String logoParameterValue = savedRequest.getParameterMap().get("logo")[0];
        //         if (logoParameterValue != null && !logoParameterValue.equals(""))
        //             logoURL = logoParameterValue;
        //     } catch (Exception ex) {
        //     }
        // }
        
        // if (principal != null) { 
        //     //depends on your security config, maybe you want to check the security context instead if you allow anonym access
        //     String redirect_uri = request.getParameter("redirect_uri"); 
        //     //here you must get all the other attributes thats needed for the authorize url
        //     if (redirect_uri == null) {
        //         redirect_uri = "https://accounts.ottimizza.com.br";
        //     }           
        //     return "redirect:/oauth/authorize?response_type=token&state=6c2bb162-0f26-4caa-abbe-b65f7e5c6a2e&client_id=admin&redirect_uri=" + URLEncoder.encode(redirect_uri, "UTF-8");
        // }
        // return "login";
        return "login.html";
    }

    @GetMapping("/maintenance")
    public String manteinencePage() {
        return "maintenance.html";
    }

}