package no.bouvet.sandvika.myfriends.rest.controller;

import no.bouvet.sandvika.myfriends.rest.model.User;
import no.bouvet.sandvika.myfriends.rest.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value = "/user/{id}/nearbyFriends", method = RequestMethod.GET)
    public List<User> getNearbyUsers(@PathVariable("id") String userName) {
        log.info("Fetching nearby user for " + userName);
        double[] currPos = userRepository.findByUserName(userName).getPosition();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -1);

        return userRepository.findByPositionNear(new Point(currPos[0], currPos[1]), new Distance(1, Metrics.KILOMETERS))
                .stream()
                .filter(u -> u.getLastUpdate().after(cal.getTime())).collect(Collectors.toList());
    }

    @RequestMapping(value = "/user/{id}/updatePosition", method = RequestMethod.POST)
    public User updatePosition(@PathVariable("id") String userName, @RequestParam(name="position") double[] position) {
        log.info("Updating position for user " + userName + " to " + position[0] + "," + position[1]);
        User user = userRepository.findByUserName(userName);
        user.setPosition(position);
        user.setLastUpdate(new Date());
        userRepository.save(user);
        return user;
    }
}
