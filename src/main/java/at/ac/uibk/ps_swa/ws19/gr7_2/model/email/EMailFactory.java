package at.ac.uibk.ps_swa.ws19.gr7_2.model.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.configs.EMailConfig;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailCreateExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.Flight;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;

import static at.ac.uibk.ps_swa.ws19.gr7_2.utils.InternetAddressUtils.userToInternetAddress;

/**
 * Factory class for creating emails.
 * <p>
 * It provides a method for each scenario in which an email should be sent.
 *
 * Most methods specify that there are multiple recipients of an EMail.
 * These methods return a list of EMails, where each EMail directly addresses a recipient.
 * @see List
 * If there is only one recipient, the methods can be called via something like this:
 * Stream.of(user).collect(Collectors.toSet());
 * @see Stream
 *
 * @see EMail
 * @see EMailService
 * @see EMailConfig
 */
@Service
public class EMailFactory {

    @Autowired
    private InternetAddress defaultEMailSender;

    /** name of the fullname template placeholder */
    private final String FULLNAME = "fullname";

    /**
     * Create base email.
     *
     * @param subject the subject
     * @return the e mail
     */
    public EMail createBaseEmail(String subject) {

        EMail baseMail = new EMail();
        baseMail.setFrom(defaultEMailSender);
        baseMail.setSubject(subject);
        baseMail.setTemplate("main-layout.ftl");

        Map<String, Object> model = new HashMap<>();
        model.put("subject", subject);
        baseMail.setModel(model);

        return baseMail;
    }

    /** Sets the given flights data as placeholder values into the given email
     * @param email email in which the placeholders should be set
     * @param flight flight containing flight data
     */
    private void addFlightDataToEMailModel(EMail email, Flight flight) {

        email.getModel().put("flightId", flight.getFlightId());
        email.getModel().put("departLocation", flight.getOriginAirport().getName());
        email.getModel().put("arrivalLocation", flight.getDestinationAirport().getName());
        email.getModel().put("aircraftType",
                (flight.getAircraft() != null && flight.getAircraft().getType() != null
                        ? flight.getAircraft().getType().toString() : "unknown"));

        email.getModel().put("departTime", dateToString(flight.getDepartureTime()));
        email.getModel().put("arrivalTime", dateToString(flight.getArrivalTime()));
    }

    private String dateToString(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH)+1) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH) + " " + calendar.get(Calendar.HOUR_OF_DAY)
                + ":" + calendar.get(Calendar.MINUTE);
    }

    /** Creates EMails for all personnel on the flight (crew and pilots)
     * @param flight flight that has been cancelled
     * @return List of emails which contain the same text (except for greeting). Each email is made for a different user.
     * @throws EMailCreateExceptionCollector
     */
    public List<EMail> createFlightCancelledEMail(Flight flight) throws EMailCreateExceptionCollector {
        return createFlightCancelledEMail(flight, flight.getCrewAndPilots().collect(Collectors.toSet()));
    }

    /** Creates EMails for all given Users
     * @param flight flight that has been cancelled
     * @param toUsers one or multiple users that should receive the email
     * @return List of emails which contain the same text (except for greeting). Each email is made for a different user.
     * @throws EMailCreateExceptionCollector
     */
    public List<EMail> createFlightCancelledEMail(Flight flight, Iterable<User> toUsers) throws EMailCreateExceptionCollector {

        EMail baseMail = createBaseEmail("Your flight has been cancelled (No."
                + flight.getFlightId() + ")");
        addFlightDataToEMailModel(baseMail, flight);
        baseMail.getModel().put("contentTemplate", "/flight/flight-cancelled.ftl");

        List<EMail> emails = new ArrayList<>();
        Map<User, Exception> failedEmails = new HashMap<>();

        EMail email = null;
        for(User toUser : toUsers) {
            try {
                email = new EMail(baseMail);
                email.setTo(userToInternetAddress(toUser));
                email.getModel().put(FULLNAME, toUser.getFullName());
                emails.add(email);
            }
            catch (UnsupportedEncodingException e) {
                failedEmails.put(toUser, e);
            }
        }
        // If one or multiple emails could not be created
        if(!failedEmails.isEmpty()) {
            // Throw one single Exception which contains all other exceptions
            throw new EMailCreateExceptionCollector(failedEmails);
        }

        return emails;
    }

    public List<EMail> createFlightProblemEMail(Flight flight) throws EMailCreateExceptionCollector {
        return createFlightProblemEMail(flight, flight.getCrewAndPilots().collect(Collectors.toSet()));
    }

    /**
     * Create flight problem email list.
     *
     * @param flight  the flight
     * @param toUsers the to users
     * @return the list
     * @throws EMailCreateExceptionCollector
     */
    public List<EMail> createFlightProblemEMail(Flight flight, Iterable<User> toUsers) throws EMailCreateExceptionCollector {

        EMail baseMail = createBaseEmail("Your flight has encountered a problem (No."
                + flight.getFlightId() + ")");
        addFlightDataToEMailModel(baseMail, flight);
        baseMail.getModel().put("contentTemplate", "/flight/flight-problem.ftl");

        List<EMail> emails = new ArrayList<>();
        Map<User, Exception> failedEmails = new HashMap<>();

        EMail email = null;
        for(User toUser : toUsers) {
            try {
                email = new EMail(baseMail);
                email.setTo(userToInternetAddress(toUser));
                email.getModel().put(FULLNAME, toUser.getFullName());
                emails.add(email);
            }
            catch (UnsupportedEncodingException e) {
                failedEmails.put(toUser, e);
            }
        }
        // If one or multiple emails could not be created
        if(!failedEmails.isEmpty()) {
            // Throw one single Exception which contains all other exceptions
            throw new EMailCreateExceptionCollector(failedEmails);
        }

        return emails;
    }

    public List<EMail> createFlightAssignmentEMail(Flight flight) throws EMailCreateExceptionCollector {
        return createFlightAssignmentEMail(flight, flight.getCrewAndPilots().collect(Collectors.toSet()));
    }

    /**
     * Create flight assignment email list.
     *
     * @param flight  the flight
     * @param toUsers the to users
     * @return the list
     * @throws EMailCreateExceptionCollector
     */
    public List<EMail> createFlightAssignmentEMail(Flight flight, Iterable<User> toUsers) throws EMailCreateExceptionCollector {

        EMail baseMail = createBaseEmail("New flight assignment");
        baseMail.getModel().put("flightId", flight.getFlightId());
        baseMail.getModel().put("contentTemplate", "/flight/flight-assignment.ftl");
        addFlightDataToEMailModel(baseMail, flight);

        List<EMail> emails = new ArrayList<>();
        Map<User, Exception> failedEmails = new HashMap<>();

        UserRole role;
        for(User toUser : toUsers) {

            try {
                if(flight.getCrew().contains(toUser)) role = UserRole.CREW;
                else if(flight.getPilots().contains(toUser)) role = UserRole.PILOT;
                else role = toUser.getRoles().iterator().next(); // first role of user

                EMail email = new EMail(baseMail);
                email.setTo(userToInternetAddress(toUser));

                email.getModel().put(FULLNAME, toUser.getFullName());
                email.getModel().put("role", role.name());

                emails.add(email);
            }
            catch (UnsupportedEncodingException e) {
                failedEmails.put(toUser, e);
            }
        }
        // If one or multiple emails could not be created
        if(!failedEmails.isEmpty()) {
            // Throw one single Exception which contains all other exceptions
            throw new EMailCreateExceptionCollector(failedEmails);
        }

        return emails;
    }
}