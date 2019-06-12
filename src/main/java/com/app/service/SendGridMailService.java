package com.app.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.app.model.User;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

@Service
public class SendGridMailService {

    SendGrid sendGrid;

    public SendGridMailService(SendGrid sendGrid) {
        this.sendGrid = sendGrid;
    }

    public void ConfirmAccountMail(User User, String verificationCode, String url) {
    	sendMail(User.getEmail(),
    			"please, click on the link to confirm your registration : ",
    			"Confirm account creation",
    			url + "/confirm/",
    			verificationCode);
    }
    
    public void ResetPasswordMail(User User, String verificationCode, String url) {
    	sendMail(User.getEmail(),
    			"please, click on the link to confirm your password change : ",
    			"password change",
    			url + "/",
    			verificationCode);
    }
    
    private void sendMail(String email,String message, String topic, String url, String verificationCode) {
        Email from = new Email("do-not-reply@bookswap.com");
        String subject = topic;
        Email to = new Email(email);
        Content content = new Content("text/plain",message  + url + verificationCode);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            this.sendGrid.api(request);
        } catch (IOException ex) {
        }
    }
    
    public void SendNewPassword(User User, String password) {
        Email from = new Email("do-not-reply@bookswap.com");
        String subject = "New Password";
        Email to = new Email(User.getEmail());
        Content content = new Content("text/plain","your new password : " + password);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            this.sendGrid.api(request);
        } catch (IOException ex) {
        }
    }
    
}