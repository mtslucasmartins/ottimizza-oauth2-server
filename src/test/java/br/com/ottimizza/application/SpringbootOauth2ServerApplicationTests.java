package br.com.ottimizza.application;

import javax.inject.Inject;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.repositories.organizations.OrganizationRepository;
import br.com.ottimizza.application.repositories.users.UsersRepository;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootOauth2ServerApplication.class)
class SpringbootOauth2ServerApplicationTests {

	@Inject
	OrganizationRepository organizationRepository;

	@Inject
	UsersRepository usersRepository;

	@Test
	void contextLoads() {
		// Cria uma contabilidade para testes.
		Organization accounting = createAccounting();
		System.out.println("ACCOUNTING: " + accounting.getId());

		// Cria uma empresa para testes.
		Organization organization = createOrganization(accounting);
		System.out.println("ORGANIZATION: " + organization.getId());

		// Cria um contador para testes.
		User accoutant = createAccountant(accounting);
		System.out.println("ACCOUTANT: " + accoutant.getId());

		// Cria uma empresa para testes.
		User customer = createCustomer(organization);
		System.out.println("CUSTOMER: " + customer.getId());

	}

	Organization createAccounting() { // @formatter:off
		return organizationRepository.save(Organization
			.builder()
				.name("Accounting Firm Co")
				.cnpj("82910893000177")
				.type(Organization.Type.ACCOUNTING)
			.build()
		);
	}

	Organization createOrganization(Organization accounting) { // @formatter:off
		return organizationRepository.save(Organization
			.builder()
				.name("Customer Company Co")
				.cnpj("99097492000142")
				.type(Organization.Type.CLIENT)
				.organization(accounting)
			.build()
		);
	}

	User createAccountant(Organization accounting) {
		return usersRepository.save(User
			.builder()
				.username("accountant@ottimizza.com.br")
				.email("accountant@ottimizza.com.br")
				.firstName("Accoutant")
				.lastName("Ottimizza")
				.type(User.Type.ACCOUNTANT)
				.organization(accounting)
			.build()
		);
	}

	User createCustomer(Organization organization) {
		User customer = usersRepository.save(User
			.builder()
				.username("customer@ottimizza.com.br")
				.email("customer@ottimizza.com.br")
				.firstName("Customer")
				.lastName("Ottimizza")
				.type(User.Type.CUSTOMER)
				.organization(organization.getOrganization())
			.build()
		);
		usersRepository.addOrganization(customer.getId(), organization.getId());
		return customer;
	}

}
