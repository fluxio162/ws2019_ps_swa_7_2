package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.data.domain.Persistable;

/**
 * Entity representing airports.
 *
 * @author Benedikt Schenk
 */
@Entity
public class Airport implements Persistable<String>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String iataCode;
	private String name;
	private String country;
	private int flightDuration;

	@ManyToOne(optional = false)
	private User createUser;
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	@ManyToOne(optional = true)
	private User updateUser;
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	private boolean enabled;

	@Temporal(TemporalType.TIMESTAMP)
	private Date deleteDate;
	@ManyToOne(optional = true)
	private User deleteUser;

	/**
	 * Gets flight duration.
	 *
	 * @return the flight duration
	 */
	public int getFlightDuration() {
		return flightDuration;
	}

	/**
	 * Sets flight duration.
	 *
	 * @param flightDuration the flight duration
	 */
	public void setFlightDuration(int flightDuration) {
		this.flightDuration = flightDuration;
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
	 * Is enabled boolean.
	 *
	 * @return the boolean
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets enabled.
	 *
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets delete date.
	 *
	 * @return the delete date
	 */
	public Date getDeleteDate() { return deleteDate; }

	/**
	 * Sets delete date.
	 *
	 * @param deleteDate the delete date
	 */
	public void setDeleteDate(Date deleteDate) { this.deleteDate = deleteDate; }

	/**
	 * Gets delete user.
	 *
	 * @return the delete user
	 */
	public User getDeleteUser() { return deleteUser; }

	/**
	 * Sets delete user.
	 *
	 * @param deleteUser the delete user
	 */
	public void setDeleteUser(User deleteUser) { this.deleteUser = deleteUser; }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 3;
		result = prime * result + ((iataCode == null) ? 0 : iataCode.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Airport other = (Airport) obj;
		if (iataCode == null) {
			if (other.iataCode != null)
				return false;
		} else if (!iataCode.equals(other.iataCode)){
			return false;
		}
		return true;
	}


	@Override
	public String getId() {
		return getIataCode();
	}

	@Override
	public boolean isNew() {
		return (null == createDate);
	}
}
