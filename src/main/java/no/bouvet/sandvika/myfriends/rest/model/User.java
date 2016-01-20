package no.bouvet.sandvika.myfriends.rest.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

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

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
