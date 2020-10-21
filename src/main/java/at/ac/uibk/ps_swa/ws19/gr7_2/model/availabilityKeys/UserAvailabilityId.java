package at.ac.uibk.ps_swa.ws19.gr7_2.model.availabilityKeys;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;

/**
 * This class represents a composite key for a UserAvailability object.
 *
 * @author Philipp Schießl
 */
public class UserAvailabilityId implements Serializable{


	private static final long serialVersionUID = 1L;
	private User user;
	private Date timestamp;
	
	/**
	 * Instantiates a new User availability id.
	 */
	public UserAvailabilityId() {
	}

	/**
	 * @param user
	 * @param timestamp
	 */
	public UserAvailabilityId(User user, Date timestamp) {
		this.user = user;
		this.timestamp = timestamp;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(timestamp, user);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserAvailabilityId))
			return false;
		UserAvailabilityId other = (UserAvailabilityId) obj;
		return Objects.equals(timestamp, other.timestamp) && Objects.equals(user, other.user);
	}

}
