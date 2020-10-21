package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Vacation;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailCreateExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailExceptionSuperCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailSendExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.*;
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
 * Controller for the user detail view.
 */
@Component
@Scope("view")
public class UserDeletionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDeletionController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private UserAvailabilityService userAvailabilityService;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    @Autowired
    private EMailFactory emailFactory;

    @Autowired
    private EMailService emailService;

    /**
     * Attribute to cache the currently displayed user
     */
    private User user;

    /**
     * Sets the currently displayed user and reloads it form db. This user is
     * targeted by any further calls of
     * {@link #doReloadUser()}, and
     * {@link #doDeleteUser()}.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
        doReloadUser();
    }

    /**
     * Returns the currently displayed user.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Action to force a reload of the currently displayed user.
     */
    public void doReloadUser() {
        user = userService.loadUser(user.getUsername());
    }


    /**
     * Action to delete the currently displayed user.
     */
    public void doDeleteUser() throws EMailExceptionSuperCollector {
        EMailExceptionSuperCollector emailException = null;
        try {
            deleteFromAllFlight();
        } catch (EMailExceptionSuperCollector e) {
            emailException = e;
        }
        deleteVacation();
        deleteAvailabilities();
        this.userService.deleteUser(user);

        if(emailException != null) throw emailException;
    }

    /**
     * Action to delete the currently displayed user from all upcoming flights.
     */
    public void deleteFromAllFlight() throws EMailExceptionSuperCollector {

        User loggedInUser = sessionInfoBean.getCurrentUser();
        List<EMailExceptionCollector<?>> exceptions = new LinkedList<>();
        Collection<Flight> flightList = flightService.findAllPlannedFromUser(user);
        for (Flight flight : flightList) {
            flight.removeUser(user);
            flight.setComplete(false);
            flight.getDependentUserAvailabilities().removeIf(ua -> ua.getUser() == user);
            flightService.saveFlight(flight);

            // Send email to manager
            if(loggedInUser != null) {
                List<EMail> emails = null;
                try {
                    emails = emailFactory.createFlightProblemEMail(flight, Collections.singletonList(loggedInUser));
                } catch (EMailCreateExceptionCollector e) {
                    exceptions.add(e);
                }
                try {
                    if(emails != null) emailService.sendEMails(emails);
                } catch (EMailSendExceptionCollector e) {
                    exceptions.add(e);
                }
            }
        }

        // If any exceptions occurred ...
        if(!exceptions.isEmpty()) {
            // ... Throw one single exception that encapsulates all of them
            throw new EMailExceptionSuperCollector(exceptions);
        }
        else refreshPage();
    }

    public void refreshPage() {
        LOGGER.info("refreshing Page");
        PrimeFaces.current().ajax().update("userForm");

        String refreshpage = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        ViewHandler handler = FacesContext.getCurrentInstance().getApplication().getViewHandler();
        UIViewRoot root = handler.createView(FacesContext.getCurrentInstance(), refreshpage);
        root.setViewId(refreshpage);
        FacesContext.getCurrentInstance().setViewRoot(root);
    }

    /**
     * Delete vacation.
     */
    public void deleteVacation(){
        for(Vacation vacation : vacationService.getFutureFromUser(user)){
            vacationService.deleteVacation(vacation);
        }
    }

    /**
     * Delete availabilities.
     */
    public void deleteAvailabilities(){
        for (UserAvailability ua : userAvailabilityService.getAllFromUser(user)){
            userAvailabilityService.deleteAvailability(ua);
        }
    }
}
