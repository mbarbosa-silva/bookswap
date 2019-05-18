package com.app.main;

import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@EntityScan("com.app.model")
@EnableJpaRepositories("com.app.repository")
@ComponentScan({
	"com.app.main",
	"com.app.controller",
	"com.app.service"
})
public class BookswapApplication {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init(){
        User user = new User(
                "Memory",
                "Not Found",
                "PEPE",
                passwordEncoder.encode("1234"),
                Arrays.asList(
                        new Role("ROLE_USER"),
                        new Role("ROLE_ADMIN")));

        if (userRepository.findByUsername(user.getUsername()) == null){
            userRepository.save(user);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(BookswapApplication.class, args);
    }
}
