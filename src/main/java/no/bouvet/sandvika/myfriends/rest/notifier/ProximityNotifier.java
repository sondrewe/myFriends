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

/**
 * Created by sondre.engell on 08.02.2016.
 */

@Component
public class ProximityNotifier {

    Logger log = LoggerFactory.getLogger(ProximityNotifier.class);

    @Autowired
    UserRepository userRepository;

    @Value("${gcm.application_key}")
    String apiKey;

    @Value("${proximity_km}")
    String proximity;

    private void sendNotification(User receiver, User user) {
        Sender sender = new Sender(apiKey);
        Message message = new Message.Builder()
                .addData("message", "You are now close to " + user.getFirstName())
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

    public void notifyListAboutUser(List<User> recipientList, User user) {
        recipientList.stream()
                .forEach(recipient -> sendNotification(recipient, user));
    }

    public void notifyUserAboutList(User recipient, List<User> userList) {
        userList.stream()
                .forEach(user -> sendNotification(recipient, user));
    }
}
