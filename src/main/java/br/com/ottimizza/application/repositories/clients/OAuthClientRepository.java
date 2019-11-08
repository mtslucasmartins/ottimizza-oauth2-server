package br.com.ottimizza.application.repositories.clients;

import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.ottimizza.application.model.OAuthClientDetails;

public interface OAuthClientRepository extends PagingAndSortingRepository<OAuthClientDetails, String> {
}
