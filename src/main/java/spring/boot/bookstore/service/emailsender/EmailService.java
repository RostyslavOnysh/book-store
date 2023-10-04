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
public class EmailService {
    private String emailUsername = "rospsix@gmail.com";
    private String emailPassword = "boujgexfjbdhzkvn";

    @Value("${image.path.pending}")
    private String imagePathPending;

    @Value("${image.path.completed}")
    private String imagePathCompleted;

    @Value("${image.path.delivered}")
    private String imagePathDelivered;

    public void sendStatusChangeEmail(String userEmail, Order.Status newStatus) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailUsername, emailPassword);
                    }
                }
        );
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailUsername));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            message.setSubject("Order Status Change ! ");
            Multipart multipart = new MimeMultipart();
            BodyPart textPart = new MimeBodyPart();
            String emailText = "The status of your order has been changed to : " + newStatus;
            textPart.setText(emailText);
            multipart.addBodyPart(textPart);
            BodyPart imagePart = new MimeBodyPart();
            String imagePath = null;
            switch (newStatus) {
                case PENDING:
                    imagePath = imagePathPending;
                    break;
                case COMPLETED:
                    imagePath = imagePathCompleted;
                    break;
                case DELIVERED:
                    imagePath = imagePathDelivered;
                    break;
                default: imagePath = imagePathPending;
            }
            DataSource source = new FileDataSource(imagePath);
            imagePart.setDataHandler(new DataHandler(source));
            imagePart.setFileName("image.jpg");
            multipart.addBodyPart(imagePart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MessageSenderException("Error sending email....", e);
        }
    }
}
