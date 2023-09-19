package spring.boot.bookstore.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import spring.boot.bookstore.validation.book.isbn.Isbn;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Length(min = 4, max = 255)
    private String title;
    @NotNull
    private String author;
    @Isbn
    private String isbn;
    @Min(0)
    private BigDecimal price;
    private String description;
    private String coverImage;
    @NotNull
    private Set<Long> categoryIds;
}
