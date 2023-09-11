package spring.boot.bookstore.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import spring.boot.bookstore.validation.user.email.EmailValidation;

@Data
public class UserLoginRequestDto {
    @EmailValidation
    private String email;
    @NotNull
    @Length(min = 4, max = 155)
    private String password;
}
