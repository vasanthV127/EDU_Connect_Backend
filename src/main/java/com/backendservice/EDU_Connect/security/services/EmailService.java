package com.backendservice.EDU_Connect.security.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtpEmail(String to, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject("Password Reset OTP");
            helper.setText("Your OTP for password reset is: " + otp + "\nValid for 10 minutes.", true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    public void sendAnnouncementEmail(String to, String title, String content, String teacherName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates HTML support
            helper.setTo(to);
            helper.setSubject("New Announcement: " + title);

            // HTML-formatted email body with proper headings and line breaks
            String htmlBody = "<html><body>" +
                    "<p>Dear Student,</p>" +
                    "<p>A new announcement has been posted by " + teacherName + ":</p>" +
                    "<h2>Title: " + title + "</h2>" +
                    "<p><h2>Content:</h2> " + content + "</p>" +
                    "<p>Please check the EDU_Connect platform for more details.</p>" +
                    "<p>Best regards,<br>EDU_Connect Team</p>" +
                    "</body></html>";

            helper.setText(htmlBody, true); // true indicates the text is HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send announcement email", e);
        }


    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}