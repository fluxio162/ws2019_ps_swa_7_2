package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * This Interface is implemented by different kinds of collector-Exceptions for EMail Exceptions.
 * Collector-Exceptions encapsulate multiple (email) exceptions.
 *
 * EMail exceptions have a special use-case:
 * ** EMail exceptions should not prevent other code from running
 * (i.e. stop creating a flight just because an email couldn't be sent)
 * ** There are a lot of things that can go wrong with emails
 * (template not found, template attributes insufficient, email address parsing, wrong/insufficient email attributes, etc.)
 * ** But still, the User should be notified when an EMail couldn't be sent (via UI)
 *
 * Therefore, there is a need to collect, encapsulate and unify these errors until they are shown in the UI.
 * EMailExceptionCollector instances collect lower-level exceptions.
 * @see InsufficientEMailAttributesException
 * @see java.io.IOException for freemarker templates
 * @see freemarker.core.ParseException
 * @see javax.mail.internet.AddressException for parsing EMail addresses
 * @see UnsupportedEncodingException for parsing EMail addresses
 *
 * One step above that are {@link EMailExceptionSuperCollector}, which
 * collect multiple EMailExceptionCollector instances or even other EMailExceptionSuperCollector instances.
 * @see EMailExceptionSuperCollector
 *
 * Note: EMailExceptionCollectors are still {@link Exception}s, so they should still be <strong>immutable</strong>.
 * @see Exception
 *
 * Known implementations of this Interface are {@link EMailCreateExceptionCollector} and {@link EMailSendExceptionCollector}
 *
 * @param <T> type of attribute at which the exception was thrown
 */
public interface EMailExceptionCollector<T> extends Serializable {

    /**
     *
     * @return all exceptions that have been accumulated with the respective attribute at which the exception was thrown
     */
    Iterable<Map.Entry<T, Exception>> getFailedAttributesAndExceptions();

    /**
     *
     * @return all attributes that (indirectly) raised an exception
     */
    Iterable<T> getFailedAttributes();

    /**
     *
     * @return all exceptions that have been accumulated
     */
    Iterable<Exception> getExceptions();

    /**
     * This method should be overridden, because it is likely that a concrete implementation
     * can do this more effectively.
     * @return whether this instance has accumulated any exceptions
     */
    default boolean hasFailedAttributesAndExceptions() {
        return getFailedAttributes().iterator().hasNext();
    }

    /**
     * Accumulate the messages from all exceptions.
     * All messages are written into the given StringBuilder (to improve performance for SuperCollectors)
     * @param sb StringBuilder instance into which the message should be added
     * @return the same StringBuilder instance that has been given as parameter
     */
    StringBuilder getMessage(StringBuilder sb);
}
