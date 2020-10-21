package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Random;

/**
 * Controller to create new airport.
 *
 * @author Christoph Kugler
 */
@Component
@Scope("view")
public class AirportCreationController {

    @Autowired
    private AirportService airportService;

    private Airport airport = new Airport();

    private boolean disableSaveButton = true;

    private String iataCode;
    private String name;
    private String country;

    /**
     * Gets airport.
     *
     * @return the airport
     */
    public Airport getAirport() {
        return airport;
    }

    /**
     * Sets airport.
     *
     * @param airport the airport
     */
    public void setAirport(Airport airport) {
        this.airport = airport;
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
     * Gets iata code.
     *
     * @return the iata code
     */
    public String getIataCode() {
        return iataCode;
    }

    /**
     * Sets iata code.
     *
     * @param iataCode the iata code
     */
    public void setIataCode(String iataCode) {
        this.iataCode = iataCode;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Saves the new airport into the Airport Repository.
     */
    public void doSaveAirport() {

        if (airportService.getAllAirports().contains(this.airport)){
            return;
        }

        airport.setName(name);
        airport.setCountry(country);
        airport.setEnabled(true);
        airport = this.airportService.saveAirport(airport);
    }

    /**
     * Checks if the given IATA Code is already in Airport Repository and enables Save Button.
     *
     * @return the boolean
     */
    public boolean validateIataCode(){

        if(iataCode == null){
            warn("IATA Code is too short.");
            disableSaveButton = true;
            return false;
        }

        airport.setIataCode(iataCode);

        if (airportService.getAllAirports().contains(airport)){
            warn("IATA Code already in use.");
            disableSaveButton = true;
            return false;
        }


        disableSaveButton = false;
        return true;

    }


    /**
     * Method to send Error Message for invalid IATA Code.
     *
     * */

    private void warn(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", message));
    }
}
