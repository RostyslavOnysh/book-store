package spring.boot.bookstore.validation.book.title;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class TitleValidation implements ConstraintValidator<Title, String> {

    private static final String PATTERN_OF_TITLE = "^[\\p{L}\\d\\s'.,:;!?-]+$";

    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        return title != null && Pattern.compile(PATTERN_OF_TITLE).matcher(title).matches();
    }
}
