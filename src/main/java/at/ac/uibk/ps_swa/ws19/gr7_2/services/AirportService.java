package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.AirportRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;


/**
 * Service for accessing and manipulating airport data.
 *
 * @author Benedikt Schenk
 */
@Component
@Scope("application")
public class AirportService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportService.class);

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Gets all airports.
     *
     * @return the all airports
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    /**
     * Load airport.
     *
     * @param iataCode the iata code
     * @return the airport
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Airport loadAirport(String iataCode) { return airportRepository.findFirstByIataCode(iataCode); }

    /**
     * Load home airport.
     *
     * @return the airport
     */
    @PreAuthorize("isAuthenticated()")
    public Airport loadHomeAirport() { return airportRepository.findFirstByIataCode("INN"); }

    /**
     * Load airport by name.
     *
     * @param name the name
     * @return the airport
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Airport loadAirportByName(String name) { return airportRepository.findFirstByName(name); }

    /**
     * Saves the airport. This method will also set createDate}for new
     * entities or updateDate for updated entities. The user
     * requesting this operation will also be stored as createDate
     * or updateAirport respectively.
     *
     * @param airport the airport to save
     * @return the updated airport
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Airport saveAirport(Airport airport) {
        if (airport.isNew()) {
            airport.setCreateDate(new Date());
            airport.setCreateUser(getAuthenticatedUser());
        } else {
            airport.setUpdateDate(new Date());
            airport.setUpdateUser(getAuthenticatedUser());
        }
        return airportRepository.save(airport);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Deletes the airport.
     *
     * @param airport the airport to delete
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void deleteAirport(Airport airport) {
        if(airport != null){
            airport.setDeleteUser(getAuthenticatedUser());
            airport.setDeleteDate(new Date());
            airport.setEnabled(false);
            saveAirport(airport);

            LOGGER.info("Deleted Airport {} by {}", airport.getIataCode(), airport.getDeleteUser().getUsername());
        }
    }
}
