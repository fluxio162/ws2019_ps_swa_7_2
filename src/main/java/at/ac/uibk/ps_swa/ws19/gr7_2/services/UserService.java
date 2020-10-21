package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.UserRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import java.util.Collection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Service for accessing and manipulating user data.
 */
@Component
@Scope("application")
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FlightService flightService;
    
    @Autowired
    private UserAvailabilityService userAvailabilityService;


    /**
     * Returns a collection of all users.
     *
     * @return collection of all users
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Returns a collection of all users without test user.
     *
     * @return collection of all users without test user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<User> getAllUsersWithoutTestUser() {
        return userRepository.findAll();
    }


    /**
     * Loads a single user identified by its username.
     *
     * @param username the username to search for
     * @return the user with the given username
     */
    @PreAuthorize("hasAuthority('ADMIN') or principal.username eq #username or hasAuthority('MANAGER') ")
    public User loadUser(String username) {
        return userRepository.findFirstByUsername(username);
    }

    /**
     * Saves the user. This method will also set reateDate for new
     * entities or updateDate for updated entities. The user
     * requesting this operation will also be stored as createDate
     * or updateUser respectively.
     *
     * @param user the user to save
     * @return the updated user
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public User saveUser(User user) {
        if (user.isNew()) {
            user.setCreateDate(new Date());
            user.setCreateUser(getAuthenticatedUser());
        } else {
            user.setUpdateDate(new Date());
            user.setUpdateUser(getAuthenticatedUser());
        }
        return userRepository.save(user);
    }

    /**
     * Deletes the user.
     *
     * @param user the user to delete
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void deleteUser(User user) {
        if(user != null){
            user.setDeleteUser(getAuthenticatedUser());
            user.setDeleteDate(new Date());
            user.setEnabled(false);
            userRepository.save(user);
            flightService.deleteAvailabilitiesAfter(user, user.getDeleteDate());
            userAvailabilityService.saveAvailability(new UserAvailability(user, user.getDeleteDate(), null));
            

            LOGGER.info("Deleted User {} by {}", user.getUsername(), user.getDeleteUser().getUsername());
        }
    }

    /**
     * Returns a collection of all users with the given role.
     *
     * @param role desired role
     * @return collection of all users with the given role
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<User> getAllUsers(UserRole role) {
        return userRepository.findByRole(role);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

}
