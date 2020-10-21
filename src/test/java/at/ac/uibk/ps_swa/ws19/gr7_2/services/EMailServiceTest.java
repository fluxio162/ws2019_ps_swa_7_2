package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.configs.TestEMailConfig;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.InsufficientEMailAttributesException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.mail.internet.AddressException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { TestEMailConfig.class, EMailService.class, EMailFactory.class })
@WebAppConfiguration
@ActiveProfiles("test")
class EMailServiceTest {

    @Autowired
    private EMailService emailService;

    @Test
    void testValidateEMail() throws AddressException {

        EMail eMail = new EMail();

        assertNull(eMail.getFrom());

        assertThrows(InsufficientEMailAttributesException.class, () -> emailService.validateEMail(eMail));
        eMail.setTo("test@airinfo.com");
        assertThrows(InsufficientEMailAttributesException.class, () -> emailService.validateEMail(eMail));
        eMail.setContent("test content");
        assertThrows(InsufficientEMailAttributesException.class, () -> emailService.validateEMail(eMail));
        eMail.setSubject("test subject");
        assertDoesNotThrow(() -> emailService.validateEMail(eMail));

        // 'from' should be set to default from-address
        assertNotNull(eMail.getFrom());
    }

    @Test
    void testProcessEMailTemplateNone() {

        EMail eMail = new EMail();
        assertDoesNotThrow(() -> emailService.processEMailTemplate(eMail));
        assertFalse(eMail.hasTemplate());
        assertTrue(eMail.getContent() == null || eMail.getContent().isEmpty());
    }

}