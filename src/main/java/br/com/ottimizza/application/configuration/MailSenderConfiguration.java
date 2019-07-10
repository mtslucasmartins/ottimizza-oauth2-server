
package br.com.ottimizza.application.configuration;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

// @Configuration
public class MailSenderConfiguration {

    // @Value("{}")
    
    // @Bean
    // public JavaMailSender getJavaMailSender() {
    //     JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    //     mailSender.setHost("mail.ottimizza.com.br"); // br510.hostgator.com.br ? mail.ottimizza.com.br
    //     mailSender.setPort(587); // 26 ? --- 587 comum --- 465  SSL (doesn't connect)

    //     mailSender.setUsername("redefinicao@ottimizza.com.br");
    //     mailSender.setPassword("ottimizza");

    //     Properties props = mailSender.getJavaMailProperties();
    //     props.put("mail.transport.protocol", "smtp");
    //     props.put("mail.smtp.auth", "true");
    //     props.put("mail.smtp.starttls.enable", "true");
    //     props.put("mail.debug", "true");

    //     return mailSender;
    // }
}
