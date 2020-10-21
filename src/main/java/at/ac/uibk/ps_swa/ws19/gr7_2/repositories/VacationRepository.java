package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Entity representing users.
 *
 * @author Christoph Wittauer
 */
public interface VacationRepository extends AbstractRepository <Vacation, String> {
        /**
         * Find first vacation by vacationId.
         *
         * @param vacationId the vacation id
         * @return the vacation
         */
        Vacation findFirstByVacationId(int vacationId);

        /**
         * Find all vacations from user.
         *
         * @param user the user
         * @return the list
         */
        @Query("SELECT u FROM Vacation u WHERE :user = u.createUser")
        List<Vacation> findAllFromUser(@Param("user") User user);

        /**
         * Find all future vacations from user.
         *
         * @param user the user
         * @return the list
         */
        @Query("SELECT u FROM Vacation u WHERE :user = u.createUser AND u.vacationStart >= CURRENT_TIMESTAMP")
        List<Vacation> findFutureFromUser(@Param("user") User user);
}
