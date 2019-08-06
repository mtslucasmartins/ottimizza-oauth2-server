package br.com.ottimizza.application;

import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.service.SignUpService;

@SpringBootTest
class SignUpTests {

    // @Autowired
    // private TestEntityManager entityManager;

    // @Autowired
    // SignUpService signUpService;

    // @Test
    // public void whenSignUp_thenReturnUser() throws Exception {
    // System.out.println("Testing Sign Up.");
    // User user = new User();
    // Organization organization = new Organization();

    // //
    // //
    // organization.setName("Arthur e Vera Entregas Expressas LTDA");
    // organization.setCnpj("62.585.137/0001-02");

    // user.setFirstName("Lucas");
    // user.setLastName("Martins");
    // user.setEmail("lucas@ottimizza.com.br");
    // user.setPassword("ottimizza@123");

    // // when
    // User registered = signUpService.register(user, organization);

    // // then
    // Assert.assertEquals(registered.getFirstName(), user.getFirstName());
    // }

    // @Test
    // public void registerUser() throws Exception {
    // System.out.println("Testing Sign Up.");
    // User user = new User();
    // Organization organization = new Organization();

    // //
    // //
    // organization.setName("Arthur e Vera Entregas Expressas LTDA");
    // organization.setCnpj("62.585.137/0001-02");

    // user.setFirstName("Lucas");
    // user.setLastName("Martins");
    // user.setEmail("lucas@ottimizza.com.br");
    // user.setPassword("ottimizza@123");

    // user = signUpService.register(user, organization);

    // }

}
