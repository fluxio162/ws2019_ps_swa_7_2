package at.ac.uibk.ps_swa.ws19.gr7_2.tests;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Vacation;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.VacationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class VacationServiceTest {

    @Autowired
    VacationService vacationService;

    @Autowired
    UserService userService;



    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadVacations() {
        for (Vacation vacation : vacationService.getAllVacation()) {
            Assert.fail("Call to vacationService.getAllVacations() should not work without proper authorization");
        }
    }


    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAdminSaveVacation() {
        Vacation vacation = vacationService.loadVacation(1234);
        Assert.assertEquals("Call to vacationService.loadVacation() returned wrong vacation", 1234, vacation.getVacationId());
        vacationService.saveVacation(vacation);
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"MANAGER"})
    public void testManagerSaveVacation() {
        Vacation vacation = vacationService.loadVacation(1234);
        Assert.assertEquals("Call to vacationService.loadVacation() returned wrong vacation", 1234, vacation.getVacationId());
        vacationService.saveVacation(vacation);
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testAdminLoadVacation() {
        Vacation vacation = vacationService.loadVacation(1234);
        Assert.fail("Call to vacationService.getAllVacations() should not work without proper authorization");
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user1", authorities = {"MANAGER"})
    public void testManagerLoadVacation() {
        Vacation vacation = vacationService.loadVacation(1234);
        Assert.fail("Call to vacationService.getAllVacations() should not work without proper authorization");
    }

    }

