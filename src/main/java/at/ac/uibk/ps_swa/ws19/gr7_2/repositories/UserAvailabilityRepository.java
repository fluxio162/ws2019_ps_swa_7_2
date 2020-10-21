package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;

import java.util.Date;
import java.util.List;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availabilityKeys.UserAvailabilityId;

/**
 * The interface User availability repository.
 *
 * @author Philipp Schießl
 */
public interface UserAvailabilityRepository extends AbstractRepository <UserAvailability, UserAvailabilityId> {

	/**
	 * Gets all available User.
	 *
	 * @param timestamp the timestamp
	 * @param location  the location
	 * @return List of available Users.
	 */
	@Query("SELECT a FROM UserAvailability a WHERE a.location = :location AND a.timestamp = (SELECT MAX(s.timestamp) FROM UserAvailability s WHERE a.user = s.user AND s.timestamp <= :timestamp)")
	List<UserAvailability> getAllAvailable(@Param("timestamp") Date timestamp, @Param("location") Airport location);

	/**
	 * Gets all future Availabilities from user.
	 *
	 * @param user      the user
	 * @param timestamp the timestamp
	 * @return List of future availabilities from user.
	 */
	@Query("SELECT a FROM UserAvailability a WHERE a.user = :user AND a.timestamp >= :timestamp")
	List<UserAvailability> getAllFutureWithUser(@Param("user") User user, @Param("timestamp") Date timestamp);

	/**
	 * Gets all availabilities from user.
	 *
	 * @param user the user
	 * @return List of all availabilities from user.
	 */
	@Query("SELECT a FROM UserAvailability  a WHERE a.user = :user")
	List<UserAvailability> getAllFromUser(@Param("user") User user);

	/**
	 * Gets all future availabilities by airport.
	 *
	 * @param airport the airport
	 * @return List of all availabilities by airport.
	 */
	@Query("SELECT a FROM UserAvailability a WHERE a.location = :airport AND a.timestamp >= current_timestamp ")
	List<UserAvailability> getAllFutureByAirport(@Param("airport") Airport airport);
}
