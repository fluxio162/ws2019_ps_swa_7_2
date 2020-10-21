package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;



/**
 * Controller to create new vacation.
 *
 * @author Christoph Wittauer
 */


import at.ac.uibk.ps_swa.ws19.gr7_2.model.*;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.VacationService;
import at.ac.uibk.ps_swa.ws19.gr7_2.ui.beans.SessionInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.*;

/**
 * Controller to create new vacations.
 * @author Christoph Wittauer
 */
@Component
@Scope("view")
public class VacationCreationController {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private SessionInfoBean sessionInfoBean;

    private Vacation vacation = new Vacation();
    private int vacationId = 0;
    private List<Date> range;
    private int daysTaken;
    private Date startVacation;
    private Date endVacation;
    private final String FLIGHT_ALREADY = "There is already a flight!";
    private final String VACATION_ALREADY = "There is already a vacation booked!";
    private Calendar currentCalendar = Calendar.getInstance();

    /**
     * Gets range.
     *
     * @return the range
     */
    public List<Date> getRange() {
        return range;
    }

    /**
     * Sets range.
     *
     * @param range the range
     */
    public void setRange(List<Date> range) {
        this.range = range;
    }

    /**
     * Gets vacation service.
     *
     * @return the vacation service
     */
    public VacationService getVacationService() {
        return vacationService;
    }

    /**
     * Sets vacation service.
     *
     * @param vacationService the vacation service
     */
    public void setVacationService(VacationService vacationService) {
        this.vacationService = vacationService;
    }

    /**
     * Gets vacation.
     *
     * @return the vacation
     */
    public Vacation getVacation() {
        return vacation;
    }

    /**
     * Sets vacation.
     *
     * @param vacation the vacation
     */
    public void setVacation(Vacation vacation) {
        this.vacation = vacation;
    }

    /**
     * Gets vacation id.
     *
     * @return the vacation id
     */
    public int getVacationId() {
        return vacationId;
    }

    /**
     * Sets vacation id.
     *
     * @param vacationId the vacation id
     */
    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }

    /**
     * Gets days taken.
     *
     * @return the days taken
     */
    public int getDaysTaken() {return daysTaken;}

    /**
     * Sets days taken.
     *
     * @param daysTaken the days taken
     */
    public void setDaysTaken(int daysTaken) {this.daysTaken = daysTaken;}

    /**
     * Gets start vacation.
     *
     * @return the start vacation
     */
    public Date getStartVacation() {return startVacation; }

    /**
     * Gets end vacation.
     *
     * @return the end vacation
     */
    public Date getEndVacation() {return endVacation;}

    /**
     * Sets end vacation.
     *
     * @param endVacation the end vacation
     */
    public void setEndVacation(Date endVacation) {this.endVacation = endVacation;}

    /**
     * Sets start vacation.
     *
     * @param startVacation the start vacation
     */
    public void setStartVacation(Date startVacation) {this.startVacation = startVacation;}


    /**
     * Save vacation boolean.
     *
     * @return the boolean
     */
    public boolean saveVacation() {
        FacesContext context = FacesContext.getCurrentInstance();

        if (generateVacationId() == 0) {
            this.vacationId = generateVacationId();
        }

        if(range == null){
            error("Vacation not given!");
            return false;
        }
        else {
            this.startVacation = this.range.get(0);
            this.endVacation = this.range.get(range.size()-1);
            this.vacation.setVacationStart(this.range.get(0));
            this.vacation.setVacationEnd(this.range.get(range.size()-1));
            this.currentCalendar.setTime(this.startVacation);
        }

        if(this.daysTaken == 0){
            long differenceTime = vacation.getVacationEnd().getTime()-vacation.getVacationStart().getTime();
            this.daysTaken += (differenceTime/(1000*3600*24));
            this.vacation.setDaysTaken(this.daysTaken);
        }

        if(this.endVacation.equals(this.startVacation)){
            error("Same date not allowed!");
            return false;
        }

        if (validateVacation() && checkFiveWeeks()) {
            context.addMessage(null, new FacesMessage("Successful", "Your vacation is now in your schedule!"));
            vacation = this.vacationService.saveVacation(vacation);
            return true;
        }
        else {
            error("Something went wrong!\n Try again!");
            return false;
        }
    }


    /**
     * checks if the user has vacation left
     * user can take maximum 5 weeks of vacation
     *
     * @return the boolean
     */
    public boolean checkFiveWeeks(){
        Collection<Vacation> vacations = vacationService.getAllFromUser(sessionInfoBean.getCurrentUser());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        Calendar vacationCalendar = Calendar.getInstance();

        int currentDays = 0;
        int weeksAllowed = 35;

        currentDays += this.daysTaken;

        if (currentDays >= weeksAllowed) {
            error("No vacation left!");
            return false;
        }

        for (Vacation v : vacations) {
            vacationCalendar.setTime(v.getVacationStart());
            if (vacationCalendar.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                currentDays += v.getDaysTaken();
            }
        }

        if (currentDays >= weeksAllowed) {
            error("No vacation left!");
            return false;
        }

        info("Vacation left: "+(weeksAllowed-currentDays)+"day/s  in "+ currentCalendar.get(Calendar.YEAR));
        this.daysTaken = 0;
        return true;
    }


    /**
     * Validate vacation
     * checks if the vacation is in the past
     * checks if there is already a flight
     * checks if there is already a vacation booked
     *
     * @return the boolean
     */
    public boolean validateVacation(){

        User user = this.sessionInfoBean.getCurrentUser();
        Collection<Vacation> vacations = vacationService.getAllFromUser(user);

        if(this.vacation.getVacationStart().compareTo(new Date()) < 0){
            error("Vacation is in the past");
            return false;
        }

        if (checkPilotForFlights(user)) return false;

        if (checkCrewForFlights(user)) return false;


        if (checkForVacationOverlap(vacations)) return false;
        return true;
    }

    private boolean checkPilotForFlights(User user) {
        if(this.sessionInfoBean.getCurrentUserRoles().contains("PILOT")) {
            for (Date date : this.range) {
                for (Flight flight : this.flightService.getAllFromPilot(user)) {
                    if(date.equals(flight.getDepartureTime()) ||date.equals(flight.getArrivalTime())){
                        error(FLIGHT_ALREADY);
                        return true;
                    }
                    if (checkForFlight(flight)) return true;
                }
            }
        }
        return false;
    }

    private boolean checkCrewForFlights(User user) {
        if(this.sessionInfoBean.getCurrentUserRoles().contains("CREW")) {

                for (Flight flight : this.flightService.getAllFromCrew(user)) {
                    if(this.startVacation.equals(flight.getDepartureTime()) || this.startVacation.equals(flight.getArrivalTime()) || this.endVacation.equals(flight.getDepartureTime()) || this.endVacation.equals(flight.getArrivalTime())){
                        error(FLIGHT_ALREADY);
                        return true;
                    }
                    if (checkForFlight(flight)) return true;
                }
        }
        return false;
    }

    private boolean checkForVacationOverlap(Collection<Vacation> vacations) {
        for (Vacation v: vacations) {
                if (this.startVacation.equals(v.getVacationStart()) || this.endVacation.equals(v.getVacationEnd()) || this.startVacation.equals(v.getVacationEnd()) || this.endVacation.equals(v.getVacationStart())) {
                    error(VACATION_ALREADY);
                    return true;
                }
                if (this.startVacation.compareTo(v.getVacationStart()) > 0 && this.startVacation.compareTo(v.getVacationEnd()) < 0 || this.endVacation.compareTo(v.getVacationEnd()) > 0 && this.endVacation.compareTo(v.getVacationStart()) < 0) {
                    error(VACATION_ALREADY);
                    return true;
                }

                if(this.startVacation.compareTo(v.getVacationStart()) < 0 && this.startVacation.compareTo(v.getVacationEnd()) < 0 && this.endVacation.compareTo(v.getVacationStart()) > 0 && this.endVacation.compareTo(v.getVacationEnd()) > 0){
                    error(VACATION_ALREADY);
                    return true;
                }
        }
        return false;
    }

    private boolean checkForFlight(Flight flight) {
        if (this.startVacation.compareTo(flight.getDepartureTime()) >= 0 && this.startVacation.compareTo(flight.getArrivalTime()) <= 0 || this.endVacation.compareTo(flight.getArrivalTime()) >= 0 && this.endVacation.compareTo(flight.getDepartureTime()) <= 0) {
            error(FLIGHT_ALREADY);
            return true;
        }
        if (this.startVacation.compareTo(flight.getDepartureTime()) <= 0 && this.startVacation.compareTo(flight.getArrivalTime()) <= 0 && this.endVacation.compareTo(flight.getDepartureTime()) >= 0 && this.endVacation.compareTo(flight.getArrivalTime()) >= 0){
            error(FLIGHT_ALREADY);
            return true;
        }
        return false;
    }

    /**
     * Generate vacation id int.
     *
     * @return the int
     */
    public int generateVacationId(){
        Random r = new Random();
        int low = 100000;
        int high = 999999;

        vacationId = r.nextInt(high-low) + low;
        vacation.setVacationId(vacationId);

        if(vacationService.getAllVacation().contains(vacation)){
            vacationId = generateVacationId();
        }

        return vacationId;
    }

    /**
     * Shows an error message
     *
     * @param message the message
     */
    public void error(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error!", message));
    }

    /**
     * Shows an info message
     *
     * @param message the message
     */
    public void info(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", message));
    }
}
