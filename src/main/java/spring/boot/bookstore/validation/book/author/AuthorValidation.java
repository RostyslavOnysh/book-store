package spring.boot.bookstore.validation.book.author;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class AuthorValidation implements ConstraintValidator<Author, String> {

    private static final String AUTHOR_PATTERN =
            "^[\\p{L}\\d\\s'.,:;!?\\-–—‘’“”„\"«»&()\\[\\]{}<>@#$%^*_+=|\\\\/]+$";

    @Override
    public boolean isValid(String author, ConstraintValidatorContext constraintValidatorContext) {
        return author != null && Pattern.compile(AUTHOR_PATTERN).matcher(author).matches();
    }
}
