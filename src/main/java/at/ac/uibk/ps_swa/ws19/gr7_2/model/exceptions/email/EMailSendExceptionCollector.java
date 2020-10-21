package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/** This checked Exception type is used to encapsulate multiple exceptions
 * that occur while emails are sent.
 * @see EMail
 * @see EMailService
 * The aim of this class is to prevent a bulk email sending operation to
 * fail just because a few out of many emails could not be delivered.
 *
 * It stores emails and the exception that occurred while sending them in a Map
 * @see Map
 *
 * Note: Most getter-methods return <em>unmodifiable</em> versions of the datastructure, to uphold <strong>immutability</strong>.
 *
 * @see Exception
 */
public class EMailSendExceptionCollector extends Exception implements EMailExceptionCollector<EMail>, Serializable {

    private Map<EMail, Exception> failedEMails;

    public EMailSendExceptionCollector(Map<EMail, Exception> failedEMails) {
        this.failedEMails = failedEMails;
    }

    @Override
    public Set<Map.Entry<EMail, Exception>> getFailedAttributesAndExceptions() {
        return Collections.unmodifiableSet(failedEMails.entrySet());
    }

    @Override
    public Set<EMail> getFailedAttributes() {
        return Collections.unmodifiableSet(failedEMails.keySet());
    }

    @Override
    public Collection<Exception> getExceptions() {
        return Collections.unmodifiableCollection(failedEMails.values());
    }

    @Override
    public boolean hasFailedAttributesAndExceptions() {
        return !failedEMails.isEmpty();
    }

    @Override
    public StringBuilder getMessage(StringBuilder sb) {
        //sb.append("Following EMails encountered exceptions while sending:\n");
        if(!failedEMails.isEmpty()) {
            int i = 1;
            for(Map.Entry<EMail, Exception> entry : failedEMails.entrySet()) {
                sb.append("\n");
                sb.append(i++);
                sb.append(".) EMail: ");
                sb.append(entry.getKey());
                sb.append("\nException: ");
                sb.append(entry.getValue().getMessage());
            }
        }
        /*else {
            sb.append(" none");
        }*/
        return sb;
    }

    @Override
    public String getMessage() {
        return this.getMessage(new StringBuilder()).toString();
    }
}
