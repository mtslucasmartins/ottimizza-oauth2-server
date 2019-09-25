package br.com.ottimizza.application.services;

// import org.junit.jupiter.api.Assertions;
// import org.junit.jupiter.api.BeforeAll;

// import static org.junit.jupiter.api.Assertions.fail;

// import java.math.BigInteger;
// import java.util.Calendar;
// import java.util.Date;

// import org.junit.Assert;
// import org.junit.jupiter.api.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringRunner;

// import br.com.ottimizza.application.SpringbootOauth2ServerApplication;
// import br.com.ottimizza.application.domain.exceptions.PasswordResetTokenInvalidException;
// import br.com.ottimizza.application.model.Organization;
// import br.com.ottimizza.application.model.PasswordResetToken;
// import br.com.ottimizza.application.model.user.User;

// @RunWith(SpringRunner.class)
// @SpringBootTest(classes = SpringbootOauth2ServerApplication.class)
class OrganizationServiceTest {

    // @Autowired
    // OrganizationService organizationService;

    // @Test
    // void testCheckIfOrganizationIsNotParentOfItself() {
    //     Organization accounting = new Organization();
    //     accounting.setId(BigInteger.ONE);

    //     Organization organization = new Organization();
    //     organization.setId(BigInteger.TEN);
    //     organization.setOrganization(accounting);

    //     try {
    //         // null
    //         Assertions.assertTrue(organizationService.checkIfOrganizationIsNotParentOfItself(null));

    //         // accounting (does not have a organization related to it)
    //         Assertions.assertTrue(organizationService.checkIfOrganizationIsNotParentOfItself(accounting));

    //         // company (has fk to accouting)
    //         Assertions.assertTrue(organizationService.checkIfOrganizationIsNotParentOfItself(organization));

    //     } catch (Exception ex) {
    //         ex.printStackTrace();
    //         fail("Exception was thrown, but no exception should be expected." + ex.getMessage());
    //     }
    // }
}
