package com.rippletalk.rippletalk_backend;

import java.util.HashSet;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.rippletalk.rippletalk_backend.config.RSAKeyProperties;
import com.rippletalk.rippletalk_backend.models.ApplicationUser;
import com.rippletalk.rippletalk_backend.models.Role;
import com.rippletalk.rippletalk_backend.repositories.RoleRepository;
import com.rippletalk.rippletalk_backend.repositories.UserRepository;

@EnableConfigurationProperties(RSAKeyProperties.class)
@SpringBootApplication
public class RippletalkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RippletalkBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			Role r = roleRepo.save(new Role(1, "USER"));

			Set<Role> roles = new HashSet<>();

			roles.add(r);

			ApplicationUser u1 = new ApplicationUser();
			u1.setAuthorities(roles);
			u1.setFirstName("Yash");
			u1.setLastName("Suri");
			u1.setEmail("yashsuri1999@gmail.com");
			u1.setUsername("phenom");
			u1.setPassword(passwordEncoder.encode("Raj4yash"));
			u1.setEnabled(true);

			ApplicationUser u2 = new ApplicationUser();
			u2.setAuthorities(roles);
			u2.setFirstName("Follower1");
			u2.setLastName("test");
			u2.setEmail("test@gmail.com");
			u2.setUsername("test1");
			u2.setPassword(passwordEncoder.encode("Test123"));
			u2.setEnabled(true);

			userRepository.save(u1);
			userRepository.save(u2);
		};
	}

}
