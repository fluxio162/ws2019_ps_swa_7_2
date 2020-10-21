package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.domain.Persistable;

/**
 * Entity representing users.
 */
@Entity
public class User implements Persistable<String>, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 100)
    private String username;
    @ManyToOne(optional = false)
    private User createUser;
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;
    @ManyToOne(optional = true)
    private User updateUser;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    /**
     * The Enabled.
     */
    boolean enabled;
    @ElementCollection(targetClass = UserRole.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "User_UserRole")
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles;
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleteDate;
    @ManyToOne(optional = true)
    private User deleteUser;

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets first name.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets first name.
     *
     * @param firstName the first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets last name.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets last name.
     *
     * @param lastName the last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets full name.
     *
     * @return full name of the given user (human readable)
     */
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * Gets roles.
     *
     * @return the roles
     */
    public Set<UserRole> getRoles() {
        return roles;
    }

    /**
     * Sets roles.
     *
     * @param roles the roles
     */
    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.username);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return username;
    }

    @Override
    public String getId() {
        return getUsername();
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        setUsername(id);
    }

    @Override
    public boolean isNew() {
        return (null == createDate);
    }

}
