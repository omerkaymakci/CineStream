package com.cinestream.auth_service.bootstrap;

import com.cinestream.auth_service.domain.Role;
import com.cinestream.auth_service.domain.User;
import com.cinestream.auth_service.repository.RoleRepository;
import com.cinestream.auth_service.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    ApplicationRunner initUsers(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder) {

        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            if (userRepository.findByUsername("admin").isEmpty()) {
                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin123"));
                user.setEnabled(true);
                user.getRoles().add(adminRole);
                userRepository.save(user);
            }
        };
    }

}

