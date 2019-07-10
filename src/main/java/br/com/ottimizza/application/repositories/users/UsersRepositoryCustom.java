package br.com.ottimizza.application.repositories.users;

import java.util.List;

import br.com.ottimizza.application.model.User;

public interface UsersRepositoryCustom {

    List<User> findAll(Integer pageSize, Integer pageIndex);

}
