package spring.boot.bookstore.validation.book.isbn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class IsbnValidation implements ConstraintValidator<Isbn, String> {

    private static final String ISBN_PATTERN = "^[0-9]+$";

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        return isbn != null && Pattern.compile(ISBN_PATTERN).matcher(isbn).matches();
    }
}

