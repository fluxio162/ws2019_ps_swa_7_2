package at.ac.uibk.ps_swa.ws19.gr7_2.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Implementation of EMailConfig used for testing purposes.
 * Offers configuration for EMails.
 *
 * @see EMailConfig
 * @see at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory
 * @see at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService
 */
@Configuration
@Profile("test")
public class TestEMailConfig implements EMailConfig {

    @Override
    @Bean
    @Profile("test")
    public InternetAddress getDefaultEMailSender() throws UnsupportedEncodingException {
        return new InternetAddress("test@airinfo.com", "Airinfo Test");
    }

    @Override
    @Bean
    public FreeMarkerConfigurationFactoryBean getEMailFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath(String.valueOf(DefaultEMailConfig.class.getResource("/templates/email/")));
        return bean;
    }

    @Override
    @Bean
    public JavaMailSender getJavaMailSender() {
        return new JavaMailSenderImpl();
    }
}
