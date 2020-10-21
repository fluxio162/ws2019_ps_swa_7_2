package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailCreateExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionSuperCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailSendExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftAvailabilityService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import at.ac.uibk.ps_swa.ws19.gr7_2.ui.beans.SessionInfoBean;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Controller for aircraft deletion.
 */
@Component
@Scope("view")
public class AircraftDeletionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AircraftDetailController.class);

    @Autowired
    private AircraftService aircraftService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private AircraftAvailabilityService aircraftAvailabilityService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private EMailFactory emailFactory;

    @Autowired
    private EMailService emailService;

    /**
     * Attribute to cache the currently displayed aircraft
     */
    private Aircraft aircraft;

    /**
     * Sets the currently displayed aircraft and reloads it form db. This aircraft is
     * targeted by any further calls of
     * {@link #doReloadAircraft)}, and
     * {@link #deleteAircraft()}.
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
     * Action to delete the currently displayed aircraft.
     */
    public void deleteAircraft() throws EMailExceptionSuperCollector {
        EMailExceptionSuperCollector emailException = null;
        try {
            deleteFromAllFlight();
        } catch (EMailExceptionSuperCollector e) {
            emailException = e;
        }
        deleteAvailabilities();
        this.aircraftService.deleteAircraft(aircraft);

        if(emailException != null) throw emailException;
        else refreshPage();
    }

    /**
     * Action to delete the currently displayed aircraft from all upcoming flights.
     */
    public void deleteFromAllFlight() throws EMailExceptionSuperCollector {

        User loggedInUser = sessionInfoBean.getCurrentUser();
        List<EMailExceptionCollector<?>> emailExceptions = new LinkedList<>();
        Collection<Flight> flightList = flightService.findAllPlannedFromAircraft(aircraft);
        for (Flight flight : flightList) {
            flight.removeAircraft(aircraft);
            flight.setComplete(false);
            aircraftAvailabilityService.deleteAvailability(Objects.requireNonNull(flight.getDependentAircraftAvailability()));
            flightService.saveFlight(flight);

            if(loggedInUser != null) {
                List<EMail> emails = null;
                try {
                    emails = emailFactory.createFlightProblemEMail(flight, Collections.singletonList(loggedInUser));
                } catch (EMailCreateExceptionCollector e) {
                    emailExceptions.add(e);
                }
                if(emails != null) {
                    try {
                        emailService.sendEMails(emails);
                    } catch (EMailSendExceptionCollector e) {
                        emailExceptions.add(e);
                    }
                }
            }
        }

        if(!emailExceptions.isEmpty()) {
            throw new EMailExceptionSuperCollector(emailExceptions);
        }
    }

    public void refreshPage() {
        LOGGER.info("refreshing Page");
        PrimeFaces.current().ajax().update("aircraftForm");

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
        for (AircraftAvailability aa : aircraftAvailabilityService.getAllFromAircraft(aircraft)){
            aircraftAvailabilityService.deleteAvailability(aa);
        }
    }
}
