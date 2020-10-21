package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The interface Airport repository.
 */
public interface AirportRepository extends AbstractRepository<Airport,String>{
	/**
	 * Find first airport by iata code.
	 *
	 * @param iataCode the iata code
	 * @return the airport
	 */
	Airport findFirstByIataCode(String iataCode);

	/**
	 * Find first airport by name.
	 *
	 * @param name the name
	 * @return the airport
	 */
	@Query("SELECT u FROM Airport u WHERE u.name = :name")
	Airport findFirstByName(String name);

	/**
	 * Find first all airports.
	 *
	 * @return list of airports
	 */
	@Query("SELECT u FROM Airport u WHERE u.enabled = true")
	List<Airport> findAll();
}
