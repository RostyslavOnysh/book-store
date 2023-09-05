package spring.boot.bookstore.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import spring.boot.bookstore.dto.response.BookDto;

@Data
@Entity
@SQLDelete(sql = "UPDATE books SET is_Deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@Getter
@Setter
@Table(name = "books")
public class Book extends BookDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String author;
    @Column(nullable = false, unique = true)
    private String isbn;
    @Column(nullable = false)
    private BigDecimal price;
    @Column
    private String description;
    @Column(name = "coverImage")
    private String coverImage;
    @Column(nullable = false)
    private boolean isDeleted = false;
}
