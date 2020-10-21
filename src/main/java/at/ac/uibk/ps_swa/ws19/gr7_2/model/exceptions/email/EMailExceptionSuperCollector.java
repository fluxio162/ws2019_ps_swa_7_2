package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.utils.MapEntryKeyIterator;
import at.ac.uibk.ps_swa.ws19.gr7_2.utils.MapEntryValueIterator;

import java.io.Serializable;
import java.util.*;

/**
 * This checked Exception type is used to encapsulate multiple {@link EMailExceptionCollector} exceptions
 * that occur during EMail operations.
 * @see EMail
 * It implements {@link EMailExceptionCollector}
 * @see EMailExceptionCollector
 *
 * The aim of this class is to prevent a bulk email creation operation to
 * fail just because a few out of many emails could not be created.
 *
 * {@link EMailExceptionCollector} instances themself already encapsulate multiple exceptions.
 * Therefore, this class allows the iteration over exceptions <em>within other</em> EMailExceptionCollector exceptions.
 * To do so, it uses {@link EMailExceptionCollectorIterator} combined with {@link MapEntryKeyIterator}
 * and {@link MapEntryValueIterator}.
 * @see EMailExceptionCollectorIterator
 * @see MapEntryValueIterator
 * @see MapEntryKeyIterator
 *
 */
public class EMailExceptionSuperCollector extends Exception implements EMailExceptionCollector<Object>, Serializable {

    /**
     * All accumulated EMailExceptionCollector exceptions (can have any type)
     */
    private Collection<EMailExceptionCollector<? extends Object>> exceptions;

    /**
     *
     * @param exceptions All accumulated EMailExceptionCollector exceptions (can have any type)
     */
    public EMailExceptionSuperCollector(Collection<EMailExceptionCollector<? extends Object>> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public Iterable<Map.Entry<Object, Exception>> getFailedAttributesAndExceptions() {
        return new EMailExceptionCollectorIterable();
    }

    @Override
    public Iterable<Object> getFailedAttributes() {
        return new FailedAttributesIterable();
    }

    @Override
    public Iterable<Exception> getExceptions() {
        return new ExceptionsIterable();
    }

    @Override
    public StringBuilder getMessage(StringBuilder sb) {
        getFailedAttributesAndExceptions().forEach(objectExceptionEntry -> {
                sb.append(objectExceptionEntry.toString());
        });
        return sb;
    }

    @Override
    public String getMessage() {
        return this.getMessage(new StringBuilder()).toString();
    }

    @Override
    public boolean hasFailedAttributesAndExceptions() {
        return exceptions.stream().anyMatch(EMailExceptionCollector::hasFailedAttributesAndExceptions);
    }

    /**
     * {@link Iterable} which wraps an {@link EMailExceptionCollectorIterator}.
     * @see Iterable
     * @see EMailExceptionCollectorIterator
     */
    private class FailedAttributesIterable implements Iterable<Object> {
        @Override
        public Iterator<Object> iterator() {
            return new MapEntryKeyIterator<>(new EMailExceptionCollectorIterator(exceptions));
        }
    }

    private class ExceptionsIterable implements Iterable<Exception> {
        @Override
        public Iterator<Exception> iterator() {
            return new MapEntryValueIterator<>(new EMailExceptionCollectorIterator(exceptions));
        }
    }

    /**
     * {@link Iterable} which wraps an {@link EMailExceptionCollectorIterator}.
     * @see Iterable
     * @see EMailExceptionCollectorIterator
     */
    private class EMailExceptionCollectorIterable implements Iterable<Map.Entry<Object, Exception>> {

        @Override
        public Iterator<Map.Entry<Object, Exception>> iterator() {
            return new EMailExceptionCollectorIterator(exceptions);
            /*return new EMailExceptionCollectorIterator(Stream.concat(
                            exceptions.stream().filter(ex -> ex.getClass().equals(EMailCreateExceptionCollector.class)),
                            exceptions.stream().filter(ex -> !ex.getClass().equals(EMailCreateExceptionCollector.class))
            ));*/
        }
    }
}
