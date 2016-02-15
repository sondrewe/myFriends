package no.bouvet.sandvika.myfriends.rest.eventhandler;

import no.bouvet.sandvika.myfriends.rest.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by sondre.engell on 20.01.2016.
 */

@Component
@RepositoryEventHandler(User.class)
public class UserEventHandler {
    Logger log = LoggerFactory.getLogger(UserEventHandler.class);

    @HandleBeforeCreate
    public void handleBeforeCreate(User user) {
        user.setLastUpdate(new Date());
    }

    @HandleAfterCreate
    public void handleAfterCreate(User user) {
        log.info("Saved user:" + user.toString());
    }

}
