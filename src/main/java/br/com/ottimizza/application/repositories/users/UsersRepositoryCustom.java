package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsersRepositoryCustom {

    Page<User> fetchAll(UserDTO filter, Pageable pageable);

    Page<User> fetchAll(UserDTO filter, Pageable pageable, User authorizedUser);


}
