package at.ac.uibk.ps_swa.ws19.gr7_2.model.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;

import static at.ac.uibk.ps_swa.ws19.gr7_2.utils.InternetAddressUtils.setToArray;
import static at.ac.uibk.ps_swa.ws19.gr7_2.utils.InternetAddressUtils.setToString;

/**
 * Representation of an email.
 * <p>
 * The actual content of an email can be manually set as a String,
 * or it can be set via a template and a content model.
 * The template contains placeholders which will be
 * replaced by the contents of a content model.
 * <p>
 * An EMail instance can be easily created by an EMailFactory
 * @see EMailFactory
 * The EMail class also employs the <strong>Prototype-Pattern</strong>.
 * To create a copy of an EMail-object, use the Copy-Constructor.
 *
 * To send an EMail, use EmailService
 * @see EMailService
 *
 * EMail addresses (and the fullname of the person that owns the EMail address) are stored within
 * InternetAddress objects.
 * @see InternetAddress
 *
 * Most Set-attributes within this class use HashSet as default implementation.
 * @see Set
 * @see HashSet
 */
public class EMail implements Serializable {

    /** sender of the email */
    private InternetAddress from;
    /** recipients of the email */
    private Set<InternetAddress> to;
    private String subject;
    /** main content of the email.
     * Set by evaluating the template with data in model. */
    private String content;
    /** name of the template file (relative to template folder in EMailConfig) */
    private String template;
    /** key-value pairs of data that should be inserted into the template */
    private Map<String, Object> model;

    /** email address(es) for the CC (carbon copy) field */
    private Set<InternetAddress> cc;
    /** email address(es) for the BCC (blind carbon copy) field */
    private Set<InternetAddress> bcc;
    /** email address for the replyTo field.
     * Most email clients will recommend that the receiver of an email should
     * send their replies to the 'replyTo' email address */
    private InternetAddress replyTo;

    /**
     * Empty constructor
     */
    public EMail() {
        this.to = new HashSet<>();
    }

    /**
     * Constructor for all required email attributes.
     * EMail addresses are given as a String and then subsequently converted into InternetAddress objects.
     * @param from email address of the sender
     * @param to email address of the recipient
     * @param subject subject of this email
     * @param content actual content of the email (can be HTML)
     * @throws AddressException if the given String email address (for 'from' or 'to') could not be converted into a
     * valid InternetAddress object.
     */
    public EMail(String from, String to, String subject, String content) throws AddressException {
        this();
        this.from = new InternetAddress(from);
        this.to.add(new InternetAddress(to));
        this.subject = subject;
        this.content = content;
    }

    /**
     * Constructor for all required email attributes.
     * EMail addresses are given as a InternetAddress object.
     * @param from email address of the sender
     * @param to email address of the recipient
     * @param subject subject of this email
     * @param content actual content of the email (can be HTML)
     */
    public EMail(InternetAddress from, Set<InternetAddress> to, String subject, String content) {
        this();
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    /**
     * Copy constructor.
     * Creates a copy of the given email object.
     * @param email email that should be copied.
     */
    public EMail(EMail email) {

        this.setFrom(email.getFrom());
        this.setTo(new HashSet<>(email.getTo()));
        if(email.hasBcc()) this.setBcc(new HashSet<>(email.getBcc()));
        if(email.hasCc()) this.setCc(new HashSet<>(email.getCc()));
        if(email.hasReplyTo()) this.setReplyTo(email.getReplyTo());

        this.setSubject(email.getSubject());
        this.setTemplate(email.getTemplate());
        this.setContent(email.getContent());
        if(email.getModel() != null) {
            this.setModel(new HashMap<>(email.getModel()));
        }
    }

    /**
     * Gets from.
     *
     * @return the from
     */
    public InternetAddress getFrom() {
        return from;
    }

    /**
     * Sets from.
     *
     * @param from the from
     * @throws AddressException the address exception
     */
    public void setFrom(String from) throws AddressException {
        setFrom(new InternetAddress(from));
    }

    /**
     * Sets from.
     *
     * @param from the from
     */
    public void setFrom(InternetAddress from) {
        this.from = from;
    }

    /**
     * Gets to.
     *
     * @return the to
     */
    public Set<InternetAddress> getTo() {
        return Collections.unmodifiableSet(this.to);
    }

    /** @return a new array containing all 'to' InternetAddress objects  */
    public InternetAddress[] getToAsArray() {
        return setToArray(this.to);
    }

    /**
     * Gets to as string.
     *
     * @return the to as string
     */
    public String getToAsString() {
        return setToString(this.to);
    }

    /**
     * Sets to.
     *
     * @param to the to
     * @throws AddressException the address exception
     */
    public void setTo(String to) throws AddressException {
        setTo(new InternetAddress(to));
    }

    /**
     * Sets to.
     *
     * @param to the to
     */
    public void setTo(InternetAddress to) {
        this.to.clear();
        this.to.add(to);
    }

    /**
     * Sets to.
     *
     * @param to the to
     */
    public void setTo(Set<InternetAddress> to) {
        this.to = to;
    }

    /**
     * Add to.
     *
     * @param to the to
     */
    public void addTo(InternetAddress to) {
        this.to.add(to);
    }

    /**
     * Add to.
     *
     * @param to the to
     * @throws AddressException the address exception
     */
    public void addTo(String to) throws AddressException {
        addTo(new InternetAddress(to));
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets content.
     *
     * @param content the content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Gets template.
     *
     * @return the template
     */
    public String getTemplate() {
        return template;
    }

    /**
     * Has template boolean.
     *
     * @return the boolean
     */
    public boolean hasTemplate() {
        return !(this.template == null || this.template.isEmpty());
    }

    /**
     * Sets template.
     *
     * @param template the template
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * Gets model.
     *
     * @return the model
     */
    public Map<String, Object> getModel() {
        return this.model;
    }

    /**
     * Sets model.
     *
     * @param model the model
     */
    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    /**
     * Gets cc.
     *
     * @return the cc
     */
    public Set<InternetAddress> getCc() {
        return Collections.unmodifiableSet(this.cc);
    }

    /** @return a new array containing all 'cc' InternetAddress objects  */
    public InternetAddress[] getCcAsArray() {
        return setToArray(this.cc);
    }

    /**
     * Gets cc as string.
     *
     * @return the cc as string
     */
    public String getCcAsString() {
        return setToString(this.cc);
    }

    /**
     * Sets cc.
     *
     * @param cc the cc
     */
    public void setCc(Set<InternetAddress> cc) {
        this.cc = cc;
    }

    /**
     * Add cc.
     *
     * @param cc the cc
     * @throws AddressException the address exception
     */
    public void addCc(String cc) throws AddressException {
        addCc(new InternetAddress(cc));
    }

    /**
     * Add cc.
     *
     * @param cc the cc
     */
    public void addCc(InternetAddress cc) {
        if(this.cc == null) this.cc = new HashSet<>();
        this.cc.add(cc);
    }

    /**
     * Gets bcc.
     *
     * @return the bcc
     */
    public Set<InternetAddress> getBcc() {
        return Collections.unmodifiableSet(this.bcc);
    }

    /** @return a new array containing all 'bcc' InternetAddress objects  */
    public InternetAddress[] getBccAsArray() {
        return setToArray(this.bcc);
    }

    /**
     * Gets bcc as string.
     *
     * @return the bcc as string
     */
    public String getBccAsString() {
        return setToString(this.bcc);
    }

    /**
     * Sets bcc.
     *
     * @param bcc the bcc
     */
    public void setBcc(Set<InternetAddress> bcc) {
        this.bcc = bcc;
    }

    /**
     * Add bcc.
     *
     * @param bcc the bcc
     * @throws AddressException the address exception
     */
    public void addBcc(String bcc) throws AddressException {
        addBcc(new InternetAddress(bcc));
    }

    /**
     * Add bcc.
     *
     * @param bcc the bcc
     */
    public void addBcc(InternetAddress bcc) {
        if(this.bcc == null) this.bcc = new HashSet<>();
        this.bcc.add(bcc);
    }

    /**
     * Gets reply to.
     *
     * @return the reply to
     */
    public InternetAddress getReplyTo() {
        return replyTo;
    }

    /**
     * Sets reply to.
     *
     * @param replyTo the reply to
     */
    public void setReplyTo(InternetAddress replyTo) {
        this.replyTo = replyTo;
    }

    /** @return is the 'CC'-field set? */
    public boolean hasCc() {
        return (this.cc != null) && (!this.cc.isEmpty());
    }

    /** @return is the 'BCC'-field set? */
    public boolean hasBcc() {
        return (this.bcc != null) && (!this.bcc.isEmpty());
    }

    /** @return is the 'replyTo'-field set? */
    public boolean hasReplyTo() {
        return this.replyTo != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EMail eMail = (EMail) o;
        return from.equals(eMail.from) &&
                to.equals(eMail.to) &&
                subject.equals(eMail.subject) &&
                Objects.equals(template, eMail.template) &&
                Objects.equals(model, eMail.model) &&
                Objects.equals(cc, eMail.cc) &&
                Objects.equals(bcc, eMail.bcc) &&
                Objects.equals(replyTo, eMail.replyTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, subject, template, model, cc, bcc, replyTo);
    }

    @Override
    public String toString() {
        return "EMail{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                '}';
    }
}
