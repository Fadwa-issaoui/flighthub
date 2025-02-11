package com.example.flighthub.controllers.booking;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    private static final String SMTP_HOST = "smtp-relay.brevo.com"; // Brevo SMTP server
    private static final String SMTP_PORT = "587";
    private static final String SENDER_EMAIL = "flighthub.rh@outlook.com"; // Your Outlook email
    private static final String SENDER_PASSWORD = "FlightHubcontact6&"; // Use an app password if needed

    public static boolean sendBookingEmail(String recipientEmail, String passengerName, String flightNumber, String departure, String arrival, String date, boolean isHtml) {
        String host = "smtp-relay.brevo.com";
        final String username = "856e0e001@smtp-brevo.com"; // Your Brevo SMTP username
        final String password = "6LR2vqYIsXw5rfJA"; // Your Brevo SMTP password

        // Set properties for the mail session
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", host);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Flight Booking Confirmation: " + flightNumber);

            String emailContent;
            if (isHtml) {
                // Enhanced HTML email content with logo and brand colors
                emailContent = "<html>" +
                        "<head>" +
                        "<style>" +
                        "body { font-family: 'Arial', sans-serif; color: #333; line-height: 1.6; background-color: #f4f4f9; margin: 0; padding: 0; }" +
                        ".container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); }" +
                        ".header { text-align: center; background-color: #5067E9; color: white; padding: 15px; border-radius: 8px 8px 0 0; }" +
                        ".header img { max-width: 150px; height: auto; }" +
                        ".content { margin-top: 20px; }" +
                        ".footer { text-align: center; margin-top: 20px; font-size: 0.9em; color: #777; }" +
                        "ul { list-style: none; padding: 0; }" +
                        "li { padding: 5px 0; }" +
                        ".highlight { font-weight: bold; color: #5067E9; }" +
                        "</style>" +
                        "</head>" +
                        "<body>" +
                        "<div class='container'>" +
                        "<div class='header'>" +
                        "<img src='cid:logo' alt='FlightHub Logo' />" +
                        "<h2>Flight Booking Confirmation</h2>" +
                        "</div>" +
                        "<div class='content'>" +
                        "<p>Dear <span class='highlight'>" + passengerName + "</span>,</p>" +
                        "<p>We are excited to confirm your flight details as follows:</p>" +
                        "<ul>" +
                        "<li><span class='highlight'>Flight Number:</span> " + flightNumber + "</li>" +
                        "<li><span class='highlight'>From:</span> " + departure + "</li>" +
                        "<li><span class='highlight'>To:</span> " + arrival + "</li>" +
                        "<li><span class='highlight'>Date:</span> " + date + "</li>" +
                        "</ul>" +
                        "<p>Thank you for choosing our airline. We look forward to serving you!</p>" +
                        "</div>" +
                        "<div class='footer'>" +
                        "<p>Safe travels, and don't hesitate to contact us for any further assistance.</p>" +
                        "<p><em>FlightHub Team</em></p>" +
                        "</div>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
                message.setContent(emailContent, "text/html");

                // Add logo as an inline attachment
                MimeBodyPart logoPart = new MimeBodyPart();
                DataSource fds = new FileDataSource("C:/Users/maiss/flighthub/src/main/resources/images/flighthubIcone.png");
                logoPart.setDataHandler(new DataHandler(fds));
                logoPart.setHeader("Content-ID", "<logo>");

                // Create the multipart message
                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(logoPart);
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setContent(emailContent, "text/html");
                multipart.addBodyPart(messageBodyPart);

                message.setContent(multipart);

            } else {
                // Plain text email content
                emailContent = String.format(
                        "Dear %s,\n\nYour flight details:\nFlight: %s\nDeparture: %s\nArrival: %s\nDate: %s\n\nSafe travels!",
                        passengerName, flightNumber, departure, arrival, date);
                message.setText(emailContent);
            }

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            System.out.println("Failed to send email to " + recipientEmail);
            e.printStackTrace();
            return false;
        }
    }
}
