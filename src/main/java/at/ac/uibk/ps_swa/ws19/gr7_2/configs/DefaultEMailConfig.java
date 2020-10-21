package at.ac.uibk.ps_swa.ws19.gr7_2.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;

/**
 * Default implementation of EMailConfig.
 * Offers configuration for EMails in the default WebApp.
 *
 * @see EMailConfig
 * @see at.ac.uibk.ps_swa.ws19.gr7_2.model.email.EMailFactory
 * @see at.ac.uibk.ps_swa.ws19.gr7_2.services.EMailService
 */
@Configuration
public class DefaultEMailConfig implements EMailConfig {

    @Override
    @Bean
    public InternetAddress getDefaultEMailSender() throws UnsupportedEncodingException {
        return new InternetAddress("noreply@airinfo.com", "Airinfo");
    }

    @Bean
    @Override
    public FreeMarkerConfigurationFactoryBean getEMailFreeMarkerConfiguration() {
        FreeMarkerConfigurationFactoryBean bean = new FreeMarkerConfigurationFactoryBean();
        bean.setTemplateLoaderPath(String.valueOf(DefaultEMailConfig.class.getResource("/templates/email/")));
        return bean;
    }

    @Bean
    @Override
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        return mailSender;
    }

}
