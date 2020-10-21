package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.AircraftAvailabilityRepository;

/**
 * The type Aircraft availability service.
 *
 * @author Philipp Schießl
 */
@Component
@Scope("application")
public class AircraftAvailabilityService {

	@Autowired
	private AircraftAvailabilityRepository availabilityRepository;

	/**
	 * Gets all availabilities.
	 *
	 * @param timestamp the timestamp
	 * @param location  the location
	 * @return the all availabilities
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public Collection<AircraftAvailability> getAllAvailabilities(Date timestamp, Airport location) {

		return availabilityRepository.getAllAvailable(timestamp, location);
	}

	/**
	 * Gets all available aircraft.
	 *
	 * @param timestamp the required date/time
	 * @param location  the required location
	 * @return a list of available aircraft at the given location and date/time
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public Collection<Aircraft> getAllAvailableAircraft(Date timestamp, Airport location) {

		Collection<Aircraft> aircraft = new LinkedList<>();
		Collection<AircraftAvailability> availabilities = availabilityRepository.getAllAvailable(timestamp, location);

		for (AircraftAvailability availability : availabilities) {
			aircraft.add(availability.getAircraft());
		}

		return aircraft;
	}

	/**
	 * Get all availabilities from aircraft.
	 *
	 * @param aircraft the aircraft
	 * @return the list
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public List<AircraftAvailability> getAllFromAircraft(Aircraft aircraft){
		return availabilityRepository.getAllFromAircraft(aircraft);
	}


	/**
	 * Get all future availabilities from aircraft.
	 *
	 * @param aircraft the aircraft
	 * @param date     the date
	 * @return the list
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public List<AircraftAvailability> getAllFutureWithAircraft(Aircraft aircraft, Date date){
		return availabilityRepository.getAllFutureWithAircraft(aircraft, date);
	}

	/**
	 * Get all future availabilities by airport.
	 *
	 * @param airport the airport
	 * @return the list
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public List<AircraftAvailability> getAllFutureByAirport(Airport airport){
		return availabilityRepository.getAllFutureByAirport(airport);
	}

	/**
	 * Save availability.
	 *
	 * @param availability the new availability
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public void saveAvailability(AircraftAvailability availability) {
		if(availability.isNew()){
			availability.setCreateDate(new Date());
		}
		availabilityRepository.save(availability);
	}

	/**
	 * Delete availability.
	 *
	 * @param availability the availability
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public void deleteAvailability(AircraftAvailability availability) {
		availability.setLocation(null);
		saveAvailability(availability);
	}
}
