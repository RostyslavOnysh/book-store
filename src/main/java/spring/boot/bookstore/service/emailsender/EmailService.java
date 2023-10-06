package spring.boot.bookstore.service.emailsender;

import spring.boot.bookstore.model.Order;

public interface EmailService {
    void sendStatusChangeEmail(String userEmail, Order.Status newStatus);
}
