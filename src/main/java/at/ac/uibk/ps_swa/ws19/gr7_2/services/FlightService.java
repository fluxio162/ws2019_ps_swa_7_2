package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailCreateExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionSuperCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailSendExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.FlightFactory;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.FlightRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.UserRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.utils.Time;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.InsufficientStaffException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.NoSuitableAircraftFoundException;

import java.io.UnsupportedEncodingException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


/**
 * Service for accessing and manipulating flight data.
 */
@Component
@Scope("application")
public class FlightService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FlightFactory flightFactory;

	@Autowired
	private AircraftAvailabilityService aircraftAvailabilityService;

	@Autowired
	private UserAvailabilityService userAvailabilityService;

	@Autowired
	private EMailFactory emailFactory;

	@Autowired
	private EMailService emailService;

	/**
	 * Gets all flights.
	 *
	 * @return the all flights
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getAllFlights() {
		return flightRepository.findAll();
	}

	/**
	 * Gets all flights with cancelled flights.
	 *
	 * @return the all flights with cancelled
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getAllFlightsWithCancelled() {
		return flightRepository.findAllWithCancelled();
	}

	/**
	 * Gets all canceled flights.
	 *
	 * @return the all canceled flights
	 */
	public Collection<Flight> getAllCanceledFlights() {
		return flightRepository.findCancelledFlights();
	}

	/**
	 * Load flight.
	 *
	 * @param flightId the flight id
	 * @return the flight
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Flight loadFlight(long flightId) {
		return flightRepository.findFirstByFlightId(flightId);
	}

	/**
	 * Save flight.
	 *
	 * @param flight the flight
	 * @return the flight
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Flight saveFlight(Flight flight) {
		if (flight.isNew()) {
			flight.setCreateDate(new Date());
			flight.setCreateUser(getAuthenticatedUser());
		} else {
			flight.setUpdateDate(new Date());
			flight.setUpdateUser(getAuthenticatedUser());
		}
		if (flight.getCreateDate() == null){
			flight.setCreateDate(new Date());
		}
		if (flight.getCreateUser() == null){
			flight.setCreateUser(getAuthenticatedUser());
		}
		return flightRepository.save(flight);
	}

	private User getAuthenticatedUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findFirstByUsername(auth.getName());
	}

	/**
	 * Gets completed flights.
	 *
	 * @return the completed flights
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getCompletedFlights() {
		return flightRepository.findCompletedFlights(new Date());
	}


	/**
	 * Gets current flights.
	 *
	 * @return the current flights
	 */
	public Collection<Flight> getCurrentFlights() {
		return flightRepository.findCurrentFlights(new Date());
	}

	/**
	 * Gets planned flights.
	 *
	 * @return the planned flights
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getPlannedFlights() {
		return flightRepository.findPlannedFlights(new Date());
	}

	/**
	 * Gets all flights with user in timeframe.
	 *
	 * @param user the user
	 * @param from the from
	 * @param to   the to
	 * @return the all flights with user in timeframe
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getAllFlightsWithUserInTimeframe(User user, Date from, Date to) {
		return flightRepository.findFlightsWithUserInTimeframe(user, from, to);
	}

	/**
	 * Gets user workload in week of date.
	 *
	 * @param user the user
	 * @param date the date
	 * @return the amount of time the user has worked in the week of the given date in milliseconds
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public long getUserWorkloadInWeekOfDate(User user, Date date) {
		long timeWorked = 0;
		Date startOfWeek = Time.startOfWeek(date);
		Date endOfWeek = Time.endOfWeek(date);
		Collection<Flight> flights = getAllFlightsWithUserInTimeframe(user, startOfWeek, endOfWeek);
		for (Flight flight : flights) {
			timeWorked += Long.min(endOfWeek.getTime(), flight.getArrivalTime().getTime()) - Long.max(startOfWeek.getTime(), flight.getDepartureTime().getTime());
		}
		return timeWorked;
	}

	/**
	 * Gets incomplete flights.
	 *
	 * @return the incomplete flights
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getIncompleteFlights() {
		return flightRepository.findIncompleteFlights();
	}

	/**
	 * Find all flights by airport.
	 *
	 * @param airport the airport
	 * @return the collection
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> findAllByAirport(Airport airport) {
		return flightRepository.findAllByAirport(airport);
	}

	/**
	 * Find all flights from user.
	 *
	 * @param user the user
	 * @return the collection
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> findAllFromUser(User user) {
		return flightRepository.findAllFromUser(user);
	}

	/**
	 * Find all planned flights from user.
	 *
	 * @param user the user
	 * @return the collection
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> findAllPlannedFromUser(User user) {
		return flightRepository.findAllPlannedFromUser(user);
	}

	/**
	 * Find all planned flights from aircraft.
	 *
	 * @param aircraft the aircraft
	 * @return the collection
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> findAllPlannedFromAircraft(Aircraft aircraft) {
		return flightRepository.findAllPlannedFromAircraft(aircraft);
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
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Flight createFlight(int requiredSeats, Airport origin, Airport destination, Date departure, Date arrival) throws NoSuitableAircraftFoundException, InsufficientStaffException {
		return flightFactory.createFlight(requiredSeats, origin, destination, departure, arrival);
	}

	/**
	 * Try recreate flight.
	 *
	 * @param flight the flight
	 * @throws NoSuitableAircraftFoundException the no suitable aircraft found exception
	 * @throws InsufficientStaffException       the insufficient staff exception
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public void tryRecreateFlight(Flight flight) throws NoSuitableAircraftFoundException, InsufficientStaffException {
		flightFactory.tryRecreateFlight(flight);
	}

	/**
	 * Cancels the flight.
	 *
	 * @param flight the flight to cancel
     * @throws EMailExceptionSuperCollector exception that encapsulates
     * EMailCreateExceptionCollector Exceptions (while email creation (factory)) and
     * EMailSendExceptionCollector Exceptions (while email sending (service))
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public void doCancelFlight(Flight flight) throws EMailExceptionSuperCollector {
	    LOGGER.debug("doCancelFlight: {}", flight);
        LOGGER.debug("pilots & crew amount: {}", flight.getFlightPersonnelAmount());

        List<EMailExceptionCollector<?>> exceptions = new LinkedList<>();
        // Create emails
        List<EMail> emails = null;
        try {
            emails = emailFactory.createFlightCancelledEMail(flight);
        } catch (EMailCreateExceptionCollector e) {
            exceptions.add(e);
        }

        // Actually cancel the flight
		cancelFlight(flight);

        // send the previously created emails
        try {
            if(emails != null) {
                LOGGER.debug("sending {} emails...", emails.size());
                emailService.sendEMails(emails);
            }
        } catch (EMailSendExceptionCollector e) {
            exceptions.add(e);
        }

        // If email exceptions occurred
        if(!exceptions.isEmpty()) {
            // Throw new exception that encapsulates all occurred exceptions
            throw new EMailExceptionSuperCollector(exceptions);
        }
        LOGGER.debug("Flight cancelled");
    }

	/**
	 * Cancel flight.
	 *
	 * @param flight the flight
	 */
	@PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public void cancelFlight(Flight flight) {
		if(flight != null){
			flight.setDeleteUser(getAuthenticatedUser());
			flight.setDeleteDate(new Date());
			flight.removeAllUserAndAircraft();
			flight.setCancelled(true);
			flightRepository.save(flight);

			deleteAvailabilities(flight);

			LOGGER.info("Cancelled Flight {} by {}", flight.getFlightId(), flight.getDeleteUser().getUsername());
		}
	}

	/**
	 * @param flight the flight
	 */
	private void deleteAvailabilities(Flight flight) {
		AircraftAvailability dependentAircraftAvailability = flight.getDependentAircraftAvailability();
		List<UserAvailability> dependentUserAvailabilities = new ArrayList<UserAvailability>(flight.getDependentUserAvailabilities());

		flight.setDependentAircraftAvailability(null);
		flight.setDependentUserAvailabilities(null);
		flightRepository.save(flight);

		deleteAvailabilitiesAfter(dependentAircraftAvailability.getAircraft(), dependentAircraftAvailability.getTimestamp());

		for (UserAvailability dependentUserAvailability : dependentUserAvailabilities) {
			deleteAvailabilitiesAfter(dependentUserAvailability.getUser(), dependentUserAvailability.getTimestamp());
		}
	}

	/**
	 * Delete availabilities after.
	 *
	 * @param user the user
	 * @param date the date
	 */
	public void deleteAvailabilitiesAfter(User user, Date date) {
		for (UserAvailability futureUserAvailability : userAvailabilityService.getAllFutureWithUser(user, date)) {
			userAvailabilityService.deleteAvailability(futureUserAvailability);
			if (futureUserAvailability.hasDependentFlight()) {
				Flight dependentFlight = futureUserAvailability.getDependentFlight();
				dependentFlight.getDependentUserAvailabilities().remove(futureUserAvailability);
				dependentFlight.getPilots().remove(futureUserAvailability.getUser());
				dependentFlight.getCrew().remove(futureUserAvailability.getUser());
				flightFactory.completeFlightStaff(dependentFlight);
			}
		}
	}

	/**
	 * Delete availabilities after.
	 *
	 * @param aircraft the aircraft
	 * @param date     the date
	 */
	public void deleteAvailabilitiesAfter(Aircraft aircraft, Date date) {
		for (AircraftAvailability aircraftAvailability : aircraftAvailabilityService.getAllFutureWithAircraft(aircraft, date)) {
			aircraftAvailabilityService.deleteAvailability(aircraftAvailability);
			if (aircraftAvailability.hasDependentFlight()) {
				Flight dependentFlight = aircraftAvailability.getDependentFlight();
				try {
					flightFactory.tryRecreateFlight(dependentFlight);
				} catch (NoSuitableAircraftFoundException | InsufficientStaffException e) {
					dependentFlight.setAircraft(null);
					dependentFlight.setComplete(false);
				}

			}
		}
	}

	/**
	 * Gets all flights from pilot.
	 *
	 * @param pilot the pilot
	 * @return the all from pilot
	 */
	@PreAuthorize("hasAuthority('PILOT') or hasAuthority('ADMIN')or hasAuthority('MANAGER') ")
	public Collection<Flight> getAllFromPilot(User pilot) {
		return flightRepository.findAllFromPilot(pilot);
	}

	/**
	 * Get all flights from crew collection.
	 *
	 * @param crew the crew
	 * @return the collection
	 */
	@PreAuthorize("hasAuthority('CREW') or hasAuthority('MANAGER') or hasAuthority('ADMIN')")
	public Collection<Flight> getAllFromCrew(User crew){
		return flightRepository.findAllFromCrew(crew);
	}
}