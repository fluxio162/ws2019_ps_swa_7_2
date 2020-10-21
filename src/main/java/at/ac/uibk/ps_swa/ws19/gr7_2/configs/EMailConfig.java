package at.ac.uibk.ps_swa.ws19.gr7_2.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Offers configuration for EMails.
 *
 * @see DefaultEMailConfig
 */
public interface EMailConfig {

    /**
     * @return Default 'from'-EMail address. Should be used when no concrete 'from'-EMail address is specified.
     * @throws AddressException thrown if the email address cannot be converted into a valid InternetAddress object
     * @throws UnsupportedEncodingException thrown if the email address cannot be converted into a valid InternetAddress object
     */
    @Bean
    InternetAddress getDefaultEMailSender() throws AddressException, UnsupportedEncodingException;

    /**
     *
     * @return FreeMarker configuration for emails
     */
    @Bean
    FreeMarkerConfigurationFactoryBean getEMailFreeMarkerConfiguration();

    @Bean
    JavaMailSender getJavaMailSender();
}
