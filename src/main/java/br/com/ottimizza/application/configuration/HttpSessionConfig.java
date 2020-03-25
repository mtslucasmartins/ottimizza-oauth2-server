package br.com.ottimizza.application.configuration;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.RedisSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {

    @Value("${spring.redis.url}")
    private String redisUrl;

    @Bean
    LettuceConnectionFactory redisConnectionFactory() throws Exception {

        URI uri = new URI(redisUrl);

        String host = uri.getHost();
        Integer port = uri.getPort();
        String password = uri.getUserInfo().split(":", 2)[1];

        System.out.println();
        System.out.println();
        System.out.println(host);
        System.out.println(port);

        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, port);
        configuration.setPassword(RedisPassword.of(password));

        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisOperations<String, Object> sessionRedisOperations() throws Exception {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // redisTemplate.setConnectionFactory(this.redisConnectionFactory);
        redisTemplate.setConnectionFactory(this.redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    // @Bean
    // public RedisSessionRepository sessionRepository(RedisOperations<String,
    // Object> sessionRedisOperations) {
    // return new RedisSessionRepository(sessionRedisOperations);
    // }

    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }

}
