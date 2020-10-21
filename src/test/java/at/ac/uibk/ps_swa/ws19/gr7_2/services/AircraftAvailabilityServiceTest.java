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

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftAvailabilityService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;

/**
 * @author Philipp Schießl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AircraftAvailabilityServiceTest {
	
	@Autowired
	private AircraftService aircraftService;
	
	@Autowired
	private AircraftAvailabilityService aircraftAvailabilityService;
	
	@Autowired
	private AirportService airportService;
	
	List<Aircraft> aircraftList;
	
	Airport innsbruck;
	Airport munich;
	
	@Before
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void before() {
		aircraftList = new ArrayList<Aircraft>();
		aircraftList.addAll(aircraftService.getAllAircraft());
		
		innsbruck = airportService.loadAirport("INN");
		munich = airportService.loadAirport("MUC");
	}
	
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testSave() {
		aircraftAvailabilityService.saveAvailability(new AircraftAvailability(aircraftList.get(0),new Date(2019, 1, 1, 0, 0) , innsbruck));
		Assert.assertTrue("Availability was not saved successfully.", aircraftAvailabilityService.getAllAvailableAircraft(new Date(2019, 1, 1, 0, 0), innsbruck).contains(aircraftList.get(0)));
	}
	
	@Test
	@WithMockUser(username= "admin", authorities = {"ADMIN"})
	public void testIsNew() {
		AircraftAvailability availability = new AircraftAvailability(aircraftList.get(0),new Date(2019, 1, 1, 0, 0) , innsbruck);
		
		Assert.assertTrue("Unsaved Availability is not new.", availability.isNew());
		aircraftAvailabilityService.saveAvailability(availability);
		Assert.assertTrue("Saved Availability is new.", !aircraftAvailabilityService.getAllAvailableAircraft(new Date(2019, 1, 1, 0, 0), innsbruck).iterator().next().isNew());
	}
	
	@Test
	@WithMockUser(username= "admin", authorities = {"ADMIN"})
	public void testDelete() {
		AircraftAvailability availability = new AircraftAvailability(aircraftList.get(0),new Date(2019, 1, 1, 0, 0) , innsbruck);
		
		aircraftAvailabilityService.saveAvailability(availability);
		Assert.assertTrue("Availability was not saved successfully.", aircraftAvailabilityService.getAllAvailableAircraft(new Date(2019, 1, 1, 0, 0), innsbruck).contains(availability.getAircraft()));

		aircraftAvailabilityService.deleteAvailability(availability);
		Assert.assertTrue("Availability was not deleted successfully.", !aircraftAvailabilityService.getAllAvailabilities(new Date(2019, 1, 1, 0, 0), innsbruck).contains(availability));


	}
	
}
