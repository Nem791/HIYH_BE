package com.example.demo.services;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VerificationEmailService {

    private final String apiKey;
    private final String senderEmail;
    private final String frontendUrl;

    public VerificationEmailService(
            @Value("${sendgrid.api.key}") String apiKey,
            @Value("${sendgrid.sender.email}") String senderEmail,
            @Value("${sendgrid.frontend.url}") String frontendUrl) {
        this.apiKey = apiKey;
        this.senderEmail = senderEmail;
        this.frontendUrl = frontendUrl;
    }

    public void sendVerificationEmail(String recipientEmail, String token) throws Exception {
        String subject = "Verify your account";
        String verificationLink = frontendUrl + "?token=" + token;

        String htmlContent = "<p>Click the link below to verify your account:</p>" +
                "<a href=\"" + verificationLink + "\">Verify Account</a>";

        Email from = new Email(senderEmail);
        Email to = new Email(recipientEmail);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        if (response.getStatusCode() != 202) {
            throw new RuntimeException("Failed to send verification email: " + response.getBody());
        }
    }

    public void sendVerificationCode(String recipientEmail, String code) throws Exception {
        String subject = "Your Verification Code";
        String htmlContent = "<p>Your verification code is: <strong>" + code + "</strong></p>";

        Email from = new Email(senderEmail);
        Email to = new Email(recipientEmail);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());

        Response response = sg.api(request);

        if (response.getStatusCode() != 202) {
            throw new RuntimeException("Failed to send verification code: " + response.getBody());
        }
    }

}