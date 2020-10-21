package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the airport detail view.
 *
 * @author Benedikt Schenk
 */
@Component
@Scope("view")
public class AirportDetailController {

    @Autowired
    private AirportService airportService;

    /**
     * Attribute to cache the currently displayed airport
     */
    private Airport airport;

    /**
     * Sets the currently displayed airport and reloads it form db. This airport is
     * targeted by any further calls of
     * {@link #doReloadAirport()} ()}, {@link #doSaveAirport()} and
     *
     * @param airport the airport
     */
    public void setAirport(Airport airport) {
        this.airport = airport;
        doReloadAirport();
    }

    /**
     * Returns the currently displayed airport.
     *
     * @return airport
     */
    public Airport getAirport() {
        return airport;
    }

    /**
     * Action to force a reload of the currently displayed airport.
     */
    public void doReloadAirport() {
        airport = airportService.loadAirport(airport.getIataCode());
    }

    /**
     * Action to save the currently displayed airport.
     */
    public void doSaveAirport() {
        airport = this.airportService.saveAirport(airport);
    }

}
