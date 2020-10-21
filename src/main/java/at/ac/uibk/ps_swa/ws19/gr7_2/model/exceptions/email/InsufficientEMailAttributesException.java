package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;

/** Checked Exception that is thrown when attempting
 * to send an email with insufficient attributes/fields.
 * @see EMail
 *
 * The following fields are required to send an email:
 * 'from', 'to', 'subject', 'content'.
 * Whereby the 'from' attribute can be taken from the default from address
 * specified in an EMailConfig instance.
 * @see at.ac.uibk.ps_swa.ws19.gr7_2.configs.EMailConfig
 *
 * @see EMailService
 */
public class InsufficientEMailAttributesException extends Exception {

    /** EMail for which the Exception occurred */
    private EMail email;

    /**
     * @param message detailed message stating what attributes were missing
     * @param email EMail for which the Exception occurred
     */
    public InsufficientEMailAttributesException(String message, EMail email) {
        super(message);
        this.email = email;
    }

    /**
     * @param email EMail for which the Exception occurred
     */
    public InsufficientEMailAttributesException(EMail email) {
        this.email = email;
    }

    /**
     * @return EMail for which the Exception occurred
     */
    public EMail getEmail() {
        return email;
    }
}
