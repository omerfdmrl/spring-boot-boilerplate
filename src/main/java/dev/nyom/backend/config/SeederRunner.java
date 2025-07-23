package dev.nyom.backend.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import dev.nyom.backend.user.model.Permission;
import dev.nyom.backend.user.model.Role;
import dev.nyom.backend.user.model.User;
import dev.nyom.backend.user.repository.PermissionRepository;
import dev.nyom.backend.user.repository.RoleRepository;
import dev.nyom.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class SeederRunner implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            Permission userRead = permissionRepository.save(new Permission(null, "USER_READ"));
            Permission userWrite = permissionRepository.save(new Permission(null, "USER_WRITE"));

            Role adminRole = new Role(null, "ROLE_ADMIN", Set.of(userRead, userWrite));
            Role userRole = new Role(null, "ROLE_USER", Set.of(userRead));

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

            User admin = new User();
            admin.setName("Admin");
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder().encode("Admin@123"));
            admin.setRoles(Set.of(adminRole));

            userRepository.save(admin);

            System.out.println("✅ Admin user created: admin@example.com / Admin@123");

            User user = new User();
            user.setName("User");
            user.setUsername("user");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder().encode("User@123"));
            user.setRoles(Set.of(userRole));

            userRepository.save(user);

            System.out.println("✅ User user created: user@example.com / User@123");
        }
    }
}
