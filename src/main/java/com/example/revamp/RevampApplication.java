package com.example.revamp;

import com.example.revamp.Model.Role;
import com.example.revamp.Model.User;

import com.example.revamp.Service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

@SpringBootApplication
public class RevampApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevampApplication.class, args);

	}
	@Bean
	PasswordEncoder passwordEncoder() throws GeneralSecurityException, IOException {
		return new BCryptPasswordEncoder();
	}
	@Bean
	CommandLineRunner run (UserService userService){
		return args ->{

			userService.saveRole(new Role(null, "ROLE_USER"));
			userService.saveRole(new Role(null, "ROLE_PREMIUM"));
			userService.saveRole(new Role(null, "ROLE_ADMIN"));
			userService.saveRole(new Role(null, "ROLE_SUPER_ADMIN"));

			System.out.println();

			userService.saveUser(new User(null, "John Travolta", "john","1234", new ArrayList<>()));
			userService.saveUser(new User(null, "patricia volta", "patricia","1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Boula magora", "Boula","1234", new ArrayList<>()));
			userService.saveUser(new User(null, "Valentino poché", "valentino","1234", new ArrayList<>()));

			userService.addRoleToUser("john", "ROLE_USER");
			userService.addRoleToUser("patricia", "ROLE_PREMIUM");
			userService.addRoleToUser("Boula", "ROLE_ADMIN");
			userService.addRoleToUser("Boula", "ROLE_SUPER_ADMIN");
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:3000");
			}
		};
	}

}
