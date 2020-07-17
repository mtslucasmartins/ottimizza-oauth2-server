package br.com.ottimizza.application.services;

import org.mockito.Mock;
import org.mockito.Mockito;

import java.security.Principal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.ottimizza.application.SpringbootOauth2ServerApplication;
import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.domain.exceptions.OrganizationAlreadyRegisteredException;
import br.com.ottimizza.application.model.Organization;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootOauth2ServerApplication.class) // @formatter:off
class OrganizationServiceTest {

    public static final String ADMINISTRATOR = "administrator@ottimizza.com.br";
    public static final String ACCOUNTANT = "accountant@ottimizza.com.br";
    public static final String CUSTOMER = "customer@ottimizza.com.br";

    @Mock
    private Principal principal;

    @Autowired
    OrganizationService organizationService;

    @Test
    public void givenOrganizationDTO_whenSaveAccountingAndRetreive_thenOK() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Accounting Firm Co")
            .cnpj("00000000000101")
            .type(Organization.Type.ACCOUNTING).build();
        OrganizationDTO created = organizationService.create(organizationDTO,false, principal);
        Assertions.assertNotNull(created);
        Assertions.assertNotNull(created.getId());
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingAgain_thenThrowOrganizationAlreadyRegisteredException() throws Exception { 
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Accounting Firm Co")
            .cnpj("00000000000101")
            .type(Organization.Type.ACCOUNTING).build();
        Assertions.assertThrows(OrganizationAlreadyRegisteredException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingWithNullType_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Accounting Firm Co")
            .cnpj("00000000000101").build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
        	organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingWithInvalidType_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Accounting Firm Co")
            .cnpj("00000000000101")
            .type(99).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingWithNullName_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .cnpj("00000000000101")
            .type(Organization.Type.ACCOUNTING).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingWithEmptyName_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("")
            .cnpj("00000000000101")
            .type(Organization.Type.ACCOUNTING).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingWithNullCNPJ_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Example Company Ltd")
            .type(Organization.Type.ACCOUNTING).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveAccountingWithEmptyCNPJ_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);
        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Example Company Ltd")
            .cnpj("")
            .type(Organization.Type.ACCOUNTING).build();
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

    @Test
    public void givenOrganizationDTO_whenSaveCompanyWithoutAccounting_thenIllegalArgumentException() throws Exception {
        Mockito.when(principal.getName()).thenReturn(ADMINISTRATOR);

        OrganizationDTO organizationDTO = OrganizationDTO.builder()
            .name("Example Company Ltd")
            .cnpj("00000000000102")
            .type(Organization.Type.CLIENT).build();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            organizationService.create(organizationDTO, false, principal);
        });
	}

}
