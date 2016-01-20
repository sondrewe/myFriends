package no.bouvet.sandvika.myfriends.rest.eventhandler;

import no.bouvet.sandvika.myfriends.rest.model.User;
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

    @HandleBeforeCreate
    public void handleBeforeCreate(User user) {
        user.setLastUpdate(new Date());
    }
}
