package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

import org.springframework.data.domain.Persistable;

/**
 * Entity representing Aircraft.
 *
 * @author Christoph Kugler
 */
@Entity
public class Aircraft implements Persistable<Integer>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int aircraftId;
    @CollectionTable(name = "Aircraft_AircraftType")
    @Enumerated(EnumType.STRING)
    private AircraftType type;
    private int requiredPilots;
    private int requiredCrew;
    private int seats;
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
     * Gets aircraft id.
     *
     * @return the aircraft id
     */
    public int getAircraftId() {
        return aircraftId;
    }

    /**
     * Sets aircraft id.
     *
     * @param aircraftId the aircraft id
     */
    public void setAircraftId(int aircraftId) {
        this.aircraftId = aircraftId;
    }

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
     * Gets required pilots.
     *
     * @return the required pilots
     */
    public int getRequiredPilots() {
        return requiredPilots;
    }

    /**
     * Sets required pilots.
     *
     * @param requiredPilots the required pilots
     */
    public void setRequiredPilots(int requiredPilots) {
        this.requiredPilots = requiredPilots;
    }

    /**
     * Gets required crew.
     *
     * @return the required crew
     */
    public int getRequiredCrew() {
        return requiredCrew;
    }

    /**
     * Sets required crew.
     *
     * @param requiredCrew the required crew
     */
    public void setRequiredCrew(int requiredCrew) {
        this.requiredCrew = requiredCrew;
    }

    /**
     * Gets seats.
     *
     * @return the seats
     */
    public int getSeats() {
        return seats;
    }

    /**
     * Sets seats.
     *
     * @param seats the seats
     */
    public void setSeats(int seats) {
        this.seats = seats;
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
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.aircraftId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Aircraft)) {
            return false;
        }
        final Aircraft other = (Aircraft) obj;
        if (!Objects.equals(this.aircraftId, other.aircraftId)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "at.ac.uibk.ps_swa.ws19.gr7_2.model.Aircraft[ id=" + aircraftId + " ]";
    }

    @Override
    public Integer getId() { return aircraftId; }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) { setAircraftId(id);}

    @Override
    public boolean isNew() {
        return (null == createDate);
    }
}
