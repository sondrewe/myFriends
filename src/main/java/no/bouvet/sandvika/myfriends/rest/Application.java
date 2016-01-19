package no.bouvet.sandvika.myfriends.rest;

import no.bouvet.sandvika.myfriends.rest.validator.UserValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by sondre.engell on 19.01.2016.
 */

@SpringBootApplication
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "beforeCreateUserValidator")
    UserValidator userValidator() {
        return new UserValidator();
    }

}
