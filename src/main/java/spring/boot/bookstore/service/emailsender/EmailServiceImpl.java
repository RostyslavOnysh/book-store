package spring.boot.bookstore.service.emailsender;

import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import spring.boot.bookstore.exception.MessageSenderException;
import spring.boot.bookstore.model.Order;

@Service
public class EmailServiceImpl implements EmailService {
    @Value("${mail.smtp.host}")
    private String smtpHost;
    @Value("${mail.smtp.port}")
    private int smtpPort;
    @Value("${mail.smtp.auth}")
    private String auth;
    @Value("${mail.smtp.starttls.enable}")
    private String starttlsEnable;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;
    @Value("${image.path.pending}")
    private String imagePathPending;
    @Value("${image.path.completed}")
    private String imagePathCompleted;
    @Value("${image.path.delivered}")
    private String imagePathDelivered;

    @Override
    public void sendStatusChangeEmail(String userEmail, Order.Status newStatus) {
        try {
            Session session = prepareSession();
            MimeMessage message = createMessage(session, userEmail, newStatus);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MessageSenderException("Error sending email....", e);
        }
    }

    private Session prepareSession() {
        Properties properties = prepareProperties();
        return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    private Properties prepareProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.auth", auth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        return properties;
    }

    private MimeMessage createMessage(Session session, String userEmail, Order.Status newStatus)
            throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
        message.setSubject("Order Status Change ! ");
        Multipart multipart = createMultipart(newStatus);
        message.setContent(multipart);
        return message;
    }

    private Multipart createMultipart(Order.Status newStatus)
            throws MessagingException {
        Multipart multipart = new MimeMultipart();
        addTextPart(multipart, newStatus);
        addImagePart(multipart, newStatus);
        return multipart;
    }

    private void addTextPart(Multipart multipart, Order.Status newStatus)
            throws MessagingException {
        BodyPart textPart = new MimeBodyPart();
        String emailText = "The status of your order has been changed to : " + newStatus;
        textPart.setText(emailText);
        multipart.addBodyPart(textPart);
    }

    private void addImagePart(Multipart multipart, Order.Status newStatus)
            throws MessagingException {
        BodyPart imagePart = new MimeBodyPart();
        String imagePath = getImagePathForStatus(newStatus);
        DataSource source = new FileDataSource(imagePath);
        imagePart.setDataHandler(new DataHandler(source));
        imagePart.setFileName("image.jpg");
        multipart.addBodyPart(imagePart);
    }

    private String getImagePathForStatus(Order.Status status) {
        return switch (status) {
            case COMPLETED -> imagePathCompleted;
            case DELIVERED -> imagePathDelivered;
            default -> imagePathPending;
        };
    }
}
