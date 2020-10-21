package at.ac.uibk.ps_swa.ws19.gr7_2.model.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.configs.TestEMailConfig;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import static at.ac.uibk.ps_swa.ws19.gr7_2.utils.InternetAddressUtils.setToArray;
import static at.ac.uibk.ps_swa.ws19.gr7_2.utils.InternetAddressUtils.setToString;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestEMailConfig.class, EMailService.class, EMailFactory.class })
@WebAppConfiguration
@ActiveProfiles("test")
class EMailTest {

    private static InternetAddress internetAddress1;
    private static String address1;
    private static String name1 = "name1";
    private static InternetAddress internetAddress2;
    private static String address2;
    private static String name2 = "name2";

    private EMail email;

    @BeforeAll
    static void setupAll() throws UnsupportedEncodingException {
        address1 = name1 + "@airinfo.com";
        address2 = name2 + "@airinfo.com";

        internetAddress1 = new InternetAddress(address1, name1);
        internetAddress2 = new InternetAddress(address2, name2);
    }

    @BeforeEach
    void setup() {
        email = new EMail();
        email.setTo(internetAddress1);
    }

    @Test
    void testSetToArray() {

        Set<InternetAddress> addresses = new HashSet<>();
        addresses.add(internetAddress1);
        addresses.add(internetAddress2);

        InternetAddress[] addressesArr = setToArray(addresses);
        assertEquals(addresses.size(), addressesArr.length);
        for(InternetAddress address : addressesArr) {
            assertTrue(addresses.contains(address));
        }
    }

    @Test
    void testSetToString() {

        Set<InternetAddress> addresses = new HashSet<>();
        addresses.add(internetAddress1);

        String addressesString = setToString(addresses);
        String expectedAddressesString = internetAddress1.toString();
        assertEquals(expectedAddressesString, addressesString);
    }

    @Test
    void testSetToStringMultiple() {

        Set<InternetAddress> addresses = new HashSet<>();
        addresses.add(internetAddress1);
        addresses.add(internetAddress2);

        String addressesString = setToString(addresses);
        String expectedAddressesString = internetAddress1.toString() + ", " + internetAddress2.toString();
        assertEquals(expectedAddressesString, addressesString);
    }

    @Test
    void testSetToStringEmpty() {
        Set<InternetAddress> addresses = new HashSet<>();
        String addressString = setToString(addresses);
        assertEquals("", addressString);
    }

    @Test
    void testTo() {

        Set<InternetAddress> toAddresses = email.getTo();
        assertEquals(1, toAddresses.size());
        assertTrue(toAddresses.contains(internetAddress1));
    }

    @Test
    void testHasCc() {
        EMail email2 = new EMail();
        assertFalse(email2.hasCc());
        email2.addCc(internetAddress1);
        assertTrue(email2.hasCc());
    }

    @Test
    void testHasBcc() {
        EMail email2 = new EMail();
        assertFalse(email2.hasBcc());
        email2.addBcc(internetAddress1);
        assertTrue(email2.hasBcc());
    }

    @Test
    void testHasReplyTo() {
        EMail email2 = new EMail();
        assertFalse(email2.hasReplyTo());
        email2.setReplyTo(internetAddress1);
        assertTrue(email2.hasReplyTo());
    }

    /*@Test
    void testEquals() {
    }*/

    /**
     * Updates all constructors of the EMail class
     * (except for the copy constructor, which has its own test case).
     * It is important that every email instance has the required attributes set
     * (especially 'to', because it is a mutable attribute)
     */
    @Test
    void testConstructors() {

        EMail email1 = new EMail();
        assertNotNull(email1.getTo());

        try {
            EMail email2 = new EMail(address1, address2, "subject", "content");
            assertNotNull(email2.getTo());
        } catch (AddressException e) {
            e.printStackTrace();
            fail();
        }

        Set<InternetAddress> a = new HashSet<>();
        a.add(internetAddress2);
        EMail email3 = new EMail(internetAddress1, a, "subject", "content");
        assertNotNull(email3.getTo());

        // Future proofing: If in the future a new constructor is added, this test will automatically fail
        // to alert developers to update this test case
        assertEquals(4, EMail.class.getDeclaredConstructors().length);
    }

    @Test
    void testCopyConstructor() {

        EMail emailOg = new EMail();
        emailOg.setSubject("subject");
        emailOg.setTo(internetAddress1);
        emailOg.setContent("<p>hello world!</p>");
        emailOg.setFrom(internetAddress2);
        try {
            emailOg.addBcc("test3@airinfo.com");
        } catch (AddressException e) {
            e.printStackTrace();
            fail("Error while attempting to create an InternetAddress instance from String");
        }

        EMail emailCopy = new EMail(emailOg);
        assertEquals(emailOg.getSubject(), emailCopy.getSubject());

        assertEquals(emailOg.getTo().iterator().next(), emailCopy.getTo().iterator().next());
        assertEquals(emailOg.getTo().size(), emailCopy.getTo().size());

        assertEquals(emailOg.getBcc().iterator().next(), emailCopy.getBcc().iterator().next());
        assertEquals(emailOg.getBcc().size(), emailCopy.getBcc().size());

        assertEquals(emailOg.hasCc(), emailCopy.hasCc());

        assertEquals(emailOg.getFrom(), emailCopy.getFrom());
        assertEquals(emailOg.getContent(), emailCopy.getContent());

        assertEquals(emailOg, emailCopy);
    }

}