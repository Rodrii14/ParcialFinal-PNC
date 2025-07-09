package com.uca.parcialfinalncapas.config;

import com.uca.parcialfinalncapas.entities.User;
import com.uca.parcialfinalncapas.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository UserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (UserRepository.count() == 0) {

            User tech = new User();
            tech.setNombre("tech-1");
            tech.setUsername("tech@gmail.com");
            tech.setPassword(passwordEncoder.encode("tech123"));
            tech.setNombreRol("TECH");
            UserRepository.save(tech);

            User app_user = new User();
            app_user.setNombre("user-1");
            app_user.setUsername("user@gmail.com");
            app_user.setPassword(passwordEncoder.encode("user123"));
            app_user.setNombreRol("USER");
            UserRepository.save(app_user);
        }
    }
}
