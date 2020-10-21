package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftAvailabilityService;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailCreateExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionSuperCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailSendExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserAvailabilityService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for airport deletion.
 */
@Component
@Scope("view")
public class AirportDeletionController {

    @Autowired
    private AirportService airportService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private UserAvailabilityService userAvailabilityService;

    @Autowired
    private AircraftAvailabilityService aircraftAvailabilityService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AirportDeletionController.class);

    /**
     * Attribute to cache the currently displayed airport
     */
    private Airport airport;

    /**
     * Sets the currently displayed airport and reloads it form db. This airport is
     * targeted by any further calls of
     * {@link #doReloadAirport()}, and
     * {@link #doDeleteAirport()}.
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
     * @return airport airport
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
     * Action to delete the currently displayed airport.
     */
    public void doDeleteAirport() throws EMailExceptionSuperCollector {
        EMailExceptionSuperCollector emailException = null;
        try {
            cancelAllFlightsToAirport();
        }
        catch (EMailExceptionSuperCollector ex) {
            emailException = ex;
        }
        deleteAvailabilities();
        airportService.deleteAirport(airport);

        if(emailException != null) throw emailException;
        else refreshPage();
    }

    /**
     * Action to cancel all upcoming flights to and from displayed airport.
     */
    public void cancelAllFlightsToAirport() throws EMailExceptionSuperCollector {
        Collection<Flight> flightList = flightService.findAllByAirport(airport);

        List<EMailExceptionCollector<?>> exceptions = new LinkedList<>();
        for (Flight flight : flightList) {
            try {
                flightService.doCancelFlight(flight);
            } catch (EMailExceptionSuperCollector e) {
                exceptions.add(e);
            }
        }

        if(!exceptions.isEmpty()) {
            throw new EMailExceptionSuperCollector(exceptions);
        }
    }

    public void refreshPage() {
        LOGGER.info("refreshing Page");
        PrimeFaces.current().ajax().update("airportForm");

        String refreshpage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        ViewHandler handler = FacesContext.getCurrentInstance().getApplication().getViewHandler();
        UIViewRoot root = handler.createView(FacesContext.getCurrentInstance(), refreshpage);
        root.setViewId(refreshpage);
        FacesContext.getCurrentInstance().setViewRoot(root);
    }

    /**
     * Delete availabilities.
     */
    public void deleteAvailabilities(){
        for(UserAvailability ua : userAvailabilityService.getAllFutureByAirport(airport)){
            userAvailabilityService.deleteAvailability(ua);
        }
        for(AircraftAvailability aa : aircraftAvailabilityService.getAllFutureByAirport(airport)){
            aircraftAvailabilityService.deleteAvailability(aa);
        }
    }
}
