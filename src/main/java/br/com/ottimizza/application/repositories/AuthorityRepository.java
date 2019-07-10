package br.com.ottimizza.application.repositories;

import br.com.ottimizza.application.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, String> {

    Authority findByName(String name);

}
