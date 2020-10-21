package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.AircraftType;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Random;

/**
 * Controller to create new aircraft.
 *
 * @author Christoph Kugler
 */
@Component
@Scope("view")
public class AircraftCreationController {

    @Autowired
    private AircraftService aircraftService;

    private Aircraft aircraft = new Aircraft();

    @Enumerated(EnumType.STRING)
    private AircraftType type;

    private boolean disableSaveButton = true;

    private Integer aircraftId;
    private Integer seats;
    private Integer requiredPilots;
    private Integer requiredCrew;


    /**
     * Gets type.
     *
     * @return the type
     */
    public AircraftType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(AircraftType type) {
        this.type = type;
    }

    /**
     * Is disable save button boolean.
     *
     * @return the boolean
     */
    public boolean isDisableSaveButton() {
        return disableSaveButton;
    }

    /**
     * Sets disable save button.
     *
     * @param disableSaveButton the disable save button
     */
    public void setDisableSaveButton(boolean disableSaveButton) {
        this.disableSaveButton = disableSaveButton;
    }

    /**
     * Sets aircraft.
     *
     * @param aircraft the aircraft
     */
    public void setAircraft(Aircraft aircraft) {
        this.aircraft = aircraft;
    }

    /**
     * Gets aircraft id.
     *
     * @return the aircraft id
     */
    public Integer getAircraftId() {
        return aircraftId;
    }

    /**
     * Sets aircraft id.
     *
     * @param aircraftId the aircraft id
     */
    public void setAircraftId(Integer aircraftId) {
        this.aircraftId = aircraftId;
    }

    /**
     * Gets seats.
     *
     * @return the seats
     */
    public Integer getSeats() {
        return seats;
    }

    /**
     * Sets seats.
     *
     * @param seats the seats
     */
    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    /**
     * Gets required pilots.
     *
     * @return the required pilots
     */
    public Integer getRequiredPilots() {
        return requiredPilots;
    }

    /**
     * Sets required pilots.
     *
     * @param requiredPilots the required pilots
     */
    public void setRequiredPilots(Integer requiredPilots) {
        this.requiredPilots = requiredPilots;
    }

    /**
     * Gets required crew.
     *
     * @return the required crew
     */
    public Integer getRequiredCrew() {
        return requiredCrew;
    }

    /**
     * Sets required crew.
     *
     * @param requiredCrew the required crew
     */
    public void setRequiredCrew(Integer requiredCrew) {
        this.requiredCrew = requiredCrew;
    }

    /**
     * Gets aircraft.
     *
     * @return the aircraft
     */
    public Aircraft getAircraft() {
        return aircraft;
    }


    /**
     * Saves the new aircraft into the Aircraft Repository.
     */
    public void doSaveAircraft() {

        if (aircraftService.getAllAircraft().contains(this.aircraft)){
            return;
        }

        aircraft.setType(type);

        if(seats == null){
            aircraft.setSeats(0);
        }
        else{
            aircraft.setSeats(seats);
        }

        if(requiredPilots == null){
            aircraft.setRequiredPilots(0);
        }
        else{
            aircraft.setRequiredPilots(requiredPilots);
        }

        if(requiredCrew == null){
            aircraft.setRequiredCrew(0);
        }
        else{
            aircraft.setRequiredCrew(requiredCrew);
        }

        aircraft.setEnabled(true);
        aircraft = this.aircraftService.saveAircraft(aircraft);
    }

    /**
     * Checks if the given AircraftId is already in Aircraft Repository.
     *
     * @return the boolean
     */
    public boolean validateAircraftId(){

        if(aircraftId == null){
            warn("Aircraft-ID is too short.");
            disableSaveButton = true;
            return false;
        }

        aircraft.setAircraftId(aircraftId);

        if (aircraftService.getAllAircraft().contains(aircraft)){
            warn("Aircraft-ID already in use.");
            disableSaveButton = true;
            return false;
        }

        return true;

    }

    /**
     * Enables the Save Button on aircraft_creation.xhtml by using validateAircraftId() and checking if a AircraftType is selected.
     */
    public void validateAircraft(){
        if (type == null){
            warn("Select a type.");
            disableSaveButton = true;
            return;
        }

        if (!validateAircraftId()){
            disableSaveButton = true;
            return;
        }

        disableSaveButton = false;
    }


    /**
     * Method to send Error Message for invalid aircraftId.
     *
     * */

    private void warn(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", message));
    }

    /**
     * Generates a unique 6 digit long aircraftId.
     *
     * @return the int
     */
    public int generateAircraftId() {
        Random r = new Random();
        int low = 100000;
        int high = 999999;
        aircraftId = r.nextInt(high-low) + low;

        aircraft.setAircraftId(aircraftId);

        if (aircraftService.getAllAircraft().contains(aircraft)){
            disableSaveButton = true;
            generateAircraftId();
        }

        return aircraftId;
    }

}
