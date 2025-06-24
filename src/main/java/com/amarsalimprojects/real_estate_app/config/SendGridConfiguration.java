package com.amarsalimprojects.real_estate_app.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.objects.Email;

@Configuration
@EnableConfigurationProperties(SendGridConfigurationProperties.class)
public class SendGridConfiguration {

    private final SendGridConfigurationProperties props;

    public SendGridConfiguration(SendGridConfigurationProperties props) {
        this.props = props;
    }

    @Bean
    public SendGrid sendGrid() {
        return new SendGrid(props.getApiKey());
    }

    @Bean
    public Email fromEmail() {
        return new Email(props.getFromEmail(), props.getFromName());
    }
}
