package at.ac.uibk.ps_swa.ws19.gr7_2.services;

import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMail;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.InsufficientEMailAttributesException;
import at.ac.uibk.ps_swa.ws19.gr7_2.model.exceptions.email.EMailSendExceptionCollector;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Service for sending emails.
 * EMails are created via EMailFactory and sent via EmailService.
 *
 * @see EMail
 * @see EMailFactory Before emails are sent, they are validated via the validateEMail method. If an EMail is invalid, a checked InsufficientEMailAttributesException is thrown.
 * @see InsufficientEMailAttributesException
 * @see EMailSendExceptionCollector
 */
@Component
@Scope("application")
public class EMailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EMailService.class);

    @Qualifier("getJavaMailSender")
    @Autowired
    private JavaMailSender emailSender;
    @Qualifier("getEMailFreeMarkerConfiguration")
    @Autowired
    private Configuration freemarkerConfig;
    @Autowired
    private InternetAddress defaultEMailSender;

    /**
     * Sends the given email(s).
     * Each given email must store all data necessary for sending it.
     * </br>
     * This method tries to send all given emails, even if some of them throw exceptions.
     *
     * @param emails one or multiple emails
     * @throws EMailSendExceptionCollector thrown if one or multiple given emails could not be sent.
     */
    public void sendEMails(Collection<EMail> emails) throws EMailSendExceptionCollector {

        Map<EMail, Exception> failedEmails = new HashMap<>();
        for(EMail email : emails) {

            MimeMessage message = emailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message,
                        MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                        StandardCharsets.UTF_8.name());

                processEMailTemplate(email);
                validateEMail(email);

                helper.setTo(email.getToAsArray());
                helper.setText(email.getContent(), true);
                helper.setSubject(email.getSubject());
                helper.setFrom(email.getFrom());
                if(email.hasBcc()) helper.setBcc(email.getBccAsArray());
                if(email.hasCc()) helper.setCc(email.getCcAsArray());
                if(email.hasReplyTo()) helper.setReplyTo(email.getReplyTo());

                emailSender.send(message);
                LOGGER.info("Successfully sent email '{}' to {}", email.getSubject(), email.getTo());

            } catch (Exception e) {
                // collect occurring exceptions
                failedEmails.put(email, e);
            }
        }

        // if any exceptions occurred during sending
        if(failedEmails.size() > 0) {
            // throw one single exception which contains info about every error
            throw new EMailSendExceptionCollector(failedEmails);
        }
    }

    /**
     * Uses the model of the given email to replace the placeholders in the template.
     * Requires that the given EMail-object has its model- and template-attributes set.
     *
     * @param email EMail object with valid model- and template-attributes
     * @throws IOException if there is a problem with finding the Freemarker .ftl template file
     * @throws TemplateException if there is a discrepancy between the placeholders declared in the .ftl template file
     * and the model.
     */
    public void processEMailTemplate(EMail email) throws IOException, TemplateException {

        if(email.hasTemplate()) {
            Template t = freemarkerConfig.getTemplate(email.getTemplate());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, email.getModel());
            email.setContent(html);
        }
    }

    /**
     * Checks if the given EMail specifies all necessary attributes.
     * If all necessary attributes exist except for 'from', it is set to the default 'from'-address
     * specified in EMailConfig.
     *
     * @param email email
     * @throws InsufficientEMailAttributesException thrown if one or multiple necessary attributes are missing  ('to', 'subject', 'content')
     */
    void validateEMail(EMail email) throws InsufficientEMailAttributesException {

        StringBuilder errorMsg = new StringBuilder();
        if(email.getTo() == null || email.getTo().isEmpty()) errorMsg.append("'to' ");
        if(email.getSubject() == null || email.getSubject().isEmpty()) errorMsg.append("'subject' ");
        if(email.getContent() == null || email.getContent().isEmpty()) errorMsg.append("'content' ");

        if(errorMsg.length() < 1) { // no error occurred

            if(email.getFrom() == null || email.getFrom().getAddress().isEmpty()) {
                LOGGER.warn("Validated EMail {} was with no 'from' address. The default 'from' address is set.", email);
                email.setFrom(defaultEMailSender);
            }
        }
        else { // error occurred
            // extend error message
            errorMsg.insert(0, "EMail is missing the following fields: ");
            errorMsg.append("\n(EMail: ");
            errorMsg.append(email);
            errorMsg.append(")");

            throw new InsufficientEMailAttributesException(errorMsg.toString(), email);
        }
    }
}
