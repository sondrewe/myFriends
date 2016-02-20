package no.bouvet.sandvika.myfriends.rest.notifier;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import no.bouvet.sandvika.myfriends.rest.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PositionNotifier extends Notifier {

    Logger log = LoggerFactory.getLogger(PositionNotifier.class);

     public void sendNotification(User receiver, User user) {
        Sender sender = new Sender(apiKey);
        Message message = new Message.Builder()
                .addData("type", "POSITION_NOTIFICATION")
                .addData("position", user.getPosition()[0] + "," + user.getPosition()[1])
                .build();
        try {
            Result result = sender.send(message, receiver.getRegId(), 3);
            log.info("Sent position notification to " + receiver.getUserName() + " about " + user.getUserName() +
                    " [" + user.getPosition()[0] + "," + user.getPosition()[1] + "]"
                    + " with result " + result.toString());
        } catch (IOException ioe) {
            log.error("Failed to send notification to " + receiver.getUserName() + ". \n" + ioe.getMessage());
        }
    }

}
