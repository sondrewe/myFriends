package no.bouvet.sandvika.myfriends.rest.notifier;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import no.bouvet.sandvika.myfriends.rest.model.User;
import no.bouvet.sandvika.myfriends.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProximityNotifier extends Notifier {

    Logger log = LoggerFactory.getLogger(ProximityNotifier.class);

    public void sendNotification(User receiver, User user) {
        Sender sender = new Sender(apiKey);
        Message message = new Message.Builder()
                .addData("type", "PROXIMITY_NOTIFICATION")
                .addData("message", "You are now close to " + user.getFirstName() + " " + user.getLastName())
                .addData("userName", user.getUserName())
                .addData("proximity", proximity)
                .build();
        try {
            Result result = sender.send(message, receiver.getRegId(), 3);
            log.info("Sent notification to " + receiver.getUserName() + " about " + user.getUserName()
                    + " with result " + result.toString());
        } catch (IOException ioe) {
            log.error("Failed to send notification to " + receiver.getUserName() + ". \n" + ioe.getMessage());
        }
    }

    public void sendDummyNotification(String userName) {
        User user = userRepository.findByUserName(userName);
        User dummyUser = new User();
        dummyUser.setUserName("dummy");
        dummyUser.setFirstName("Jim");
        dummyUser.setLastName("Dummy");
        sendNotification(user, dummyUser);
    }
}
