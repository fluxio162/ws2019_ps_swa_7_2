/**
 * 
 */
package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserAvailabilityService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;

/**
 * @author Philipp Schießl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class UserAvailabilityServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private UserAvailabilityService userAvailabilityService;
	
	@Autowired
	private AirportService airportService;
	
	List<User> pilotList;
	List<User> crewList;
	
	Airport innsbruck;
	Airport munich;

	
	@Before
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void before() {
		pilotList = new ArrayList<User>();
		pilotList.addAll(userService.getAllUsers(UserRole.PILOT));
		
		crewList = new ArrayList<User>();
		crewList.addAll(userService.getAllUsers(UserRole.CREW));
		
		innsbruck = airportService.loadAirport("INN");
		munich = airportService.loadAirport("MUC");
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testSave() {
		Assert.assertTrue("There are no pilots for testing.", !pilotList.isEmpty());
		
		for (User pilot : pilotList) {
			userAvailabilityService.saveAvailability(new UserAvailability(pilot, new Date(2020, 1, 1, 0, 0), innsbruck));
		}
		
		Assert.assertTrue("UserAvailabilities were not saved successfully.", pilotList.containsAll(userAvailabilityService.getAllAvailableUsers(new Date(2020, 1, 1, 0, 0), innsbruck, UserRole.PILOT)));
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testIsNew() {
		UserAvailability availability = new UserAvailability(pilotList.get(0), new Date(2020, 1, 1, 0, 0), innsbruck);
		Assert.assertTrue("Unsaved Availability is not new.", availability.isNew());
		
		userAvailabilityService.saveAvailability(availability);
		Assert.assertTrue("Saved Availability is new.", !availability.isNew());
	}
	
	@Test
	@WithMockUser(username= "admin", authorities = {"ADMIN"})
	public void testDelete() {
		UserAvailability availability = new UserAvailability(pilotList.get(0), new Date(2020, 1, 1, 0, 0), innsbruck);
		userAvailabilityService.saveAvailability(availability);
		
		Assert.assertTrue("Availability was not saved successfully.", userAvailabilityService.getAllAvailableUsers(availability.getTimestamp(), availability.getLocation(), UserRole.PILOT).contains(availability.getUser()));
		
		userAvailabilityService.deleteAvailability(availability);
		Assert.assertTrue("Availability was not deleted successfully.", !userAvailabilityService.getAllAvailableUsers(availability.getTimestamp(), availability.getLocation(), UserRole.PILOT).contains(availability.getUser()));
		
	}

}
