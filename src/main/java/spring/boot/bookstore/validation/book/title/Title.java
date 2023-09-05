package spring.boot.bookstore.validation.book.title;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TitleValidation.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {
    String message() default "Invalid Title format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
