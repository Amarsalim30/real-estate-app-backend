package com.amarsalimprojects.real_estate_app.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

@Service
public class EmailDispatcher {

    private static final String EMAIL_ENDPOINT = "mail/send";
    private final SendGrid sendGrid;
    private final Email fromEmail;

    public EmailDispatcher(SendGrid sendGrid, Email fromEmail) {
        this.sendGrid = sendGrid;
        this.fromEmail = fromEmail;
    }

    public void dispatchEmail(String emailId, String subject, String body) {
        Email toEmail = new Email(emailId);
        Content content = new Content("text/html", body);
        Mail mail = new Mail(fromEmail, subject, toEmail, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint(EMAIL_ENDPOINT);
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            System.out.println("Email status: " + response.getStatusCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
