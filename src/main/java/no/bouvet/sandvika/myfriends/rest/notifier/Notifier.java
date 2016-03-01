package no.bouvet.sandvika.myfriends.rest.notifier;

import no.bouvet.sandvika.myfriends.rest.model.User;
import no.bouvet.sandvika.myfriends.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public abstract class Notifier {

    @Autowired
    UserRepository userRepository;

    @Value("${gcm.application_key}")
    String apiKey;

    @Value("${proximity_km}")
    String proximity;

    abstract void sendNotification(User receiver, User user);

    public void notifyListAboutUser(List<User> recipientList, User user) {
        recipientList.stream()
                .forEach(recipient -> sendNotification(recipient, user));
    }

    public void notifyUserAboutList(User recipient, List<User> userList) {
        userList.stream()
                .forEach(user -> sendNotification(recipient, user));
    }

}
