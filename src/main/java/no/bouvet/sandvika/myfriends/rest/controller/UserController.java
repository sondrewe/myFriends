package no.bouvet.sandvika.myfriends.rest.controller;

import no.bouvet.sandvika.myfriends.rest.model.User;
import no.bouvet.sandvika.myfriends.rest.notifier.ProximityNotifier;
import no.bouvet.sandvika.myfriends.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sondre.engell on 20.01.2016.
 */

@RestController
public class UserController {

    Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProximityNotifier notifier;

    @Value("${proximity_km}")
    String proximity;


    @RequestMapping(value = "/user/{id}/nearbyFriends", method = RequestMethod.GET)
    public List<User> getNearbyUsers(@PathVariable("id") String userName) {
        log.info("Fetching nearby user for " + userName);
        double[] currPos = userRepository.findByUserName(userName).getPosition();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);

        return userRepository.findByPositionNear(new Point(currPos[0], currPos[1]), new Distance(Double.parseDouble(proximity), Metrics.KILOMETERS))
                .stream()
                .filter(u -> u.getLastUpdate().after(cal.getTime())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/user/{id}/updatePosition", method = RequestMethod.POST)
    public User updatePosition(@PathVariable("id") String userName, @RequestParam(name="position") double[] position) {
        log.info("Updating position for user " + userName + " to " + position[0] + "," + position[1]);
        User user = userRepository.findByUserName(userName);
        user.setPosition(position);
        user.setLastUpdate(new Date());
        updateNearbyFriends(user);
        userRepository.save(user);
        return user;
    }

    @RequestMapping(value = "/user/{id}/addFriend", method = RequestMethod.POST)
    public User addFriend(@PathVariable("id") String userName, @RequestParam(name="friend") String friend) {
        User user = userRepository.findByUserName(userName);
        user.addFriend(friend);
        userRepository.save(user);
        return user;
    }

    /**
     * This method does way more than it should. Refactor!
     */
    private void updateNearbyFriends(User user) {
        List<User> currentNearby = userRepository.findByPositionNear(user.getPositionAsPoint(), new Distance(Double.parseDouble(proximity), Metrics.KILOMETERS))
                .stream()
                .filter(u -> user.getFriends().contains(u.getUserName()))
                .collect(Collectors.toList());
        List<User> remove = findRemovedFriends(user, currentNearby);
        List<User> add = findAddedFriends(user, currentNearby);
        // Remove current user from list of nearby friends of all removed friends
        remove.stream().forEach(u -> u.removeNearbyFriend(user));
        userRepository.save(remove);
        // Add current user to list of nearby friends of all added friends
        add.stream().filter(us1 -> us1.getFriends().contains(user.getUserName())).forEach(u -> u.addNearbyFriend(user));
        userRepository.save(add);
        // Update current users list of nearby friends
        user.setNearByFriends(currentNearby
                .stream()
                .map(User::getUserName)
                .collect(Collectors.toSet()));
        // Notify added nearby friends about current user in proximity
        notifier.notifyListAboutUser(add
                .stream()
                .filter(us1 -> us1.getFriends().contains(user.getUserName()))
                .collect(Collectors.toList()),user);
        // Notify current user about added friends in proximity
        notifier.notifyUserAboutList(user, add);
    }

    private List<User> findAddedFriends(User user, List<User> currentNearby) {
        return currentNearby
                .stream()
                .filter(u -> !user.getNearByFriends().contains(u.getUserName()))
                .collect(Collectors.toList());
    }

    private List<User> findRemovedFriends(User user, List<User> currentNearby) {
        return user.getNearByFriends()
                .stream()
                .map(userRepository::findByUserName)
                .filter(u -> !currentNearby.contains(u.getUserName()))
                .collect(Collectors.toList());
    }
}
