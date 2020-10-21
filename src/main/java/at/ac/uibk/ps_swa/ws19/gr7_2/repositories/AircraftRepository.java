package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.AircraftType;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link Aircraft} entities.
 *
 * @author Christoph Kugler
 */
public interface AircraftRepository extends AbstractRepository<Aircraft, String> {

    /**
     * Find first by aircraft by aircraftId.
     *
     * @param aircraftId the aircraft id
     * @return the aircraft
     */
    Aircraft findFirstByAircraftId(int aircraftId);

    /**
     * Find by aircraft type.
     *
     * @param type the type
     * @return the list
     */
    @Query("SELECT u FROM Aircraft u WHERE u.type = :type")
    List<Aircraft> findByAircraftType(@Param("type") AircraftType type);

    /**
     * Find all aircraft with enabled = true.
     *
     * @return list of aircraft
     */
    @Query("SELECT u FROM Aircraft u WHERE u.enabled = true")
    List<Aircraft> findAll();


}
