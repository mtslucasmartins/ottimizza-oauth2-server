package br.com.ottimizza.application.repositories.clients;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ottimizza.application.model.OAuthClientDetails;

public interface OAuthClientRepository extends JpaRepository<OAuthClientDetails, String> {
}
