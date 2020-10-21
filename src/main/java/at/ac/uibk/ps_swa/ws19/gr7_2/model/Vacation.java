package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.*;
import java.util.List;


/**
 * Entity representing vacation.
 *
 * @author Christoph Wittauer
 */
@Entity
public class Vacation implements Serializable, Persistable<Integer> {

    @Id
    private int vacationId;

    @ManyToOne(optional = false)
    private User createUser;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    private Date vacationStart;
    private Date vacationEnd;
    private int daysTaken;

    /**
     * Gets vacation id.
     *
     * @return the vacation id
     */
    public int getVacationId() {
        return vacationId;
    }

    /**
     * Sets vacation id.
     *
     * @param vacationId the vacation id
     */
    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
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
     * Gets vacation start.
     *
     * @return the vacation start
     */
    public Date getVacationStart() {
        return vacationStart;
    }

    /**
     * Sets vacation start.
     *
     * @param vacationStart the vacation start
     */
    public void setVacationStart(Date vacationStart) {
        this.vacationStart = vacationStart;
    }

    /**
     * Gets vacation end.
     *
     * @return the vacation end
     */
    public Date getVacationEnd() {
        return vacationEnd;
    }

    /**
     * Sets vacation end.
     *
     * @param vacationEnd the vacation end
     */
    public void setVacationEnd(Date vacationEnd) {
        this.vacationEnd = vacationEnd;
    }

    /**
     * Gets days taken.
     *
     * @return the days taken
     */
    public int getDaysTaken() {
        return daysTaken;
    }

    /**
     * Sets days taken.
     *
     * @param daysTaken the days taken
     */
    public void setDaysTaken(int daysTaken) {
        this.daysTaken = daysTaken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return vacationId == vacation.vacationId &&
                Objects.equals(createUser, vacation.createUser) &&
                Objects.equals(createDate, vacation.createDate) &&
                Objects.equals(vacationStart, vacation.vacationStart) &&
                Objects.equals(vacationEnd, vacation.vacationEnd) &&
                Objects.equals(daysTaken, vacation.daysTaken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vacationId, createUser, createDate, vacationStart, vacationEnd, daysTaken);
    }

    @Override
    public Integer getId() {
        return vacationId;
    }

    @Override
    public boolean isNew() {
        return (null == this.createDate);
    }
}
