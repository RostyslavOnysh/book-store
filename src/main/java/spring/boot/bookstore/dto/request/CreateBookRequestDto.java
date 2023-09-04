package spring.boot.bookstore.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import spring.boot.bookstore.validation.book.author.Author;
import spring.boot.bookstore.validation.book.isbn.Isbn;
import spring.boot.bookstore.validation.book.title.Title;

@Data
public class CreateBookRequestDto {
    @NotNull
    @Title
    private String title;
    @NotNull
    @Author
    private String author;
    @Isbn
    private String isbn;
    @Min(0)
    private BigDecimal price;

    private String description;

    private String coverImage;

}
