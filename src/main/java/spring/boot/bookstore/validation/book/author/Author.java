package spring.boot.bookstore.validation.book.author;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AuthorValidation.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Author {
    String message() default "Invalid Author Format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
