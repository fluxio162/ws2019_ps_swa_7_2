package at.ac.uibk.ps_swa.ws19.gr7_2.utils;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Set;

/**
 * Utilities for InternetAddress objects. Mainly used for emailing.
 *
 * @see InternetAddress
 */
public class InternetAddressUtils {

    /** Returns the InternetAddress of the given user
     * @param user user
     * @return an InternetAddress object which includes the email and full name of the given user
     * @throws UnsupportedEncodingException if the persons full name could not be encoded
     */
    public static InternetAddress userToInternetAddress(User user) throws UnsupportedEncodingException {
        return new InternetAddress(user.getEmail(), user.getFullName());
    }

    /** Converts a Set of InternetAddress into a String
     * @param addresses Set of InternetAddresses
     * @return a human-readable String listing all given addresses
     */
    public static String setToString(Set<InternetAddress> addresses) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for(InternetAddress address : addresses) {
            if(!first) {
                sb.append(", ");
            }
            else first = false;
            sb.append(address.toString());
        }
        return sb.toString();
    }

    /** Converts a Set of InternetAddress into an Array
     * @param addresses a Set of InternetAddress objects
     * @return a new array containing all InternetAddress objects of the given Set
     */
    public static InternetAddress[] setToArray(Set<InternetAddress> addresses) {
        return addresses.toArray((InternetAddress[]) Array.newInstance(InternetAddress.class, addresses.size()));
    }

}
