package at.ac.uibk.ps_swa.ws19.gr7_2.model.email;

import at.ac.uibk.ps_swa.ws19.gr7_2.configs.TestEMailConfig;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailSendExceptionCollector;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.User;
import at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.CollectionUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static at.ac.uibk.ps_swa.ws19.gr7_2.utils.InternetAddressUtils.userToInternetAddress;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestEMailConfig.class, EMailService.class, EMailFactory.class })
@WebAppConfiguration
@ActiveProfiles("test")
class EMailFactoryTest {

    @Autowired
    private EMailService emailService;
    @Autowired
    private EMailFactory eMailFactory;

    private static String testEmail = "user@airinfo.com";
    private static String testFirstname = "Firstname";
    private static String testLastname = "Lastname";

    private static User user;

    @BeforeAll
    static void setup() {
        user = new User();
        user.setEmail(testEmail);
        user.setFirstName(testFirstname);
        user.setLastName(testLastname);
    }

    @Test
    void testEMail() {
        try {
            EMail mail = eMailFactory.createBaseEmail("Test");
            mail.setContent("Test content");
            mail.setTo("test@airinfo.com");
            mail.setTemplate("test-layout.ftl");
            mail.getModel().put("fullname", "Testuser");

            emailService.sendEMails(Stream.of(mail).collect(Collectors.toSet()));
        } catch (EMailSendExceptionCollector | AddressException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void testUserToInternetAddress() {
        try {
            InternetAddress address = userToInternetAddress(user);

            assertEquals(testEmail, address.getAddress());
            assertEquals(user.getFullName(), address.getPersonal());
            assertFalse(address.isGroup());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            fail("Exception called while creating InternetAddress from User");
        }
    }
}