package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftService;
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
 * basic tests for {@link AircraftService}.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AircraftServiceTest {

    @Autowired
    AircraftService aircraftService;

    @Autowired
    UserService userService;

    @DirtiesContext
    @Test
    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    public void testUpdateAircraft() {

        User adminUser = userService.loadUser("admin");
        Aircraft toBeUpdatedAircraft = aircraftService.loadAircraft(412412);

        Assert.assertNull("Aircraft \"412412\" has a updateUser defined", toBeUpdatedAircraft.getUpdateUser());
        Assert.assertNull("Aircraft \"412412\" has a updateDate defined", toBeUpdatedAircraft.getUpdateDate());

        toBeUpdatedAircraft.setSeats(100);
        toBeUpdatedAircraft.setRequiredPilots(5);
        toBeUpdatedAircraft.setRequiredCrew(5);

        aircraftService.saveAircraft(toBeUpdatedAircraft);

        Assert.assertEquals("Aircraft \"412412\" has wrong updateUser set", adminUser, toBeUpdatedAircraft.getUpdateUser());
        Assert.assertEquals("Aircraft \"412412\" does not have a the correct attribute stored being saved", 100, toBeUpdatedAircraft.getSeats());
        Assert.assertEquals("Aircraft \"412412\" does not have a the correct attribute stored being saved",5, toBeUpdatedAircraft.getRequiredPilots());
        Assert.assertEquals("Aircraft \"412412\" does not have a the correct attribute stored being saved",5, toBeUpdatedAircraft.getRequiredCrew());

    }

    @Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
    public void testUnauthenticatedLoadAircrafts() {
        for (Aircraft aircraft : aircraftService.getAllAircraft()) {
            Assert.fail("Call to aircraftService.getAllAircrafts() should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUnauthorizedLoadAircrafts() {
        for (Aircraft aircraft : aircraftService.getAllAircraft()) {
            Assert.fail("Call to aircraftService.getAllAircrafts should not work without proper authorization");
        }
    }

    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUnauthorizedLoadAircraft() {
        Aircraft aircraft = aircraftService.loadAircraft(412412);
        Assert.fail("Call to aircraftService.loadAircraft should not work without proper authorization");
    }


    @Test(expected = org.springframework.security.access.AccessDeniedException.class)
    @WithMockUser(username = "user4", authorities = {"USER"})
    public void testUnauthorizedSaveUser() {
        Aircraft aircraft = aircraftService.loadAircraft(412412);
        Assert.assertEquals("Call to aircraftService.loadAircraft returned wrong aircraft", 412412, aircraft.getAircraftId());
        aircraftService.saveAircraft(aircraft);
    }

}
