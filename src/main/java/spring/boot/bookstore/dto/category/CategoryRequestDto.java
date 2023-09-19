package spring.boot.bookstore.dto.category;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryRequestDto {
    @NotNull
    @Length(min = 4, max = 255)
    private String name;
    @Length(max = 255)
    private String description;
}
