package utilities;

import java.util.Properties;
import java.io.File;

import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailUtils_Class {

    // ------------ SMTP Configuration ------------

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    // Sender Email
    private static final String USERNAME = "qatechwave@gmail.com";

    // Gmail App Password (NOT normal password)
    private static final String PASSWORD = "Techqa#12";

    // Client Email
    private static final String DEFAULT_RECIPIENT =
            "techwaveimplementation@gmail.com";

    // Mail Subject
    private static final String DEFAULT_SUBJECT =
            "Automation Execution Report";

    // Mail Body
    private static final String DEFAULT_BODY =
            "Hello,\n\nPlease find attached Selenium Automation Extent Report.\n\nRegards,\nQA Automation Team";

    // ------------ Extent Report Path ------------

    private static final String EXTENT_REPORT_PATH =
            System.getProperty("user.dir") + "/target/extent-report/ExtentReport.html";

    // ------------ Public Method ------------

    public static void sendExtentReportByEmail() {

        sendEmail(
                DEFAULT_RECIPIENT,
                DEFAULT_SUBJECT,
                DEFAULT_BODY,
                EXTENT_REPORT_PATH
        );
    }

    // ------------ Core Email Method ------------

    private static void sendEmail(String recipient, String subject,
                                  String body, String attachmentPath) {

        try {

            // SMTP properties
            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            // Create session
            Session session = Session.getInstance(props,
                    new Authenticator() {

                        protected PasswordAuthentication
                        getPasswordAuthentication() {

                            return new PasswordAuthentication(
                                    USERNAME,
                                    PASSWORD
                            );
                        }
                    });

            // Create message
            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(USERNAME));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );

            message.setSubject(subject);

            // -------- Mail Body --------

            MimeBodyPart messageBodyPart = new MimeBodyPart();

            messageBodyPart.setText(body);

            // -------- Attachment --------

            MimeBodyPart attachmentPart = new MimeBodyPart();

            File file = new File(attachmentPath);

            if (!file.exists()) {

                System.out.println(
                        "Extent Report not found at: "
                                + attachmentPath
                );

                return;
            }

            attachmentPart.attachFile(file);

            // -------- Multipart --------

            Multipart multipart = new MimeMultipart();

            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);

            message.setContent(multipart);

            // -------- Send Mail --------

            Transport.send(message);

            System.out.println(
                    "Extent Report emailed successfully to: "
                            + recipient
            );

        }

        catch (Exception e) {

            System.out.println(
                    "Failed to send email: "
                            + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}