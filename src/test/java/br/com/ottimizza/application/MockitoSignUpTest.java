
package br.com.ottimizza.application;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.ottimizza.application.controllers.SignUpController;
import br.com.ottimizza.application.controllers.UserController;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.users.UsersRepository;
import br.com.ottimizza.application.service.SignUpService;

@SpringBootTest
public class MockitoSignUpTest {

    // @InjectMocks
    // private UserController signUpController;

    // @Mock
    // SignUpService signUpService;

    // @Mock
    // UsersRepository usersRepository;

    // @Before
    // public void init() {
    //     MockitoAnnotations.initMocks(this);
    // }

    // // @Test
    // // public void testGetUserById() {
    // // User u = new User();
    // // u.setId(1l);
    // // when(userRepository.findOne(1l)).thenReturn(u);

    // // User user = userController.get(1L);

    // // verify(userRepository).findOne(1l);

    // // assertEquals(1l, user.getId().longValue());
    // // }

    // @Test
    // public void test__GetUserById() throws Exception {
    //     User user = new User();
    //     user.setUsername("mts.lucasmartins@gmail.com");

    //     when(usersRepository.findById("mts.lucasmartins@gmail.com")).thenReturn(Optional.of(user));

    //     signUpController.findByUsername(user.getUsername(), null);

    //     verify(usersRepository).findById("mts.lucasmartins@gmail.com");

    //     // User registered = signUpService.register(user, organization);

    // }

    // @Test
    // public void signup() throws Exception {
    //     User user = new User();
    //     Organization organization = new Organization();

    //     //
    //     //
    //     organization.setName("Arthur e Vera Entregas Expressas LTDA");
    //     organization.setCnpj("62.585.137/0001-02");

    //     user.setFirstName("Lucas");
    //     user.setLastName("Martins");
    //     user.setEmail("lucas@ottimizza.com.br");
    //     user.setPassword("ottimizza@123");

    //     User registered = signUpService.register(user, organization);

    // }

}