package spring.boot.bookstore.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.boot.bookstore.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(Role.RoleName roleName);
}
