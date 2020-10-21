package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.InsufficientStaffException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.NoSuitableAircraftFoundException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionSuperCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import at.ac.uibk.ps_swa.ws19.gr7_2.utils.Time;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * basic tests for {@link FlightService}.
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class FlightServiceTest {

	@Autowired
	FlightService flightService;

	@Autowired
	AircraftService aircraftService;

	@Autowired
	AirportService airportService;

	@Autowired
	UserService userService;

	@Autowired
	AircraftAvailabilityService aircraftAvailabilityService;

	@Autowired
	UserAvailabilityService userAvailabilityService;

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testCreateFlight() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = null;
		try {
			flight = flightService.createFlight(50, innsbruck, munich, new Date(2020, 5, 5, 20, 0), new Date(2020, 5, 5, 21, 0));
		} catch (NoSuitableAircraftFoundException|InsufficientStaffException e) {}
		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getAllFlights().contains(flight), "Flight was not saved successfully.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testUpdateFlight() {

		User adminUser = userService.loadUser("admin");
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight toBeUpdatedFlight = null;
		try {
			toBeUpdatedFlight = flightService.createFlight(50, innsbruck, munich, new Date(2020, 5, 5, 20, 0), new Date(2020, 5, 5, 21, 0));
		} catch (NoSuitableAircraftFoundException|InsufficientStaffException e) {}

		Assertions.assertNotNull(toBeUpdatedFlight, "Flight creation failed.");

		Aircraft testAircraft = aircraftService.loadAircraft(784623);

		Airport testOriginAirport = airportService.loadAirport("MUC");

		Airport testDestinationAirport = airportService.loadAirport("INN");


		toBeUpdatedFlight.setAircraft(testAircraft);
		toBeUpdatedFlight.setOriginAirport(testOriginAirport);
		toBeUpdatedFlight.setOriginAirport(testOriginAirport);
		toBeUpdatedFlight.setDestinationAirport(testDestinationAirport);
		toBeUpdatedFlight.setPassengerCount(111);

		flightService.saveFlight(toBeUpdatedFlight);


		Assert.assertEquals("Flight \"1\" has wrong updateUser set", adminUser, toBeUpdatedFlight.getUpdateUser());
		Assert.assertEquals("Flight \"1\" does not have a the correct attribute stored being saved", testAircraft, toBeUpdatedFlight.getAircraft());
		Assert.assertEquals("Flight \"1\" does not have a the correct attribute stored being saved",testOriginAirport, toBeUpdatedFlight.getOriginAirport());
		Assert.assertEquals("Flight \"1\" does not have a the correct attribute stored being saved",testDestinationAirport, toBeUpdatedFlight.getDestinationAirport());
		Assert.assertEquals("Flight \"1\" does not have a the correct attribute stored being saved",111, toBeUpdatedFlight.getPassengerCount());

	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testDeleteFlight() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = null;
		try {
			flight = flightService.createFlight(50, innsbruck, munich, new Date(2020, 5, 5, 20, 0), new Date(2020, 5, 5, 21, 0));
		} catch (NoSuitableAircraftFoundException|InsufficientStaffException e) {}
		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getAllFlights().contains(flight), "Flight was not saved successfully.");

		flightService.cancelFlight(flight);

		Assertions.assertTrue(!flightService.getAllFlights().contains(flight), "The flight list does still contain the deleted flight.");
		Assertions.assertTrue(flightService.getAllCanceledFlights().contains(flight), "The canceled flight list does not contain the deleted flight.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindCompleteFlightList() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = new Flight();

		flight.setDepartureTime(new Date(1));
		flight.setArrivalTime(new Date(2));
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getCompletedFlights().contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindByFlightId() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = new Flight();

		flight.setDepartureTime(new Date());
		flight.setArrivalTime(new Date());
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertEquals(flight, flightService.loadFlight(flight.getFlightId()), "Flights are not the same!");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindCurrentFlights() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = new Flight();

		flight.setDepartureTime(new Date());
		Date arrival = new Date();
		arrival.setYear(9999);
		flight.setArrivalTime(arrival);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getCurrentFlights().contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindPlannedFlights() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = new Flight();

		Date date = new Date();
		date.setYear(9999);
		flight.setDepartureTime(date);
		flight.setArrivalTime(date);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getPlannedFlights().contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindIncompleteFlights() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = new Flight();

		flight.setDepartureTime(new Date());
		flight.setArrivalTime(new Date());
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setComplete(false);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getIncompleteFlights().contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindAllFutureByAirportFlights() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Flight flight = new Flight();

		Date date = new Date();
		date.setYear(9999);
		flight.setDepartureTime(date);
		flight.setArrivalTime(date);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.findAllByAirport(munich).contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindAllFromUser() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		User user = userService.loadUser("user2");
		Set<User> pilotSet = new HashSet<>();
		pilotSet.add(user);
		Flight flight = new Flight();

		flight.setDepartureTime(new Date());
		flight.setArrivalTime(new Date());
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setPilots(pilotSet);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.findAllFromUser(user).contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindAllPlannedFromUser() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		User user = userService.loadUser("user2");
		Set<User> pilotSet = new HashSet<>();
		pilotSet.add(user);
		Flight flight = new Flight();

		Date date = new Date();
		date.setYear(9999);
		flight.setDepartureTime(date);
		flight.setArrivalTime(date);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setPilots(pilotSet);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.findAllPlannedFromUser(user).contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindAllPlannedFromPilot() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		User user = userService.loadUser("user2");
		Set<User> pilotSet = new HashSet<>();
		pilotSet.add(user);
		Flight flight = new Flight();

		Date date = new Date();
		date.setYear(9999);
		flight.setDepartureTime(date);
		flight.setArrivalTime(date);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setPilots(pilotSet);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getAllFromPilot(user).contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindAllPlannedFromCrew() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		User user = userService.loadUser("user3");
		Set<User> crewSet = new HashSet<>();
		crewSet.add(user);
		Flight flight = new Flight();

		Date date = new Date();
		date.setYear(9999);
		flight.setDepartureTime(date);
		flight.setArrivalTime(date);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setCrew(crewSet);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.getAllFromCrew(user).contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testFindAllFromAircraft() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Aircraft aircraft = aircraftService.loadAircraft(412412);

		Assertions.assertNotNull(aircraft, "Aircraft not found.");

		Flight flight = new Flight();

		Date date = new Date();
		date.setYear(9999);
		flight.setDepartureTime(date);
		flight.setArrivalTime(date);
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setAircraft(aircraft);
		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");
		Assertions.assertTrue(flightService.findAllPlannedFromAircraft(aircraft).contains(flight), "Flight not found.");
	}

	@DirtiesContext
	@Test
	@WithMockUser(username = "admin", authorities = {"ADMIN"})
	public void testDoCancelFlight() {
		Airport innsbruck = airportService.loadAirport("INN");
		Airport munich = airportService.loadAirport("MUC");
		Aircraft aircraft = aircraftService.loadAircraft(412412);
		User user = userService.loadUser("user2");
		Set<User> pilotSet = new HashSet<>();
		Set<User> crewSet = new HashSet<>();
		Set<UserAvailability> userAvailabilities = new HashSet<>();
		Flight flight = new Flight();

		flight.setDepartureTime(new Date());
		flight.setArrivalTime(new Date());
		flight.setOriginAirport(innsbruck);
		flight.setDestinationAirport(munich);
		flight.setPilots(pilotSet);
		flight.setCrew(crewSet);
		AircraftAvailability aircraftAvailability = new AircraftAvailability(aircraft, Time.addHours(flight.getArrivalTime(), 2), flight.getDestinationAirport());
		aircraftAvailabilityService.saveAvailability(aircraftAvailability);
		flight.setDependentAircraftAvailability(aircraftAvailability);

		UserAvailability userAvailability = new UserAvailability(user, Time.addHours(flight.getArrivalTime(), 12), flight.getDestinationAirport());
		userAvailabilityService.saveAvailability(userAvailability);
		userAvailabilities.add(userAvailability);
		flight.setDependentUserAvailabilities(userAvailabilities);

		flightService.saveFlight(flight);

		Assertions.assertNotNull(flight, "Flight creation failed.");

		try{
			flightService.doCancelFlight(flight);
		} catch (EMailExceptionSuperCollector e){
			System.out.println("Exception: EMailExceptionSuperCollector");
		}

		Assertions.assertTrue(flightService.getAllCanceledFlights().contains(flight), "Flight not found.");
	}


	@Test(expected = org.springframework.security.authentication.AuthenticationCredentialsNotFoundException.class)
	public void testUnauthenticatedLoadFlights() {
		for (Flight flight : flightService.getAllFlights()) {
			Assert.fail("Call to flightService.getAllFlights() should not work without proper authorization");
		}
	}

	@Test(expected = org.springframework.security.access.AccessDeniedException.class)
	@WithMockUser(username = "user4", authorities = {"USER"})
	public void testUnauthorizedLoadFlights() {
		for (Flight flight : flightService.getAllFlights()) {
			Assert.fail("Call to aircraftService.getAllFlights should not work without proper authorization");
		}
	}

	@Test(expected = org.springframework.security.access.AccessDeniedException.class)
	@WithMockUser(username = "user4", authorities = {"USER"})
	public void testUnauthorizedLoadFlight() {
		Flight flight = flightService.loadFlight(1);
		Assert.fail("Call to flightService.loadFlight should not work without proper authorization");
	}


	@Test(expected = org.springframework.security.access.AccessDeniedException.class)
	@WithMockUser(username = "user4", authorities = {"USER"})
	public void testUnauthorizedSaveUser() {
		Flight flight = flightService.loadFlight(1);
		Assert.assertEquals("Call to flightService.loadFlight returned wrong flight", 1, flight.getFlightId());
		flightService.saveFlight(flight);
	}

}
