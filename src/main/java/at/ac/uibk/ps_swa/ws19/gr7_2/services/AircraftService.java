package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.UserRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.AircraftRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;


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
 * Service for accessing and manipulating aircraft data.
 */
@Component
@Scope("application")
public class AircraftService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AircraftService.class);

    @Autowired
    private AircraftRepository aircraftRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FlightService flightService;

    @Autowired
    private AircraftAvailabilityService aircraftAvailabilityService;

    /**
     * Returns a collection of all aircraft.
     *
     * @return collection of all aircraft
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Collection<Aircraft> getAllAircraft() {
        return aircraftRepository.findAll();
    }

    /**
     * Loads a single aircraft identified by its aircraftId.
     *
     * @param aircraftId the aircraftId to search for
     * @return the aircraft with the given aircraftId
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Aircraft loadAircraft(int aircraftId) {
        return aircraftRepository.findFirstByAircraftId(aircraftId);
    }

    /**
     * Saves the aircraft. This method will also set createDate for new
     * entities or pdateDate for updated entities. The user
     * requesting this operation will also be stored as createDate
     * or updateAircraft respectively.
     *
     * @param aircraft the aircraft to save
     * @return the updated aircraft
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public Aircraft saveAircraft(Aircraft aircraft) {
        if (aircraft.isNew()) {
            aircraft.setCreateDate(new Date());
            aircraft.setCreateUser(getAuthenticatedUser());
        } else {
            aircraft.setUpdateDate(new Date());
            aircraft.setUpdateUser(getAuthenticatedUser());
        }
        return aircraftRepository.save(aircraft);
    }

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findFirstByUsername(auth.getName());
    }

    /**
     * Delete aircraft.
     *
     * @param aircraft the aircraft
     */
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER')")
    public void deleteAircraft(Aircraft aircraft) {
        if(aircraft != null){
            aircraft.setDeleteUser(getAuthenticatedUser());
            aircraft.setDeleteDate(new Date());
            aircraft.setEnabled(false);
            aircraftRepository.save(aircraft);
            flightService.deleteAvailabilitiesAfter(aircraft, aircraft.getDeleteDate());
            aircraftAvailabilityService.saveAvailability(new AircraftAvailability(aircraft, aircraft.getDeleteDate(), null));

            LOGGER.info("Deleted Aircraft {} by {}", aircraft.getAircraftId(), aircraft.getDeleteUser().getUsername());
        }
    }
}