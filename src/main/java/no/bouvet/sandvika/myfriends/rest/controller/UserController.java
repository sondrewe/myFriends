package no.bouvet.sandvika.myfriends.rest.controller;

import no.bouvet.sandvika.myfriends.rest.model.User;
import no.bouvet.sandvika.myfriends.rest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sondre.engell on 20.01.2016.
 */

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/user/{id}/nearbyFriends", method = RequestMethod.GET)
    public List<User> getNearbyUsers(@PathVariable("id") String userName) {
        double[] currPos = userRepository.findByUserName(userName).getPosition();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, -1);

        return userRepository.findByPositionNear(new Point(currPos[0], currPos[1]), new Distance(1, Metrics.KILOMETERS))
                .stream()
                .filter(u -> u.getLastUpdate().after(cal.getTime())).collect(Collectors.toList());
    }
}
