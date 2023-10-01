package spring.boot.bookstore.service.user;

import spring.boot.bookstore.dto.user.UserRegistrationRequestDto;
import spring.boot.bookstore.dto.user.UserResponseDto;
import spring.boot.bookstore.exception.RegistrationException;
import spring.boot.bookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    User getAuthenticated();
}
