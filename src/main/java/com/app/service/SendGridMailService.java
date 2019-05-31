package com.app.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.app.model.User;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

@Service
public class SendGridMailService {

    SendGrid sendGrid;

    public SendGridMailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void ConfirmAccountMail(User User, String verificationCode, String url) {
        Email from = new Email("do-not-reply@bookswap.com");
        String subject = "Confirm account creation";
        Email to = new Email(User.getEmail());
        Content content = new Content("text/plain", "please, click on the link to confirm your registration : " + url + "/confirm/" + verificationCode);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = this.sendGrid.api(request);
        } catch (IOException ex) {
        }
    }
}