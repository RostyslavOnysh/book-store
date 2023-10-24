package spring.boot.bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring.boot.bookstore.model.Role;
import spring.boot.bookstore.model.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {
    private static final String VALID_EMAIL = "test@example.com";
    @Autowired
    private UserRepository userRepository;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName(Role.RoleName.USER);
        user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword("password567$%^&");
        user.setFirstName("Bob");
        user.setLastName("User");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRoles(userRoles);
    }

    @Test
    @DisplayName("find user using valid data")
    void findUserByValidEmail_Success() {
        userRepository.save(user);
        Optional<User> foundUser = userRepository.findByEmail(VALID_EMAIL);
        assertThat(foundUser).isNotEmpty();
        assertThat(foundUser.get().getFirstName()).isEqualTo("Bob");
    }

    @Test
    @DisplayName("find user using invalid data")
    void findUserByNonExistentEmail() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Update user details")
    void updateUser() {
        userRepository.save(user);
        User updatedUser = userRepository.findByEmail(VALID_EMAIL).orElse(null);
        assertThat(updatedUser).isNotNull();
        updatedUser.setFirstName("UpdatedFirstName");
        userRepository.save(updatedUser);
        User retrievedUser = userRepository.findByEmail(VALID_EMAIL).orElse(null);
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getFirstName()).isEqualTo("UpdatedFirstName");
    }

    @Test
    @DisplayName("delete user from db")
    void deleteUser() {
        userRepository.save(user);
        User retrievedUser = userRepository.findByEmail(VALID_EMAIL).orElse(null);
        assertThat(retrievedUser).isNotNull();
        userRepository.delete(retrievedUser);
        User deletedUser = userRepository.findByEmail(VALID_EMAIL).orElse(null);
        assertThat(deletedUser).isNull();
    }
}
