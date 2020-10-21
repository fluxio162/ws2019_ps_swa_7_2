package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.InsufficientStaffException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.NoSuitableAircraftFoundException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionSuperCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContext;

import javax.faces.application.FacesMessage;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

/**
 * Controller for the flight detail view.
 *
 * @author Benedikt Schenk
 */
@Component
@Scope("view")
public class FlightDetailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlightDetailController.class);

    private final String WARNING = "Warning!";

    @Autowired
    private FlightService flightService;
    private Flight flight;

    /**
     * Sets flight.
     *
     * @param flight the flight
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
        doReloadFlight();
    }

    /**
     * Gets flight.
     *
     * @return the flight
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * Do reload flight.
     */
    public void doReloadFlight() {
        flight = flightService.loadFlight(flight.getFlightId());
    }

    /**
     * Do save flight.
     */
    public void doSaveFlight() {
        flight = this.flightService.saveFlight(flight);
    }

    /**
     * Delete flight.
     */
    public void deleteFlight() throws EMailExceptionSuperCollector {
        flightService.doCancelFlight(flight);
        refreshPage();
    }

    public void refreshPage() {
        LOGGER.info("refreshing Page");
        PrimeFaces.current().ajax().update("flightForm");

        String refreshpage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        ViewHandler handler = FacesContext.getCurrentInstance().getApplication().getViewHandler();
        UIViewRoot root = handler.createView(FacesContext.getCurrentInstance(), refreshpage);
        root.setViewId(refreshpage);
        FacesContext.getCurrentInstance().setViewRoot(root);
    }

    /**
     * Try recreate flight.
     */
    public void tryRecreateFlight() {

        try{
            if(flight.getAircraft() == null) {
                flight = flightService.createFlight(flight.getPassengerCount(), flight.getOriginAirport(), flight.getDestinationAirport(), flight.getDepartureTime(), flight.getArrivalTime());
                flight = flightService.loadFlight(flight.getFlightId());
            }
            else {
                flightService.tryRecreateFlight(flight);
            }

            message(flight);
        } catch (NoSuitableAircraftFoundException e){
            LOGGER.info("Flight from {} to {} at {} cannot be recreated. No suitable aircraft found.", flight.getOriginAirport().getIataCode(), flight.getDestinationAirport().getIataCode(), flight.getDepartureTime());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, WARNING, "Flight cannot be created. No suitable aircraft found."));
        } catch (InsufficientStaffException e){
            LOGGER.info("Flight from {} to {} at {} cannot be recreated. Not enough staff found.", flight.getOriginAirport().getIataCode(), flight.getDestinationAirport().getIataCode(), flight.getDepartureTime());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, WARNING, "Flight cannot be created. Not enough staff found."));
        }
    }

    /**
     * Message.
     *
     * @param flight the flight
     */
    static void message(Flight flight) {
        FacesMessage msg = new FacesMessage("Successful", "Flight created!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "FlightId: " + flight.getFlightId()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Aircraft: " + flight.getAircraft().getAircraftId()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Pilots: " + flight.getPilots().toString()));
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("", "Crew: " + flight.getCrew().toString()));
    }
}

