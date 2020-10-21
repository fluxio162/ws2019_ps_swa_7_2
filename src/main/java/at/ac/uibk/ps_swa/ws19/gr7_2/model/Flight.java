package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.AircraftAvailability;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availability.UserAvailability;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.stream.Stream;

/**
 * The type Flight.
 */
@Entity
public class Flight implements Persistable<Long>, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long flightId;
	@ManyToOne
	private Airport originAirport;
	@ManyToOne
	private Airport destinationAirport;
	private Date departureTime;
	private Date arrivalTime;
	@ManyToOne
	private Aircraft aircraft;
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<User> pilots;
	@ManyToMany(fetch=FetchType.EAGER)
	private Set<User> crew;
	private int passengerCount;
	private boolean complete;
	private boolean cancelled;
	@ManyToOne(optional = false)
	private User createUser;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@ManyToOne(optional = true)
	private User updateUser;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	@ElementCollection(targetClass = UserAvailability.class, fetch = FetchType.EAGER)
	@CollectionTable(name = "Flight_DependentUserAvailabilities")
	private Set<UserAvailability> dependentUserAvailabilities;
	@ManyToOne
	@Nullable
	private AircraftAvailability dependentAircraftAvailability;
	@Temporal(TemporalType.TIMESTAMP)
	private Date deleteDate;
	@ManyToOne(optional = true)
	private User deleteUser;

	/**
	 * Gets flight id.
	 *
	 * @return the flight id
	 */
	public long getFlightId() {
		return flightId;
	}

	/**
	 * Sets flight id.
	 *
	 * @param flightId the flight id
	 */
	public void setFlightId(long flightId) {
		this.flightId = flightId;
	}

	/**
	 * Is cancelled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isCancelled() { return cancelled; }

	/**
	 * Sets cancelled.
	 *
	 * @param canceled the canceled
	 */
	public void setCancelled(boolean canceled) { this.cancelled = canceled; }

	/**
	 * Sets flight id.
	 *
	 * @param flightId the flight id
	 */
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}

	/**
	 * Gets origin airport.
	 *
	 * @return the origin airport
	 */
	public Airport getOriginAirport() {
		return originAirport;
	}

	/**
	 * Sets origin airport.
	 *
	 * @param originAirport the origin airport
	 */
	public void setOriginAirport(Airport originAirport) {
		this.originAirport = originAirport;
	}

	/**
	 * Gets destination airport.
	 *
	 * @return the destination airport
	 */
	public Airport getDestinationAirport() {
		return destinationAirport;
	}

	/**
	 * Sets destination airport.
	 *
	 * @param destinationAirport the destination airport
	 */
	public void setDestinationAirport(Airport destinationAirport) {
		this.destinationAirport = destinationAirport;
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
	}

	/**
	 * Gets arrival time.
	 *
	 * @return the arrival time
	 */
	public Date getArrivalTime() {
		return arrivalTime;
	}

	/**
	 * Sets arrival time.
	 *
	 * @param arrivalTime the arrival time
	 */
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
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
	 * @param aircraft the aircraft
	 */
	public void setAircraft(Aircraft aircraft) {
		this.aircraft = aircraft;
	}

	/**
	 * Gets pilots.
	 *
	 * @return the pilots
	 */
	public Set<User> getPilots() {
		return pilots;
	}

	/**
	 * Sets pilots.
	 *
	 * @param pilots the pilots
	 */
	public void setPilots(Set<User> pilots) {
		this.pilots = pilots;
	}

	/**
	 * Gets crew.
	 *
	 * @return the crew
	 */
	public Set<User> getCrew() {
		return crew;
	}

	/**
	 * Sets crew.
	 *
	 * @param crew the crew
	 */
	public void setCrew(Set<User> crew) {
		this.crew = crew;
	}

	/**
	 * Gets passenger count.
	 *
	 * @return the passenger count
	 */
	public int getPassengerCount() {
		return passengerCount;
	}

	/**
	 * Sets passenger count.
	 *
	 * @param passengerCount the passenger count
	 */
	public void setPassengerCount(int passengerCount) {
		this.passengerCount = passengerCount;
	}

	/**
	 * Gets create user.
	 *
	 * @return the create user
	 */
	public User getCreateUser() {
		return createUser;
	}

	/**
	 * Sets create user.
	 *
	 * @param createUser the create user
	 */
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	/**
	 * Gets create date.
	 *
	 * @return the create date
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Sets create date.
	 *
	 * @param createDate the create date
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets update user.
	 *
	 * @return the update user
	 */
	public User getUpdateUser() {
		return updateUser;
	}

	/**
	 * Sets update user.
	 *
	 * @param updateUser the update user
	 */
	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * Gets update date.
	 *
	 * @return the update date
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Sets update date.
	 *
	 * @param updateDate the update date
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Gets delete date.
	 *
	 * @return the delete date
	 */
	public Date getDeleteDate() {
		return deleteDate;
	}

	/**
	 * Sets delete date.
	 *
	 * @param deleteDate the delete date
	 */
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	/**
	 * Gets delete user.
	 *
	 * @return the delete user
	 */
	public User getDeleteUser() {
		return deleteUser;
	}

	/**
	 * Sets delete user.
	 *
	 * @param deleteUser the delete user
	 */
	public void setDeleteUser(User deleteUser) {
		this.deleteUser = deleteUser;
	}


	/**
	 * Remove user.
	 *
	 * @param user the user
	 */
	public void removeUser(User user){
		pilots.remove(user);
		crew.remove(user);
	}

	/**
	 * Remove aircraft.
	 *
	 * @param aircraft the aircraft
	 */
	public void removeAircraft(Aircraft aircraft){
		this.aircraft = null;
	}

	/**
	 * Remove all user and aircraft.
	 */
	public void removeAllUserAndAircraft(){
		pilots.clear();
		crew.clear();
		this.aircraft = null;
	}

	@Override
	public Long getId() {
		return flightId;
	}

	@Override
	public boolean isNew() {
		return false;
	}

    /**
     * This method never returns null. If there are no crew members and no pilots, it returns an empty stream.
     *
     * @return a lazy concatenated Stream which contains all pilots and all crew members of this flight
     */
	public Stream<User> getCrewAndPilots() {
	    if(this.pilots != null && this.crew != null) return Stream.concat(this.pilots.stream(), this.crew.stream());
	    if(this.pilots != null) return this.pilots.stream();
	    if(this.crew != null) return crew.stream();
	    return Stream.<User>builder().build(); // empty stream
    }

    /**
     *
     * @return the number of flight personnel on this flight (amount of crew and pilots)
     */
    public int getFlightPersonnelAmount() {
        return (this.pilots != null ? this.pilots.size() : 0) + (this.crew != null ? this.crew.size() : 0);
    }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Flight flight = (Flight) o;
		return flightId == flight.flightId;
	}

	@Override
	public int hashCode() {
		final int prime = 13;
		int result = 5;
		result = (prime * result) + Long.valueOf(flightId).hashCode();
		return result;
	}

	/**
	 * Is complete boolean.
	 *
	 * @return whether the flight is complete
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * Sets complete.
	 *
	 * @param complete whether the flight is complete
	 */
	public void setComplete(boolean complete) {
		this.complete = complete;
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
	 * Gets dependent user availabilities.
	 *
	 * @return the dependentUserAvailabilities
	 */
	public Set<UserAvailability> getDependentUserAvailabilities() {
		return dependentUserAvailabilities;
	}

	/**
	 * Sets dependent user availabilities.
	 *
	 * @param dependentUserAvailabilities the dependentUserAvailabilities to set
	 */
	public void setDependentUserAvailabilities(Set<UserAvailability> dependentUserAvailabilities) {
		this.dependentUserAvailabilities = dependentUserAvailabilities;
	}

	/**
	 * Gets dependent aircraft availability.
	 *
	 * @return the dependentAircraftAvailability
	 */
	public AircraftAvailability getDependentAircraftAvailability() {
		return dependentAircraftAvailability;
	}

	/**
	 * Sets dependent aircraft availability.
	 *
	 * @param dependentAircraftAvailability the dependentAircraftAvailability to set
	 */
	public void setDependentAircraftAvailability(AircraftAvailability dependentAircraftAvailability) {
		this.dependentAircraftAvailability = dependentAircraftAvailability;
	}
}
