package com.rippletalk.rippletalk_backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.rippletalk.rippletalk_backend.models.Role;
import com.rippletalk.rippletalk_backend.repositories.RoleRepository;
import com.rippletalk.rippletalk_backend.repositories.UserRepository;

@SpringBootApplication
public class RippletalkBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(RippletalkBackendApplication.class, args);
	}

	@Bean
	CommandLineRunner run(RoleRepository roleRepo, UserRepository userRepo) {
		return args -> {
			roleRepo.save(new Role(1, "User"));
			// ApplicationUser u = new ApplicationUser();
			// u.setFirstName("Yash");
			// u.setLastName("Suri");
			// HashSet<Role> roles = new HashSet<>();
			// roles.add(roleRepo.findByAuthority("User").get());
			// u.setAuthorities(roles);
			// userRepo.save(u);
		};
	}

}
