package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.InsufficientStaffException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.NoSuitableAircraftFoundException;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftAvailabilityService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserAvailabilityService;
import at.ac.uibk.ps_swa.ws19.gr7_2.utils.Time;

/**
 * The type Flight factory.
 *
 * @author Philipp Schießl
 */
@Component
public class FlightFactory {
	private long flightId = -1;
	private static final long  MIN_AIRCRAFT_STAY = 2;
	private static final long  MIN_CREW_STAY = 12;
	private static final long CREATE_NEW = -1;

	@Autowired
	private AircraftAvailabilityService aircraftAvailabilityService;

	@Autowired
	private UserAvailabilityService userAvailabilityService;

	@Autowired
	private FlightService flightService;
	
	/**
	 * Evaluates whether the user can participate in the flight without working more than 40 hours per week.
	 * 
	 * @param user
	 * @param flight
	 * @return
	 */
	private boolean userCanWorkFlight (User user, Flight flight) {
		Date startOfWeek = Time.startOfWeek(flight.getDepartureTime());
		long workload = flightService.getUserWorkloadInWeekOfDate(user, startOfWeek);

		if ( Time.millisecondsToHours(flight.getArrivalTime().getTime()-flight.getDepartureTime().getTime()) > 40 ) {
			return false;
		}
		if (!startOfWeek.equals(Time.startOfWeek(flight.getArrivalTime()))) {
			workload += Time.endOfWeek(flight.getDepartureTime()).getTime() - flight.getDepartureTime().getTime();
			if (Time.millisecondsToHours(workload) > 40) {
				return false;
			}

			startOfWeek = Time.startOfWeek(flight.getArrivalTime());
			workload = flightService.getUserWorkloadInWeekOfDate(user, startOfWeek) + flight.getArrivalTime().getTime() - startOfWeek.getTime();

		} else {
			workload += flight.getArrivalTime().getTime() - flight.getDepartureTime().getTime();
		}
		if (Time.millisecondsToHours(workload) > 40) {
			return false;
		}

		return true;
	}

	/**
	 * Create flight.
	 *
	 * @param requiredSeats the required seats
	 * @param origin        the origin
	 * @param destination   the destination
	 * @param departure     the departure
	 * @param arrival       the arrival
	 * @return the flight
	 * @throws NoSuitableAircraftFoundException the no suitable aircraft found exception
	 * @throws InsufficientStaffException       the insufficient staff exception
	 */
	public Flight createFlight (int requiredSeats, Airport origin, Airport destination, Date departure, Date arrival) throws NoSuitableAircraftFoundException, InsufficientStaffException {
		return createFlight(requiredSeats, origin, destination, departure, arrival, CREATE_NEW);
	}

	/**
	 * Try recreate flight with the same flightId as the given flight.
	 *
	 * @param flight the flight
	 * @throws NoSuitableAircraftFoundException the no suitable aircraft found exception
	 * @throws InsufficientStaffException       the insufficient staff exception
	 */
	public void tryRecreateFlight (Flight flight) throws NoSuitableAircraftFoundException, InsufficientStaffException {
		createFlight(flight.getAircraft().getSeats(), flight.getOriginAirport(), flight.getDestinationAirport(), flight.getDepartureTime(), flight.getArrivalTime(), flight.getFlightId());
	}

	private Flight createFlight(int requiredSeats, Airport origin, Airport destination, Date departure, Date arrival, long flightId) throws NoSuitableAircraftFoundException, InsufficientStaffException {
		AircraftAvailability usedAircraftAvailability;
		Aircraft aircraft;
		Set<UserAvailability> crewAvailabilitySet = new HashSet<UserAvailability>();
		Flight flight = new Flight();
		initiateFlightId();

		flight.setDepartureTime(departure);
		flight.setArrivalTime(arrival);
		flight.setOriginAirport(origin);
		flight.setDestinationAirport(destination);
		flight.setPilots(new HashSet<User>());
		flight.setCrew(new HashSet<User>());
		flight.setDependentUserAvailabilities(new HashSet<UserAvailability>());
		flight.setComplete(true);

		// Find Aircraft.
		usedAircraftAvailability = findAircraftAvailability(flight, requiredSeats);
		aircraft = usedAircraftAvailability.getAircraft();

		// Find Pilots.
		findCrew(crewAvailabilitySet, flight, UserRole.PILOT, aircraft.getRequiredPilots());

		// Find Crew.
		findCrew(crewAvailabilitySet, flight, UserRole.CREW, aircraft.getRequiredCrew());

		// Set flightId.
		if ( flightId == CREATE_NEW ) {
			flight.setFlightId(this.flightId++);
		} else {
			flight.setFlightId(flightId);
		}

		// Create new AircraftAvailability.
		AircraftAvailability newAircraftAvailability = new AircraftAvailability(aircraft, Time.addHours(arrival, MIN_AIRCRAFT_STAY), destination);
		flight.setDependentAircraftAvailability(newAircraftAvailability);
		aircraftAvailabilityService.saveAvailability(newAircraftAvailability);

		// save flight.
		flightService.saveFlight(flight);

		// Update used AircraftAvailability.
		usedAircraftAvailability.setDependentFlight(flight);
		aircraftAvailabilityService.saveAvailability(usedAircraftAvailability);

		// Create and update UserAvailabilities.
		updateUserAvailabilities(flight, crewAvailabilitySet);

		return flight;
	}

	private void findCrew (Set<UserAvailability> crewAvailabilitySet, Flight flight, UserRole role, int requiredCrew) throws InsufficientStaffException{
		Set<User> crewSet;
		switch (role) {
		case PILOT:
			crewSet = flight.getPilots();
			break;
		case CREW:
			crewSet = flight.getCrew();
			break;
		default:
			throw new IllegalArgumentException("The given role must either be PILOT or CREW.");
		}

		for ( UserAvailability userAvailability : userAvailabilityService.getAllAvailabilities(flight.getDepartureTime(), flight.getOriginAirport(), role) ) {
			if (crewSet.size() >= requiredCrew) {
				break;
			}
			if ( ( userAvailability.hasDependentFlight() )
					&& (! userCanWorkFlight(userAvailability.getUser(), flight))
					&& (crewSet.contains(userAvailability.getUser()))) {
				continue;

			} else {
				crewAvailabilitySet.add(userAvailability);
				crewSet.add(userAvailability.getUser());				
			}
		}
		if ( crewSet.size() < requiredCrew ) {
			switch (role) {
			case PILOT:
				throw new InsufficientStaffException("There are not enough pilots available.");
			case CREW:
				throw new InsufficientStaffException("There are not enough cabin staff available.");
			default:
				throw new InsufficientStaffException();
			}
		}
	}

	private void initiateFlightId() {
		flightId = flightService.getAllFlightsWithCancelled().size() + 1L;
	}

	/**
	 * Find an AircraftAvailability object containing an Aircraft that is suitable for the given flight.
	 * 
	 * @param flight
	 * @param requiredSeats
	 * @return
	 * @throws NoSuitableAircraftFoundException
	 */
	private AircraftAvailability findAircraftAvailability (Flight flight, int requiredSeats) throws NoSuitableAircraftFoundException {
		AircraftAvailability usedAircraftAvailability = null;
		for ( AircraftAvailability aircraftAvailability : aircraftAvailabilityService.getAllAvailabilities(flight.getDepartureTime(), flight.getOriginAirport()) ) {
			if ( aircraftAvailability.getAircraft().getSeats() < requiredSeats) {
				continue;

			} else if ( aircraftAvailability.hasDependentFlight() ) {
				continue;

			} else {
				usedAircraftAvailability = aircraftAvailability;
				break;
			}
		}

		if ( usedAircraftAvailability == null ) {
			throw new NoSuitableAircraftFoundException("No available aircraft matches the required criteria.");
		}

		flight.setAircraft(usedAircraftAvailability.getAircraft());
		flight.setPassengerCount(usedAircraftAvailability.getAircraft().getSeats());
		return usedAircraftAvailability;
	}

	private void updateUserAvailabilities(Flight flight, Set<UserAvailability> crewAvailabilitySet) {
		// Create new UserAvailabilities.
		for (User user : flight.getPilots()) {
			UserAvailability newUserAvailability = new UserAvailability(user, Time.addHours(flight.getArrivalTime(), MIN_CREW_STAY), flight.getDestinationAirport());
			flight.getDependentUserAvailabilities().add(newUserAvailability);
			userAvailabilityService.saveAvailability(newUserAvailability);
		}
		for (User user : flight.getCrew()) {
			UserAvailability newUserAvailability = new UserAvailability(user, Time.addHours(flight.getArrivalTime(), MIN_CREW_STAY), flight.getDestinationAirport());
			flight.getDependentUserAvailabilities().add(newUserAvailability);
			userAvailabilityService.saveAvailability(newUserAvailability);
		}

		// save flight.
		flightService.saveFlight(flight);

		// Update used UserAvailabilities.
		for (UserAvailability userAvailability : crewAvailabilitySet) {
			userAvailability.setDependentFlight(flight);
			userAvailabilityService.saveAvailability(userAvailability);
		}
	}

	/**
	 * Complete flight staff.
	 *
	 * @param flight the flight
	 * @return the flight
	 */
	public Flight completeFlightStaff (Flight flight) {
		Set<UserAvailability> crewAvailabilitySet = new HashSet<UserAvailability>();
		flight.setComplete(true);

		// Find Pilots.
		try {
			findCrew(crewAvailabilitySet, flight, UserRole.PILOT, flight.getAircraft().getRequiredPilots());
		} catch (InsufficientStaffException e) {
			flight.setComplete(false);
		}

		// Find Crew.
		try {
			findCrew(crewAvailabilitySet, flight, UserRole.CREW, flight.getAircraft().getRequiredCrew());
		} catch (InsufficientStaffException e) {
			flight.setComplete(false);
		}

		// Create and update UserAvailabilities.
		updateUserAvailabilities(flight, crewAvailabilitySet);

		return flight;
	}
}
