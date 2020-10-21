package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Vacation;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.UserRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;


/**
 * Service for accessing and manipulating vacation data.
 *
 * @author Christoph Wittauer
 */
@Component
@Scope("application")
public class VacationService {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserAvailabilityService userAvailabilityService;
    
    @Autowired
    private AirportService airportService;


    /**
     * Gets all vacations.
     *
     * @return the all vacation
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('PILOT') or hasAuthority('CREW') or hasAuthority('USER')")
    public Collection<Vacation> getAllVacation() {
        return vacationRepository.findAll();
    }

    /**
     * Gets all vacations from user.
     *
     * @param user the user
     * @return the all from user
     */
    @PreAuthorize("hasAuthority('PILOT') or hasAuthority('ADMIN')or hasAuthority('MANAGER') or hasAuthority('CREW') or hasAuthority('USER') ")
    public Collection<Vacation> getAllFromUser(User user) {
        return vacationRepository.findAllFromUser(user);
    }

    /**
     * Gets future vacations from user.
     *
     * @param user the user
     * @return the future from user
     */
    @PreAuthorize("hasAuthority('PILOT') or hasAuthority('ADMIN')or hasAuthority('MANAGER') or hasAuthority('CREW') or hasAuthority('USER') ")
    public Collection<Vacation> getFutureFromUser(User user) {
        return vacationRepository.findFutureFromUser(user);
    }

    /**
     * Load vacation.
     *
     * @param vacationId the vacation id
     * @return the vacation
     */

    @PreAuthorize("hasAuthority('PILOT') or hasAuthority('CREW') or hasAuthority('USER') ")
    public Vacation loadVacation(int vacationId) {
        return vacationRepository.findFirstByVacationId(vacationId);
    }


    /**
     * Save vacation.
     *
     * @param vacation the vacation
     * @return the vacation
     */
    @PreAuthorize("hasAuthority('PILOT') or hasAuthority('CREW') or hasAuthority('USER') ")
    public Vacation saveVacation(Vacation vacation){
        if (vacation.isNew()) {
            vacation.setCreateDate(new Date());
            vacation.setCreateUser(getAuthenticatedUser());
        }
        saveAvailabilities(vacation);
        return vacationRepository.save(vacation);
    }


    /**
     * Delete vacation.
     *
     * @param vacation the vacation
     */
    @PreAuthorize("hasAuthority('ADMIN') or  hasAuthority('MANAGER')")
    public void deleteVacation(Vacation vacation){
        vacationRepository.delete(vacation);
    }


    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }
    
    private void saveAvailabilities(Vacation vacation) {
    	userAvailabilityService.saveAvailability(new UserAvailability(vacation.getCreateUser(), vacation.getVacationStart(), null));
    	userAvailabilityService.saveAvailability(new UserAvailability(vacation.getCreateUser(), vacation.getVacationEnd(), airportService.loadHomeAirport()));
    }
}
