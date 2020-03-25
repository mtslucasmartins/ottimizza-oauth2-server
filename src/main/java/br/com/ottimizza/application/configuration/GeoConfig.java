package br.com.ottimizza.application.configuration;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.maxmind.geoip2.DatabaseReader;

@Configuration
public class GeoConfig {

	@Bean
	public DatabaseReader geoDatabaseReader(@Value("classpath:GeoLite2-City.mmdb") InputStream geoInputStream) throws Exception {
		return new DatabaseReader.Builder(geoInputStream).build();
	}

}
