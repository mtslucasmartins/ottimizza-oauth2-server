
package br.com.ottimizza.application.controllers;

import java.security.Principal;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// Spring - Security
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

// Spring - Web
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Spring - Mail
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

// Models 
import br.com.ottimizza.application.model.PasswordResetToken;
import br.com.ottimizza.application.model.user.User;

// Repositories
import br.com.ottimizza.application.repositories.PasswordRecoveryRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;

// Services
import br.com.ottimizza.application.services.SecurityService;
import br.com.ottimizza.application.services.MailContentBuilder;

@Controller
public class PasswordRecoveryController {

    @Inject
    private UsersRepository userRepository;

    @Inject
    private MailContentBuilder mailContentBuilder;

    @Inject
    private PasswordRecoveryRepository passwordRecoveryRepository;

    @Inject
    private SecurityService securityService;

    @Autowired
    private JavaMailSender mailSender;

    @GetMapping(value = "/password_recovery")
    public String passwordRecoveryPage(Locale locale, Model model) {
        return "password_recovery.html";
    }

    @GetMapping(value = "/password_reset") //@formatter:off
    public String passwordResetPage(@RequestParam(name = "username", defaultValue = "") String username, 
                                    @RequestParam(name = "token", defaultValue = "") String token,
                                    HttpServletRequest request, HttpServletResponse response,
                                    Locale locale, Model model) throws Exception {
        if (username.equals("")) {
            model.addAttribute("error", "username.empty");
            return "password_reset.html";
        }
        if (token.equals("")) {
            model.addAttribute("error", "token.empty");
            return "password_reset.html";
        }
        
        String passwordResetTokenValidationResult = securityService.validatePasswordRecoveryToken(
                    username, token, request
        );
        System.out.println("Password Reset Token  >>  " + passwordResetTokenValidationResult);
        if (passwordResetTokenValidationResult != null) {
            // implementar tela com erros.
            // model.addAttribute("token", token);
        }
        model.addAttribute("token", token);
        return "password_reset.html";
    } //@formatter:on

    @PostMapping(value = "/user/password_recovery") //@formatter:off
    public void passwordRecovery(@RequestParam("email") String email, 
                                 HttpServletRequest request, 
                                 HttpServletResponse response) throws Exception {
        String passwordRecoveryToken = UUID.randomUUID().toString();
        User user = userRepository.findByEmail(email);

        if (user == null) {
            response.sendRedirect("/password_recovery?error");
            return;
        }

        PasswordResetToken passwordRecoveryTokenObject = new PasswordResetToken(passwordRecoveryToken, user);
        passwordRecoveryRepository.save(passwordRecoveryTokenObject);


        sendResetPasswordEmail(user, passwordRecoveryToken);
        // String passwordResetURL = new URIBuilder("https://accounts.ottimizza.com.br/password_reset")
        //                             .addParameter("username", user.getUsername())
        //                             .addParameter("token", passwordRecoveryToken)
        //                             .toString();
        // // redefinicao@ottimizza.com.br
        // sendSimpleMessage(user.getEmail().trim(), "Ottimizza - Redefinição de Senha", passwordResetURL);
        // // sendSimpleMessage("diogo@ottimizza.com.br", "Ottimizza - Redefinição de Senha", passwordResetURL);

        response.sendRedirect("/password_recovery?success");
    }

    @PostMapping(value = "/user/password_reset") //@formatter:off
    public void passwordRecoveryPage(@RequestParam("token") String token,
                                     @RequestParam("new-password") String newPassoword,
                                     @RequestParam("new-password-check") String newPassowordCheck, 
                                     Principal principal, Locale locale, Model model, 
                                     HttpServletRequest request, HttpServletResponse response,
                                     SecurityContextHolderAwareRequestWrapper securityContext) 
                                     throws Exception {

        if (!newPassoword.equals(newPassowordCheck)) {
            // response.sendRedirect("/password_reset?error=password_mismatch");
        }

        PasswordResetToken passwordResetToken = passwordRecoveryRepository.findByToken(token);

        // atualiza a senha do usuario
        userRepository.updatePassword(
            new BCryptPasswordEncoder().encode(newPassoword), 
            passwordResetToken.getUser().getUsername()
        );

        // if (result != null) {
        //     // model.addAttribute("message", messages.getMessage("auth.message." + result, null, locale));
        //     return "redirect:/login?lang=" + locale.getLanguage();
        // }
        // return "result"; // + locale.getLanguage(); .html?lang=pt-br
        response.sendRedirect("/password_reset?success");
    }

    public void sendSimpleMessage(String to, String subject, String action) {
        // SimpleMailMessage message = new SimpleMailMessage(); 
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("redefinicao@ottimizza.com.br");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            // messageHelper.setText(message);
            // ... 
            String content = mailContentBuilder.build("Olá, " + to + "!", action);
            messageHelper.setText(content, true);
        };
        
        mailSender.send(messagePreparator);
    }

    public void sendResetPasswordEmail(User user, String resetPasswordToken) throws Exception {
        String subject = "Ottimizza - Redefinição de Senha";
        String username = user.getUsername();
        String fullname = MessageFormat.format("{0} {1}", user.getFirstName(), user.getLastName());
        
        String greeting = MessageFormat.format("Olá {0}!", fullname);
        String passwordResetURL = new URIBuilder("https://accounts.ottimizza.com.br/password_reset")
                                    .addParameter("username", username)
                                    .addParameter("token", resetPasswordToken)
                                    .toString();
        
        String content = mailContentBuilder.build(greeting, passwordResetURL);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
            helper.setFrom("redefinicao@ottimizza.com.br");
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(content, true);
        };
        
        mailSender.send(messagePreparator);
    }

}