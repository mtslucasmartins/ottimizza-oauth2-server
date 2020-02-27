package br.com.ottimizza.application.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import io.sentry.spring.SentryExceptionResolver;

@Configuration
public class SentryConfiguration {

    @Bean
    public HandlerExceptionResolver sentryExceptionResolver() {
        return new SentryExceptionResolver() {
            @Override
            public ModelAndView resolveException(HttpServletRequest request,
                    HttpServletResponse response,
                    Object handler,
                    Exception ex) {
                Throwable rootCause = ex;

                while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
                    rootCause = rootCause.getCause();
                }

                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println(rootCause.getMessage());
                System.out.println();
                System.out.println();
                System.out.println();

                if (!rootCause.getMessage().contains("HTTP 401")) {
                    super.resolveException(request, response, handler, ex);
                }
                return null;
            }   

        };
    }

    // @Bean
    // public HandlerExceptionResolver sentryExceptionResolver() {
    //     return new io.sentry.spring.SentryExceptionResolver();
    // }

    @Bean
    public ServletContextInitializer sentryServletContextInitializer() {
        return new io.sentry.spring.SentryServletContextInitializer();
    }

}