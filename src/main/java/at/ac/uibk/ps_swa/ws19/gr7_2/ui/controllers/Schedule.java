package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;


import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Vacation;
import at.ac.uibk.ps_swa.ws19.gr7_2.repositories.FlightRepository;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.VacationService;
import at.ac.uibk.ps_swa.ws19.gr7_2.ui.beans.SessionInfoBean;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * The type Schedule.
 *
 * @author Christoph Wittauer
 */
@Component
@Scope("view")
public class Schedule {

    @Autowired
    private FlightService flightService;

    @Autowired
    private VacationService vacationService;

    @Autowired
    private SessionInfoBean sessionInfoBean;


    private ScheduleModel eventModel;

    /**
     * Gets event model.
     *
     * @return the event model
     */
    public ScheduleModel getEventModel() {
        return eventModel;
    }

    /**
     * Sets event model.
     *
     * @param eventModel the event model
     */
    public void setEventModel(ScheduleModel eventModel) {
        this.eventModel = eventModel;
    }

    @PostConstruct
    private void init() {
        eventModel = new DefaultScheduleModel();

        if (sessionInfoBean.getCurrentUserRoles().contains("ADMIN") || sessionInfoBean.getCurrentUserRoles().contains("MANAGER")){
            Collection<Flight> flightList = flightService.getAllFlights();
            Collection<Vacation> vacationList = vacationService.getAllVacation();

            addFlightEvents(flightList);
            addVacationEvents(vacationList);
        }

        if(sessionInfoBean.getCurrentUserRoles().contains("CREW")){
            Collection<Flight> flightList = flightService.getAllFromCrew(sessionInfoBean.getCurrentUser());
            Collection<Vacation> vacationList = vacationService.getAllFromUser(sessionInfoBean.getCurrentUser());

            addFlightEvents(flightList);
            addVacationEvents(vacationList);
        }

        if(sessionInfoBean.getCurrentUserRoles().contains("PILOT")){
            Collection<Flight> flightList = flightService.getAllFromPilot(sessionInfoBean.getCurrentUser());
            Collection<Vacation> vacationList = vacationService.getAllFromUser(sessionInfoBean.getCurrentUser());

            addFlightEvents(flightList);
            addVacationEvents(vacationList);
        }

        if(sessionInfoBean.getCurrentUserRoles().contains("USER")){
            Collection<Vacation> vacationList = vacationService.getAllFromUser(sessionInfoBean.getCurrentUser());

            addVacationEvents(vacationList);
        }

    }

    private void addFlightEvents(Collection<Flight> flightList) {

        for (Flight f : flightList) {
             StringBuilder stringBuilder = new StringBuilder();

            DefaultScheduleEvent event = new DefaultScheduleEvent();
            event.setTitle(f.getOriginAirport().getIataCode() + " - " + f.getDestinationAirport().getIataCode());
            stringBuilder.append("Flight: " + f.getFlightId() + "\n" + "From: " + f.getOriginAirport().getName() + "\n"
                    + "To: " + f.getDestinationAirport().getName() + "\n" + "Departure: "
                    + f.getDepartureTime()+"\nPilots: ");

            for (User pilot: f.getPilots()) {
                stringBuilder.append(pilot.getFirstName()+" "+pilot.getLastName()+", ");
            }
            stringBuilder.append("\nCrew: ");
            for(User crew: f.getCrew()){
                stringBuilder.append(crew.getFirstName()+" "+crew.getLastName()+", ");
            }

            event.setDescription(stringBuilder.toString());
            event.setStartDate(f.getDepartureTime());
            event.setEndDate(f.getArrivalTime());
            eventModel.addEvent(event);
        }
    }

    private void addVacationEvents(Collection<Vacation> vacations){
        for(Vacation v : vacations){
            DefaultScheduleEvent event = new DefaultScheduleEvent();
            event.setTitle(v.getCreateUser().getLastName()+" Vacation");
            event.setDescription("By: "+v.getCreateUser().getFirstName()+" "+v.getCreateUser().getLastName()+
                    "\nFrom: "+v.getVacationStart().toString()+"\nTo: "+v.getVacationEnd().toString());
            event.setStartDate(v.getVacationStart());
            event.setEndDate(v.getVacationEnd());
            eventModel.addEvent(event);
        }
    }
}
