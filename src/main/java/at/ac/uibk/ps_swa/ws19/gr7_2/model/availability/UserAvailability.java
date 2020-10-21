package at.ac.uibk.ps_swa.ws19.gr7_2.model.availability;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.Airport;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.availabilityKeys.UserAvailabilityId;

/**
 * This class represents the Location where and the time (Date) when a User is available for their next flight.
 *
 * @author Philipp Schießl
 */
@Entity
@IdClass(UserAvailabilityId.class)
public class UserAvailability implements Persistable<UserAvailabilityId>, Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@ManyToOne
	private User user;
	@Id
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	@ManyToOne
	@Nullable
	private Airport location;
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@ManyToOne
	private Flight dependentFlight;


	/**
	 * Instantiates a new User availability.
	 */
	public UserAvailability() {
	}

	/**
	 * Instantiates a new User availability.
	 *
	 * @param user      the user
	 * @param timestamp the timestamp
	 * @param location  the location
	 */
	public UserAvailability(User user, Date timestamp, Airport location) {
		this.user = user;
		this.timestamp = timestamp;
		this.location = location;
	}

	/**
	 * Gets user.
	 *
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * Sets user.
	 *
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
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
		return Objects.hash(location, timestamp, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserAvailability))
			return false;
		UserAvailability other = (UserAvailability) obj;
		return Objects.equals(location, other.location) && Objects.equals(timestamp, other.timestamp)
				&& Objects.equals(user, other.user);
	}

	@Override
	public UserAvailabilityId getId() {
		return new UserAvailabilityId(this.user, this.timestamp);
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
