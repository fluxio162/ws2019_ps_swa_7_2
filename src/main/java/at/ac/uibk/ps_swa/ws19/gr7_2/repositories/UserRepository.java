package at.ac.uibk.ps_swa.ws19.gr7_2.repositories;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for managing {@link User} entities.
 */
public interface UserRepository extends AbstractRepository<User, String> {

    /**
     * Find first user by username.
     *
     * @param username the username
     * @return the user
     */
    User findFirstByUsername(String username);

    /**
     * Find user by username containing string.
     *
     * @param username the username
     * @return the list
     */
    List<User> findByUsernameContaining(String username);

    /**
     * Find user by whole name.
     *
     * @param wholeName the whole name
     * @return the list
     */
    @Query("SELECT u FROM User u WHERE CONCAT(u.firstName, ' ', u.lastName) = :wholeName")
    List<User> findByWholeNameConcat(@Param("wholeName") String wholeName);

    /**
     * Find user by role.
     *
     * @param role the role
     * @return the list
     */
    @Query("SELECT u FROM User u WHERE :role MEMBER OF u.roles")
    List<User> findByRole(@Param("role") UserRole role);

    /**
     * Finds all users with enabled = true.
     *
     * @return the list
     */
    @Query("SELECT u FROM User u WHERE NOT u.firstName = 'Test' AND u.enabled = true ")
    List<User> findAll();
}
