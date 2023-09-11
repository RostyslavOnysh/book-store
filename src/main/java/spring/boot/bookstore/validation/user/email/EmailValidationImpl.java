package spring.boot.bookstore.validation.user.email;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmailValidationImpl implements ConstraintValidator<EmailValidation, String> {
    private static final String PATTERN_OF_EMAIL = "^(.+)@(\\S+)$";
    private static final Logger logger = LogManager.getLogger(EmailValidationImpl.class);

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValid = email.length() >= 6
                && Pattern.compile(PATTERN_OF_EMAIL).matcher(email).matches();
        if (!isValid) {
            logger.warn("Invalid email format: {}", email);
        }
        return isValid;
    }
}



