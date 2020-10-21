package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;

import java.util.Date;
import java.util.List;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availabilityKeys.AircraftAvailabilityId;

/**
 * The interface Aircraft availability repository.
 *
 * @author Philipp Schießl
 */
public interface AircraftAvailabilityRepository extends AbstractRepository <AircraftAvailability, AircraftAvailabilityId> {

	/**
	 * Gets all available aircraft.
	 *
	 * @param timestamp the timestamp
	 * @param location  the location
	 * @return the all available
	 */
	@Query("SELECT a FROM AircraftAvailability a WHERE a.location = :location AND a.timestamp = (SELECT MAX(s.timestamp) FROM AircraftAvailability s WHERE a.aircraft = s.aircraft AND s.timestamp <= :timestamp)")
	List<AircraftAvailability> getAllAvailable(@Param("timestamp") Date timestamp, @Param("location") Airport location);

	/**
	 * Gets all future availabilities from aircraft.
	 *
	 * @param aircraft  the aircraft
	 * @param timestamp the timestamp
	 * @return the all future with aircraft
	 */
	@Query("SELECT a FROM AircraftAvailability a WHERE a.aircraft = :aircraft AND a.timestamp >= :timestamp")
	List<AircraftAvailability> getAllFutureWithAircraft(@Param("aircraft") Aircraft aircraft, @Param("timestamp") Date timestamp);

	/**
	 * Gets all availabilities from aircraft.
	 *
	 * @param aircraft the aircraft
	 * @return the all from aircraft
	 */
	@Query("SELECT a FROM AircraftAvailability a WHERE a.aircraft = :aircraft")
	List<AircraftAvailability> getAllFromAircraft(@Param("aircraft") Aircraft aircraft);

	/**
	 * Gets all future availabilities by airport.
	 *
	 * @param airport the airport
	 * @return the all future by airport
	 */
	@Query("SELECT a FROM AircraftAvailability a WHERE a.location = :airport AND a.timestamp >= current_timestamp ")
	List<AircraftAvailability> getAllFutureByAirport(@Param("airport") Airport airport);
}
