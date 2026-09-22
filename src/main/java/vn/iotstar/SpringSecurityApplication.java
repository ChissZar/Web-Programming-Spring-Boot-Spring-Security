package vn.iotstar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@SpringBootApplication
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner init(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            // Khởi tạo Role USER và ADMIN
            Role userRole = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ROLE_USER")
                                    .build()
                    ));

            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ROLE_ADMIN")
                                    .build()
                    ));

            // Khởi tạo User user01 (Ví dụ 2)
            userRepository.findByUsername("user01").ifPresentOrElse(
                    existingUser -> {
                        existingUser.setFullName("Nguyễn Phước Minh Triết");
                        userRepository.save(existingUser);
                    },
                    () -> {
                        User user = User.builder()
                                .username("user01")
                                .email("user01@gmail.com")
                                .password(passwordEncoder.encode("123456"))
                                .fullName("Nguyễn Phước Minh Triết")
                                .images("/images/user.png")
                                .role(userRole)
                                .enabled(true)
                                .build();
                        userRepository.save(user);
                    }
            );

            // Khởi tạo User admin (Ví dụ 1)
            if (userRepository.findByEmail("trungnh@hcmute.edu.vn").isEmpty()
                    && userRepository.findByUsername("admin").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("trungnh@hcmute.edu.vn")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("System Administrator")
                        .images("/images/user.png")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
