package at.ac.uibk.ps_swa.ws19.gr7_2.model.availabilityKeys;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft;

/**
 * This class represents a composite key for an AircraftAvailability object.
 *
 * @author Philipp Schießl
 */
public class AircraftAvailabilityId implements Serializable {

	private static final long serialVersionUID = 1L;
	private Aircraft aircraft;
	private Date timestamp;


	/**
	 * Instantiates a new Aircraft availability id.
	 */
	public AircraftAvailabilityId() {
	}

	/**
	 * Instantiates a new Aircraft availability id.
	 *
	 * @param aircraft  the aircraft
	 * @param timestamp the timestamp
	 */
	public AircraftAvailabilityId(Aircraft aircraft, Date timestamp) {
		this.aircraft = aircraft;
		this.timestamp = timestamp;
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
	 * Gets serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(aircraft, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof AircraftAvailabilityId))
			return false;
		AircraftAvailabilityId other = (AircraftAvailabilityId) obj;
		return Objects.equals(aircraft, other.aircraft) && Objects.equals(timestamp, other.timestamp);
	}

}