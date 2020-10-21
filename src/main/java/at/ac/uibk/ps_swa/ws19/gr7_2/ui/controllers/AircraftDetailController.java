package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the aircraft detail view.
 *
 * @author Christoph Kugler
 */
@Component
@Scope("view")
public class AircraftDetailController {

    @Autowired
    private AircraftService aircraftService;

    /**
     * Attribute to cache the currently displayed aircraft
     */
    private Aircraft aircraft;

    /**
     * Sets the currently displayed aircraft and reloads it form db. This aircraft is
     * targeted by any further calls of
     * {@link #doReloadAircraft()}, {@link #doSaveAircraft()} and
     *
     * @param aircraft the aircraft
     */
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
        doReloadAircraft();
    }

    /**
     * Returns the currently displayed aircraft.
     *
     * @return aircraft
     */
    public Aircraft getAircraft() {
        return aircraft;
    }

    /**
     * Action to force a reload of the currently displayed aircraft.
     */
    public void doReloadAircraft() {
        aircraft = aircraftService.loadAircraft(aircraft.getAircraftId());
    }

    /**
     * Action to save the currently displayed aircraft.
     */
    public void doSaveAircraft() {
        aircraft = this.aircraftService.saveAircraft(aircraft);
    }

}
