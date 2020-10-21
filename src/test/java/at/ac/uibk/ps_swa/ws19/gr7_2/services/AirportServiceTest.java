package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * basic tests for {@link AirportService}.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AirportServiceTest {

    @Autowired
    AirportService airportService;

    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateAirport() {
        User adminUser = userService.loadUser("admin");
        Airport toBeUpdatedAirport = airportService.loadAirport("MUC");


        Assert.assertNotEquals("Airport \"MUC\" could not be loaded", null,toBeUpdatedAirport);
        Assert.assertEquals("Airport \"MUC\" should not be new", false, toBeUpdatedAirport.isNew());
        Assert.assertNull("Airport \"MUC\" has a updateUser defined", toBeUpdatedAirport.getUpdateUser());
        Assert.assertNull("Airport \"MUC\" has a updateDate defined", toBeUpdatedAirport.getUpdateDate());

        toBeUpdatedAirport.setEnabled(false);
        airportService.saveAirport(toBeUpdatedAirport);
        Assert.assertEquals("Airport \"MUC\" has wrong updateUser set", adminUser, toBeUpdatedAirport.getUpdateUser());
        Assert.assertEquals("Airport \"MUC\" should be disabled", false,toBeUpdatedAirport.isEnabled());

        toBeUpdatedAirport.setCountry("germany");
        Assert.assertEquals("Country of airport \"MUC\" should be germany", "germany", toBeUpdatedAirport.getCountry());
        toBeUpdatedAirport.setIataCode("IBK");
        toBeUpdatedAirport.setCountry("austria");
        Assert.assertEquals("Wrong IATA Code", "IBK", toBeUpdatedAirport.getIataCode());
        Assert.assertEquals("Country of airport \"IBK\" should be austria", "austria", toBeUpdatedAirport.getCountry());
    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadAirports() {
        for (Airport airport : airportService.getAllAirports()) {
            Assert.fail("Call to airportService.getAllAirports() should not work without proper authorization");
        }
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUserLoadsAirports() {
        for (Airport airport : airportService.getAllAirports()) {
            Assert.fail("Call to aircraftService.getAllAirports should not work without proper authorization");
        }
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user3", authorities = {"CREW"})
    public void testCrewLoadsAirports() {
        for (Airport airport : airportService.getAllAirports()) {
            Assert.fail("Call to aircraftService.getAllAirports should not work without proper authorization");
        }
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user2", authorities = {"PILOT"})
    public void testPilotLoadsAirports() {
        for (Airport airport : airportService.getAllAirports()) {
            Assert.fail("Call to aircraftService.getAllAirports should not work without proper authorization");
        }
    }



    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadAirport() {
        Airport airport = airportService.loadAirport("MUC");
        Assert.fail("Call to airportService.loadAirport should not work without proper authorization");
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUserLoadAirport() {
        Airport airport = airportService.loadAirport("MUC");
        Assert.fail("Call to airportService.loadAirport should not work without proper authorization");
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user3", authorities = {"CREW"})
    public void testCrewLoadAirport() {
        Airport airport = airportService.loadAirport("MUC");
        Assert.fail("Call to airportService.loadAirport should not work without proper authorization");
    }
    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user2", authorities = {"PILOT"})
    public void testPilotLoadAirport() {
        Airport airport = airportService.loadAirport("MUC");
        Assert.fail("Call to airportService.loadAirport should not work without proper authorization");
    }



    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testEquality() {
        Airport airport1 = airportService.loadAirport("MUC");
        Airport airport2 = airportService.loadAirport("MUC");
        Airport airport3 = airportService.loadAirport("IBK");

        Assert.assertEquals("Airports with same IATA code should be equal", airport1 , airport2);
        Assert.assertNotEquals("Airports with different IATA codes should not be equal", airport1 , airport3);
    }


}
