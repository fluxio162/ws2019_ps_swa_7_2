package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftService;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the aircraft list view.
 *
 * @author Christoph Kugler
 */
@Component
@Scope("view")
public class AircraftListController {

    @Autowired
    private AircraftService aircraftService;

    private Collection<Aircraft> aircraftCollection;

    /**
     * Returns a list of all aircraft.
     *
     * @return collection of all aircraft
     */
    public Collection<Aircraft> getAircraft(){
        if(aircraftCollection == null){
            aircraftCollection = aircraftService.getAllAircraft();
        }
        return aircraftCollection;
    }
}
