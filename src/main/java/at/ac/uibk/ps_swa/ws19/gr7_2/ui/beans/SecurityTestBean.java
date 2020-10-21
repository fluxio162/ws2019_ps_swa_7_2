package at.ac.uibk.ps_swa.ws19.gr7_2.ui.beans;

import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

/**
 * Basic request scoped bean to test bean initialization.
 * <p>
 * This class is part of the skeleton project provided for students of the
 * courses "Software Architecture" and "Software Engineering" offered by the
 * University of Innsbruck.
 */
@Component
@Scope("request")
public class SecurityTestBean {

    private boolean showOkDialog = false;
    private String performedAction = "NONE";

    /**
     * Is show ok dialog boolean.
     *
     * @return the boolean
     */
    public boolean isShowOkDialog() {
        return showOkDialog;
    }

    /**
     * Gets performed action.
     *
     * @return the performed action
     */
    public String getPerformedAction() {
        return performedAction;
    }

    /**
     * Checks if the user has role ADMIN.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    public void doAdminAction() {
        performedAction = "ADMIN";
        showOkDialog = true;
    }

    /**
     * Checks if the user has role MANAGER.
     */
    @PreAuthorize("hasAuthority('MANAGER')")
    public void doManagerAction() {
        performedAction = "MANAGER";
        showOkDialog = true;
    }

    /**
     * Checks if the user has role PILOT.
     */
    @PreAuthorize("hasAuthority('PILOT')")
    public void doPilotAction() {
        performedAction = "PILOT";
        showOkDialog = true;
    }

    /**
     * Checks if the user has role CREW.
     */
    @PreAuthorize("hasAuthority('CREW')")
    public void doCrewAction() {
        performedAction = "CREW";
        showOkDialog = true;
    }

    /**
     * Checks if the user has role USER.
     */
    @PreAuthorize("hasAuthority('USER')")
    public void doUserAction() {
        performedAction = "USER";
        showOkDialog = true;
    }

    /**
     * Do hide ok dialog.
     */
    public void doHideOkDialog() {
        showOkDialog = false;
    }

}
