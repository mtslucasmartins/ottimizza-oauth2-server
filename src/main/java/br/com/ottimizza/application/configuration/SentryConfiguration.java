
package br.com.ottimizza.application.configuration;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.HandlerExceptionResolver;

import io.sentry.spring.SentryExceptionResolver;
import io.sentry.spring.SentryServletContextInitializer;

@Configuration
public class SentryConfiguration {

    /*
     * Register a HandlerExceptionResolver that sends all exceptions to Sentry and
     * then defers all handling to the other HandlerExceptionResolvers. You should
     * only register this is you are not using a logging integration, otherwise you
     * may double report exceptions.
     */
    @Bean
    public HandlerExceptionResolver sentryExceptionResolver() {
        return new SentryExceptionResolver();
    }

    /*
     * Register a ServletContextInitializer that installs the
     * SentryServletRequestListener so that Sentry events contain HTTP request
     * information. This should only be necessary in Spring Boot applications.
     * "Classic" Spring should automatically load the
     * `io.sentry.servlet.SentryServletContainerInitializer`.
     */
    @Bean
    public ServletContextInitializer sentryServletContextInitializer() {
        return new SentryServletContextInitializer();
    }

}
