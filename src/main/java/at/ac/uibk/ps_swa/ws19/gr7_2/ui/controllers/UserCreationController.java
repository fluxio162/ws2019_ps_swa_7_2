package at.ac.uibk.ps_swa.ws19.gr7_2.ui.controllers;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.UserService;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Controller to create new user.
 *
 * @author Christoph Kugler
 */
@Component
@Scope("view")
public class UserCreationController {

    @Autowired
    private UserService userService;

    private User user = new User();

    private String role;
    private boolean disableSaveButton = true;


    /**
     * Gets role.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets role.
     *
     * @param role the role
     */
    public void setRole(String role) {
        this.role = role;
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
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the currently displayed user.
     *
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Action to save the currently displayed user.
     */
    public void doSaveUser() {

        if (userService.getAllUsers().contains(this.user)){
            return;
        }

        if (user.getUsername().isEmpty()){
            return;
        }

        if (role.isEmpty()){
            user.setRoles(Sets.newSet(UserRole.USER));
        }

        else{
            user.setRoles(Sets.newSet(UserRole.valueOf(role)));
        }

        user.setEnabled(true);
        user = this.userService.saveUser(user);
    }

    /**
     * Validate username.
     */
    public void validateUsername(){

        if (userService.getAllUsers().contains(this.user)){
            warn("Username already in use.");
            disableSaveButton = true;
            return;
        }

        if (user.getUsername().isEmpty()){
            warn("Username is empty.");
            disableSaveButton = true;
            return;
        }

        disableSaveButton = false;
        acceptUsername();

    }

    /**
     * Warn.
     *
     * @param message the message
     */
    public void warn(String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Warning!", message));
    }

    /**
     * Accept username.
     */
    public void acceptUsername() {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success!", "Username available."));
    }

}
