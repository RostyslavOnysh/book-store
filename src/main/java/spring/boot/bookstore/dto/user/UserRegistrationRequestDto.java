package spring.boot.bookstore.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import spring.boot.bookstore.validation.user.email.EmailValidation;
import spring.boot.bookstore.validation.user.password.FieldMatch;

@Data
@FieldMatch(
        field = "password",
        fieldMatch = "repeatPassword",
        message = "Password and repeat password shouldn't be empty and should be equal"
)
public class UserRegistrationRequestDto {
    @EmailValidation
    private String email;
    @NotNull
    @Size(min = 6, max = 100)
    private String password;
    @NotNull
    @Size(min = 6, max = 100)
    private String repeatPassword;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    private String shippingAddress;
}

