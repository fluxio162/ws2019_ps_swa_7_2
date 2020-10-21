package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.UserAvailabilityRepository;

/**
 * The type User availability service.
 *
 * @author Philipp Schießl
 */
@Component
@Scope("application")
public class UserAvailabilityService {

	@Autowired
	private UserAvailabilityRepository availabilityRepository;

	/**
	 * Gets all availabilities.
	 *
	 * @param timestamp the timestamp
	 * @param location  the location
	 * @param role      the role
	 * @return the all availabilities
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public Collection<UserAvailability> getAllAvailabilities (Date timestamp, Airport location, UserRole role) {
		Collection<UserAvailability> availabilities = availabilityRepository.getAllAvailable(timestamp, location);

		availabilities.removeIf(userAvailability -> !userAvailability.getUser().getRoles().contains(role));
		return availabilities;
	}

	/**
	 * Gets all future availablities from user.
	 *
	 * @param user the user
	 * @param date the date
	 * @return the all future with user
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public List<UserAvailability> getAllFutureWithUser(User user, Date date) {
		return availabilityRepository.getAllFutureWithUser(user, date);
	}

	/**
	 * Get all availabilities from user.
	 *
	 * @param user the user
	 * @return the list
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public List<UserAvailability> getAllFromUser(User user){
		return availabilityRepository.getAllFromUser(user);
	}

	/**
	 * Get all future availabilities by airport list.
	 *
	 * @param airport the airport
	 * @return the list
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public List<UserAvailability> getAllFutureByAirport(Airport airport){
		return availabilityRepository.getAllFutureByAirport(airport);
	}

	/**
	 * Gets all availabilities from available users.
	 *
	 * @param timestamp the required date/time
	 * @param location  the required location
	 * @param role      the users required role
	 * @return a list of available users with the given role at the given location and date/time
	 */
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
	public Collection<User> getAllAvailableUsers(Date timestamp, Airport location, UserRole role) {

		Collection<User> users = new LinkedList<User>();
		Collection<UserAvailability> userAvailabilities = availabilityRepository.getAllAvailable(timestamp, location);

		for (UserAvailability availability : userAvailabilities) {
			if ( availability.getUser().getRoles().contains(role) ) {
				users.add(availability.getUser());
			}
		}

		return users;
	}

	/**
	 * Save availability.
	 *
	 * @param availability the new availability
	 */
	@PreAuthorize("isAuthenticated()")
	public void saveAvailability(UserAvailability availability) {
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
	public void deleteAvailability(UserAvailability availability) {
		availability.setLocation(null);
		saveAvailability(availability);
	}
}

