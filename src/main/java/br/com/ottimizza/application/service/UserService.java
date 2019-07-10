package br.com.ottimizza.application.service;

import br.com.ottimizza.application.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    List<User> findAll();

    void delete(long id);
}
