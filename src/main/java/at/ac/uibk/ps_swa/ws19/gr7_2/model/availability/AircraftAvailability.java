package at.ac.uibk.ps_swa.ws19.gr7_2.model.availability;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availabilityKeys.AircraftAvailabilityId;

/**
 * This class represents the Location where and the time (Date) when an Aircraft is available for its next flight.
 *
 * @author Philipp Schießl
 */
@Entity
@IdClass(AircraftAvailabilityId.class)
public class AircraftAvailability implements  Persistable<AircraftAvailabilityId> ,Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@ManyToOne
	private Aircraft aircraft;
	@Id
    @Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	@ManyToOne
	@Nullable
	private Airport location;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@OneToOne
	private Flight dependentFlight;

	/**
	 * Instantiates a new Aircraft availability.
	 */
	public AircraftAvailability() {
	}

	/**
	 * Instantiates a new Aircraft availability.
	 *
	 * @param aircraft  the aircraft
	 * @param timestamp the timestamp
	 * @param location  the location
	 */
	public AircraftAvailability(Aircraft aircraft, Date timestamp, Airport location) {
		this.aircraft = aircraft;
		this.timestamp = timestamp;
		this.location = location;
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
	 * Sets aircraft.
	 *
	 * @param aircraft the aircraft to set
	 */
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	/**
	 * Gets timestamp.
	 *
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * Sets timestamp.
	 *
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets location.
	 *
	 * @return the location
	 */
	public Airport getLocation() {
		return location;
	}

	/**
	 * Sets location.
	 *
	 * @param location the location to set
	 */
	public void setLocation(Airport location) {
		this.location = location;
	}

	/**
	 * Gets serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets create date.
	 *
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Sets create date.
	 *
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets dependent flight.
	 *
	 * @return the dependentFlight
	 */
	public Flight getDependentFlight() {
		return dependentFlight;
	}

	/**
	 * Sets dependent flight.
	 *
	 * @param dependentFlight the dependentFlight to set
	 */
	public void setDependentFlight(Flight dependentFlight) {
		this.dependentFlight = dependentFlight;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aircraft, location, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AircraftAvailability))
			return false;
		AircraftAvailability other = (AircraftAvailability) obj;
		return Objects.equals(aircraft, other.aircraft) && Objects.equals(location, other.location)
				&& Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public AircraftAvailabilityId getId() {
		return new AircraftAvailabilityId(this.aircraft, this.timestamp);
	}

	@Override
	public boolean isNew() {
		return this.createDate == null;
	}

	/**
	 * Has dependent flight boolean.
	 *
	 * @return the boolean
	 */
	public boolean hasDependentFlight() {
		return dependentFlight != null;
	}

}
