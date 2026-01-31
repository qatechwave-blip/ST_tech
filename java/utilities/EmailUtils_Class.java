package utilities;


import java.util.Properties;

import java.io.File;
import jakarta.mail.*;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

public class EmailUtils_Class {

    // Default email configuration (update with real SMTP)
    private static final String SMTP_HOST = "smtp.yourcompany.com";
    private static final String SMTP_PORT = "587";
    private static final String USERNAME = "automation@yourcompany.com";
    private static final String PASSWORD = "yourpassword";

    private static final String DEFAULT_RECIPIENT = "team@yourcompany.com";
    private static final String DEFAULT_SUBJECT = "Automation Test Extent Report";
    private static final String DEFAULT_BODY = "Please find the attached Extent Report.";

    // Path to Extent report (update according to your project)
    private static final String EXTENT_REPORT_PATH = "test-output/ExtentReport.html";

    /**
     * Send Extent report by email to default recipient.
     */
    public static void sendExtentReportByEmail() {
        sendEmail(DEFAULT_RECIPIENT, DEFAULT_SUBJECT, DEFAULT_BODY, EXTENT_REPORT_PATH);
    }

    /**
     * Core email sending method
     */
    private static void sendEmail(String recipient, String subject, String body, String attachmentPath) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);

            // Email body
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Attachment
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.attachFile(new File(attachmentPath));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Extent Report emailed successfully to: " + recipient);

        } catch (Exception e) {
            System.out.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
