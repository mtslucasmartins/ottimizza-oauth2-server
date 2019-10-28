package br.com.ottimizza.application.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class RedisHttpSessionConfig {

    @Value("${spring.redis.url}")
    private String redisUrl;

    @Bean
    @Primary
    public LettuceConnectionFactory lettuceConnectionFactory() throws Exception {

        URI uri = new URI(redisUrl);

        String host = uri.getHost();
        Integer port = uri.getPort();
        String password = uri.getUserInfo().split(":", 2)[1];

        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(host);
        redisConf.setPort(port);
        redisConf.setPassword(RedisPassword.of(password));

        return new LettuceConnectionFactory(redisConf);
        // return new LettuceConnectionFactory();
    }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

}