package no.bouvet.sandvika.myfriends.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * Created by sondre.engell on 19.01.2016.
 */

@Document(collection = "users")
public class User {

    private String userName;
    private String firstName;
    private String lastName;
    private Date lastUpdate;
    @GeoSpatialIndexed
    private double[] position;
    private String regId;
    private Set<String> friends;
    private Set<String> nearByFriends;

    @Id
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double[] getPosition() {
        return position;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @JsonIgnore
    public Point getPositionAsPoint() {
        if (position == null) {
            position = new double[]{0,0};
        }
        return new Point(position[0], position[1]);
    }

    public Set<String> getFriends() {
        if (this.friends == null) {
            this.friends = new HashSet<>();
        }
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public void addFriend(String userName) {
        if (this.friends == null) {
            this.friends = new HashSet<>();
        }
        this.friends.add(userName);
    }

    public Set<String> getNearByFriends() {
        if (this.nearByFriends == null) {
            this.nearByFriends = new HashSet<>();
        }
        return nearByFriends;
    }

    public void setNearByFriends(Set<String> nearByFriends) {
        this.nearByFriends = nearByFriends;
    }

    public void addNearbyFriend(User friend) {
        if (this.nearByFriends == null) {
            this.nearByFriends = new HashSet<>();
        }
        this.nearByFriends.add(friend.getUserName());
    }

    public void removeNearbyFriend(User friend) {
        if (this.nearByFriends == null) {
            return;
        }
        this.nearByFriends.remove(friend.getUserName());
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", position=" + Arrays.toString(position) +
                ", regId='" + regId + '\'' +
                ", friends=" + friends +
                ", nearByFriends=" + nearByFriends +
                '}';
    }
}
