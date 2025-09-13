package com.example.TaskManager;

import com.example.TaskManager.dto.RegisterRequest;
import com.example.TaskManager.repository.UserRepository;
import com.example.TaskManager.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static com.example.TaskManager.enums.Role.ADMIN;
import static com.example.TaskManager.enums.Role.MANAGER;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableMethodSecurity
public class TaskManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskManagerApplication.class, args);
	}

    @Bean
    public CommandLineRunner commandLineRunner(AuthenticationService service, UserRepository userRepository) {
        return args -> {
            if (userRepository.findByEmailAddress("admin@mail.com").isEmpty()) {
                var admin = RegisterRequest.builder()
                        .firstname("Admin")
                        .lastname("Admin")
                        .email("admin@mail.com")
                        .password("password")
                        .role(ADMIN)
                        .build();
                System.out.println("Admin token: " + service.register(admin).getAccessToken());
            }
            else {
                System.out.println("Admin user already exists!");
            }

            if (userRepository.findByEmailAddress("manager@mail.com").isEmpty()) {
                var manager = RegisterRequest.builder()
                        .firstname("Manager")
                        .lastname("Manager")
                        .email("manager@mail.com")
                        .password("password")
                        .role(MANAGER)
                        .build();
                System.out.println("Manager token: " + service.register(manager).getAccessToken());
            } else {
                System.out.println("Manager user already exists!");
            }
        };
    }
}