package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * This checked Exception type is used to encapsulate multiple exceptions
 * that occur while emails objects are created.
 * @see EMail
 * @see EMailFactory
 * It implements {@link EMailExceptionCollector}
 * @see EMailExceptionCollector
 *
 * The aim of this class is to prevent a bulk email creation operation to
 * fail just because a few out of many emails could not be created.
 *
 * It stores recipients (Users) and the exception that occurred while creating the emails in a Map
 * @see Map
 * @see User
 *
 * Note: Most getter-methods return <em>unmodifiable</em> versions of the datastructure, to uphold <strong>immutability</strong>.
 *
 * @see Exception
 * @see EMailService
 */
public class EMailCreateExceptionCollector extends Exception implements EMailExceptionCollector<User>, Serializable {

    /**
     * All users (Key) that caused an exception while creating an email for them
     * with the respective thrown Exception (value)
     */
    private Map<User, Exception> failedUsers;

    /**
     * Creates a new immutable EMailCreateExceptionCollector instance.
     *
     * @param failedUsers All users (Key) that caused an exception while creating an email for them
     *  with the respective thrown Exception (value) in a map
     */
    public EMailCreateExceptionCollector(Map<User, Exception> failedUsers) {
        this.failedUsers = failedUsers;
    }

    @Override
    public Set<Map.Entry<User, Exception>> getFailedAttributesAndExceptions() {
        return Collections.unmodifiableSet(failedUsers.entrySet());
    }

    @Override
    public Set<User> getFailedAttributes() {
        return Collections.unmodifiableSet(failedUsers.keySet());
    }

    @Override
    public Collection<Exception> getExceptions() {
        return Collections.unmodifiableCollection(failedUsers.values());
    }

    @Override
    public boolean hasFailedAttributesAndExceptions() {
        return !failedUsers.isEmpty();
    }

    @Override
    public StringBuilder getMessage(StringBuilder sb) {
        //sb.append("EMails for the following users could not be created:");
        if(failedUsers.size() > 0) {
            int i = 1;
            for(Map.Entry<User, Exception> entry : failedUsers.entrySet()) {
                sb.append("\n");
                sb.append(i++);
                sb.append(".) User: ");
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
