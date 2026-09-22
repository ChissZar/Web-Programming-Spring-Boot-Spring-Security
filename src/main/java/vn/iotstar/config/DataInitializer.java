package vn.iotstar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roles,
            UserRepository users,
            PasswordEncoder encoder,
            @Value("${ADMIN_EMAIL:trungnh@hcmute.edu.vn}") String adminEmail,
            @Value("${ADMIN_PASSWORD:123456}") String adminPassword) {
        return args -> {
            Role userRole = roles.findByNameIgnoreCase("ROLE_USER")
                    .or(() -> roles.findByNameIgnoreCase("USER"))
                    .orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));

            Role adminRole = roles.findByNameIgnoreCase("ROLE_ADMIN")
                    .or(() -> roles.findByNameIgnoreCase("ADMIN"))
                    .orElseGet(() -> roles.save(Role.builder().name("ROLE_ADMIN").build()));

            if (!users.existsByEmailIgnoreCase(adminEmail)) {
                User admin = User.builder()
                        .username("admin")
                        .email(adminEmail.toLowerCase())
                        .fullName("System Administrator")
                        .password(encoder.encode(adminPassword))
                        .images("/images/user.png")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                users.save(admin);
            }

            users.findByUsername("user01").ifPresentOrElse(
                    existingUser -> {
                        existingUser.setFullName("Nguyễn Phước Minh Triết");
                        users.save(existingUser);
                    },
                    () -> {
                        User user = User.builder()
                                .username("user01")
                                .email("user01@gmail.com")
                                .password(encoder.encode("123456"))
                                .fullName("Nguyễn Phước Minh Triết")
                                .images("/images/user.png")
                                .role(userRole)
                                .enabled(true)
                                .build();
                        users.save(user);
                    });
        };
    }
}
