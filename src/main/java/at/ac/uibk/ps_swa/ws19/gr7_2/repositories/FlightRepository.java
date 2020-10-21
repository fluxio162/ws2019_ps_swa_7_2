package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * The interface Flight repository.
 */
public interface FlightRepository extends AbstractRepository<Flight,Long>{
	/**
	 * Find first flight by flightId.
	 *
	 * @param flightId the flight id
	 * @return the flight
	 */
	Flight findFirstByFlightId(long flightId);

	/**
	 * Find all with flights without cancelled flights.
	 *
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.cancelled = false")
	List<Flight> findAll();

	/**
	 * Find all flights with cancelled list.
	 *
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u")
	List<Flight> findAllWithCancelled();

	/**
	 * Find completed flights.
	 *
	 * @param time the time
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.arrivalTime < :time AND u.cancelled = false")
	List<Flight> findCompletedFlights(@Param("time") Date time);

	/**
	 * Find current flights.
	 *
	 * @param time the time
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.departureTime <= :time AND u.arrivalTime > :time AND u.cancelled = false")
	List<Flight> findCurrentFlights(@Param("time") Date time);

	/**
	 * Find planned flights.
	 *
	 * @param time the time
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.departureTime > :time AND u.cancelled = false")
	List<Flight> findPlannedFlights(@Param("time") Date time);

	/**
	 * Find flights with user in timeframe.
	 *
	 * @param user the user
	 * @param from departureTime
	 * @param to   arrivalTime
	 * @return the list
	 */
	@Query("SELECT f FROM Flight f WHERE (:user MEMBER OF f.pilots OR :user MEMBER OF f.crew) AND (f.departureTime BETWEEN :from AND :to OR f.arrivalTime BETWEEN :from AND :to)")
	List<Flight> findFlightsWithUserInTimeframe(@Param("user") User user, @Param("from") Date from, @Param("to") Date to);

	/**
	 * Find incomplete flights.
	 *
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.complete = false AND u.cancelled = false")
	List<Flight> findIncompleteFlights();

	/**
	 * Find cancelled flights.
	 *
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.cancelled = true")
	List<Flight> findCancelledFlights();

	/**
	 * Find all flights from user.
	 *
	 * @param user the user
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE :user MEMBER OF u.pilots OR :user MEMBER OF u.crew")
	List<Flight> findAllFromUser(@Param("user") User user);

	/**
	 * Find all planned flights from user.
	 *
	 * @param user the user
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE (:user MEMBER OF u.pilots OR :user MEMBER OF u.crew) AND u.departureTime > current_timestamp ")
	List<Flight> findAllPlannedFromUser(@Param("user") User user);

	/**
	 * Find all planned flights from aircraft.
	 *
	 * @param aircraft the aircraft
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE u.aircraft = :aircraft AND u.departureTime > current_timestamp ")
	List<Flight> findAllPlannedFromAircraft(@Param("aircraft") Aircraft aircraft);

	/**
	 * Find all flights by airport.
	 *
	 * @param airport the airport
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE (u.originAirport = :airport OR u.destinationAirport = :airport) AND u.departureTime > current_timestamp ")
	List<Flight> findAllByAirport(@Param("airport") Airport airport);


	/**
	 * Find all flights from pilot.
	 *
	 * @param user the user
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE :user MEMBER OF u.pilots")
    List<Flight> findAllFromPilot(@Param("user") User user);

	/**
	 * Find all flights from crew.
	 *
	 * @param user the user
	 * @return the list
	 */
	@Query("SELECT u FROM Flight u WHERE :user MEMBER OF u.crew")
    List<Flight> findAllFromCrew(@Param("user") User user);
}
