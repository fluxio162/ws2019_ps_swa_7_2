package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import java.util.Collection;

import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the airport list view.
 *
 * @author Benedikt Schenk
 */
@Component
@Scope("view")
public class AirportListController {

    @Autowired
    private AirportService airportService;

    private Collection<Airport> airportCollection;

    /**
     * Returns a list of all airports.
     *
     * @return collection of all airports
     */
    public Collection<Airport> getAirports(){
        if(airportCollection == null){
            airportCollection = airportService.getAllAirports();
        }
        return airportCollection;
    }
}