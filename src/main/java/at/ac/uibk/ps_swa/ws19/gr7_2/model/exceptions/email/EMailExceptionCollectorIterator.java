package at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email;

import java.util.*;
import java.util.stream.Stream;

/**
 * {@link Iterator} which iterates over attributes and exceptions that have been accumulated in
 * multiple {@link EMailExceptionCollector} instances.
 *
 * It can be combined with {@link at.ac.uibk.ps_swa.ws19.gr7_2.utils.MapEntryValueIterator}
 * or {@link at.ac.uibk.ps_swa.ws19.gr7_2.utils.MapEntryKeyIterator} to iterate
 * over <em>only</em> the values or keys, respectively.
 *
 * @see Iterator
 * @see EMailExceptionCollector
 * @see EMailExceptionSuperCollector
 * @param <T> type of attributes within EMailExceptionCollector (can be {@link Object})
 */
class EMailExceptionCollectorIterator<T> implements Iterator<Map.Entry<T, Exception>> {

    /**
     * The main iterator over EMailExceptionCollector instances
     */
    private Iterator<EMailExceptionCollector<T>> exceptionIterator;

    /**
     * The iterator over the current EMailExceptionCollector exception within exceptionIterator.
     */
    private Iterator<Map.Entry<T, Exception>> curExceptionIterator;

    public EMailExceptionCollectorIterator(Stream<EMailExceptionCollector<T>> exceptions) {
        this.exceptionIterator = exceptions
                .filter(EMailExceptionCollector::hasFailedAttributesAndExceptions)
                .iterator();

        if(this.exceptionIterator.hasNext()) {
            this.curExceptionIterator = this.exceptionIterator.next().getFailedAttributesAndExceptions().iterator();
        }
        else {
            this.curExceptionIterator = Collections.emptyIterator();
        }
    }

    public EMailExceptionCollectorIterator(Collection<EMailExceptionCollector<T>> exceptions) {
        this(exceptions.stream());
    }

    @Override
    public boolean hasNext() {
        return (this.curExceptionIterator.hasNext() || this.exceptionIterator.hasNext());
    }

    /**
     * Gets the next entry. Updates the curExceptionIterator if needed.
     *
     * This method has been extracted from next() to prevent next() from being recursive.
     *
     * @return next entry
     * @throws NoSuchElementException if no next element exists.
     */
    private Map.Entry<T, Exception> getNext() throws NoSuchElementException {
        if(this.curExceptionIterator.hasNext()) {
            return this.curExceptionIterator.next();
        }

        // following line could throw a NoSuchElementException (if there are no exceptions in exceptionIterator left)
        // (but hasNext prevents it)
        this.curExceptionIterator = this.exceptionIterator.next().getFailedAttributesAndExceptions().iterator();
        return getNext();
    }

    @Override
    public Map.Entry<T, Exception> next() throws NoSuchElementException {
        return getNext();
    }
}
