package com.mfx.eventmanagement;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailSender {
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;
    private static final String USERNAME = "iminoka1977@gmail.com"; // Replace with your Gmail
    private static final String APP_PASSWORD = "gqzv vybq evcd fonc"; // Replace with App Password

    public static boolean sendVerificationEmail(String toEmail, String code) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Event Management App - Email Verification");
            message.setText("Hello,\n\nYour verification code is: " + code + "\n\nThis code expires in 10 minutes.\n\nBest,\nYour App Team");

            Transport.send(message);
            System.out.println("Email sent successfully to: " + toEmail); // For debugging
            return true;
        } catch (MessagingException e) {
            e.printStackTrace(); // In production, use logging (e.g., SLF4J)
            return false;
        }
    }
}