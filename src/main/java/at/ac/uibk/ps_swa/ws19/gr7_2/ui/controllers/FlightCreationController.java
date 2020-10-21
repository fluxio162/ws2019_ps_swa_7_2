package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.*;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.InsufficientStaffException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.NoSuitableAircraftFoundException;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.FlightService;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Controller to create new user.
 *
 * @author Christoph Kugler
 */
@Component
@Scope("view")
public class FlightCreationController {


    @Autowired
    private FlightService flightService;

    @Autowired
    private AirportService airportService;


   private Flight flight = new Flight();

   private Integer requiredSeats;
   private String origin;
   private String destination;
   private Date departure;
   private Date departureTime;
   private Date arrival;
   private Integer passengers;
   private List<String> airportList;
   private Collection<Airport> airportTempList;
   private boolean submitButton = true;

   private final String WARNING = "Warning!";


    /**
     * Gets airport list.
     *
     * @return the airport list
     */
    public List<String> getAirportList() {
        airportList = new ArrayList<String>();
        if((origin != null) && !origin.equals("INN")){
            airportList.add(airportService.loadAirport("INN").getName());
        }
        else{
            airportTempList = airportService.getAllAirports();
            airportTempList.forEach(airport -> airportList.add(airport.getName()));
            if((origin != null) && (origin.equals("INN"))){
                airportList.remove(airportService.loadAirport("INN").getName());
            }
        }
        return airportList;
    }

    /**
     * Sets airport list.
     *
     * @param airportList the airport list
     */
    public void setAirportList(List<String> airportList) {
        this.airportList = airportList;
    }

    /**
     * Gets passengers.
     *
     * @return the passengers
     */
    public Integer getPassengers() {
        return passengers;
    }

    /**
     * Sets passengers.
     *
     * @param passengers the passengers
     */
    public void setPassengers(Integer passengers) {
        this.passengers = passengers;
        setRequiredSeats(passengers);
    }

    /**
     * Gets required seats.
     *
     * @return the required seats
     */
    public Integer getRequiredSeats() {
        return requiredSeats;
    }

    /**
     * Sets required seats.
     *
     * @param requiredSeats the required seats
     */
    public void setRequiredSeats(Integer requiredSeats) {
        this.requiredSeats = requiredSeats;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets origin.
     *
     * @param origin the origin
     */
    public void setOrigin(String origin) {
        origin = airportService.loadAirportByName(origin).getIataCode();

        if(!origin.equals("INN")){
            this.destination = "INN";
        }
        this.origin = origin;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Sets destination.
     *
     * @param destination the destination
     */
    public void setDestination(String destination) {
        if(!origin.equals("INN")){
            this.destination = "INN";
        }
        else{
            this.destination = airportService.loadAirportByName(destination).getIataCode();
        }
    }

    /**
     * Gets departure.
     *
     * @return the departure
     */
    public Date getDeparture() {
        return departure;
    }

    /**
     * Sets departure.
     *
     * @param departure the departure
     */
    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    /**
     * Gets arrival.
     *
     * @return the arrival
     */
    public Date getArrival() {
        return arrival;
    }

    /**
     * Sets arrival.
     *
     * @param arrival the arrival
     */
    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    /**
     * Gets departure time.
     *
     * @return the departure time
     */
    public Date getDepartureTime() {
        return departureTime;
    }

    /**
     * Sets departure time.
     *
     * @param departureTime the departure time
     */
    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
        departure.setHours(departureTime.getHours());
        departure.setMinutes(departureTime.getMinutes());
        if(origin.equals("INN")){
            int flightDuration = airportService.loadAirport(destination).getFlightDuration();
            Date temp = new Date();
            temp.setTime(departure.getTime()+(flightDuration*60000));
            setArrival(temp);
        }
        else{
            int flightDuration = airportService.loadAirport(origin).getFlightDuration();
            Date temp = new Date();
            temp.setTime(departure.getTime()+(flightDuration*60000));
            setArrival(temp);
        }
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
     * Sets flight.
     *
     * @param flight the flight
     */
    public void setFlight(Flight flight) {
        this.flight = flight;
    }

    /**
     * Creates flight.
     */
    public void createFlight() {
        if(!submitButton){
            return;
        }
        if(departure.compareTo(new Date()) < 0){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, WARNING, "Flight cannot be created. Flight is in the past."));
        }
        else{
            submitButton = false;
            Airport originAirport = airportService.loadAirport(origin);
            Airport destinationAirport = airportService.loadAirport(destination);
            try{
                flight = flightService.createFlight(requiredSeats, originAirport, destinationAirport, departure, arrival);
                flight = flightService.loadFlight(flight.getFlightId());
                message(flight);
            } catch (NoSuitableAircraftFoundException e){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, WARNING, "Flight cannot be created. No suitable aircraft found."));
            } catch (InsufficientStaffException e){
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, WARNING, "Flight cannot be created. Not enough staff found."));
            }
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


