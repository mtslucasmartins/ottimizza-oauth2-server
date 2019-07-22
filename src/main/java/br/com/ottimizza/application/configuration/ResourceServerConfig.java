package br.com.ottimizza.application.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        
        http
            .antMatcher("/oauth/**")
            .authorizeRequests()
                .antMatchers("/oauth/revoke_token*").authenticated();
                // .antMatchers("/user/password_reset*", "/user/password_recovery*").permitAll()

        http
            .antMatcher("/user/**")
            .authorizeRequests()
                .antMatchers("/user/info", "/user/revoke_token").authenticated()
                .antMatchers("/user/password_reset*", "/user/password_recovery*").permitAll()
                .anyRequest().authenticated();
		// @formatter:on
    }
}
