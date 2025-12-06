package com.amrat.ECommerceApp.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async
    public void sendCredentials(String to, String username, String password){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("HMS App Credentials");
        mailMessage.setText(
                "Welcome!\n\n" +
                        "Your account has been created.\n\n" +
                        "Username: " + username + "\n" +
                        "Password: " + password + "\n\n" +
                        "Please log in and change your password."
        );

        mailSender.send(mailMessage);
    }

    @Async
    public void sendVerificationEmail(String to, String token) {

        try{
            MimeMessage mail = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(to);
            helper.setSubject("Verify Your Email");
            helper.setText(
                    "<html><body>" +
                            "<h3>Welcome to Hospital Management System</h3>" +
                            "<p>Click the button below to verify your email address:</p>" +
                            "<a href='http://your-verification-link/token?token={token}' " +
                            "style='padding:10px 20px;background-color:#4CAF50;color:white;text-decoration:none;border-radius:5px;'>" +
                            "Verify Email</a>" +
                            "</body></html>",
                    true // <--- this flag tells Spring to interpret text as HTML
            );
            mailSender.send(mail);
        } catch (MessagingException e){
            throw new RuntimeException("Issue in Sending Email");
        }
    }

}
