package spring.boot.bookstore.dto.order;

import lombok.Data;
import spring.boot.bookstore.model.Order;

@Data
public class OrderUpdateDto {
    private Order.Status status;
}
