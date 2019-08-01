package br.com.ottimizza.application;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import br.com.ottimizza.application.configuration.H2TestProfileJPAConfig;
import br.com.ottimizza.application.repositories.users.UsersRepository;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = { SpringbootOauth2ServerApplication.class, H2TestProfileJPAConfig.class })
// @ActiveProfiles("test")
class SpringbootOauth2ServerApplicationTests {

	@MockBean
	UsersRepository UsersRepository;

	@Test
	void contextLoads() {
	}

}
