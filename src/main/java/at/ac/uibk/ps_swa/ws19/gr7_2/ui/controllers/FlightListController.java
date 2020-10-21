package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import java.util.Collection;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Controller for the flight list view.
 *
 * @author Benedikt Schenk
 */
@Component
@Scope("view")
public class FlightListController {

    @Autowired
    private FlightService flightService;


    private Collection<Flight> flightCollection;

    /**
     * Returns a list of all flights.
     *
     * @return collection of all flights
     */
    public Collection<Flight> getFlights() {
        if(flightCollection == null){
            flightCollection = flightService.getAllFlights();
        }
        return flightCollection;
    }

    /**
     * Get completed flights.
     *
     * @return the collection
     */
    public Collection<Flight> getCompletedFlights(){
        if(flightCollection == null){
            flightCollection = flightService.getCompletedFlights();
        }
        return flightCollection;
    }

    /**
     * Get current flights.
     *
     * @return the collection
     */
    public Collection<Flight> getCurrentFlights(){
        if(flightCollection == null){
            flightCollection = flightService.getCurrentFlights();
        }
        return flightCollection;
    }

    /**
     * Get planned flights.
     *
     * @return the collection
     */
    public Collection<Flight> getPlannedFlights(){
        if(flightCollection == null){
            flightCollection = flightService.getPlannedFlights();
        }
        return flightCollection;
    }

    /**
     * Get incomplete flights.
     *
     * @return the collection
     */
    public Collection<Flight> getIncompleteFlights(){
        if(flightCollection == null){
            flightCollection = flightService.getIncompleteFlights();
        }
        return flightCollection;
    }

    /**
     * Get canceled flights.
     *
     * @return the collection
     */
    public Collection<Flight> getCanceledFlights(){
        if(flightCollection == null){
            flightCollection = flightService.getAllCanceledFlights();
        }
        return flightCollection;
    }
}