package no.bouvet.sandvika.myfriends.rest;

import no.bouvet.sandvika.myfriends.rest.eventhandler.UserEventHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by sondre.engell on 19.01.2016.
 */

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    UserEventHandler userEventHandler() {
        return new UserEventHandler();
    }
}
