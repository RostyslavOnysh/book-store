package spring.boot.bookstore.mapper;

import org.mapstruct.Mapper;
import spring.boot.bookstore.config.MapperConfig;
import spring.boot.bookstore.dto.user.UserRegistrationRequestDto;
import spring.boot.bookstore.dto.user.UserResponseDto;
import spring.boot.bookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto userRequestDto);
}
