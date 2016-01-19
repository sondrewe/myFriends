package no.bouvet.sandvika.myfriends.rest.model;

import org.springframework.data.annotation.Id;

import java.util.List;

/**
 * Created by sondre.engell on 19.01.2016.
 */

public class User {

    private String userName;
    private String firstName;
    private String lastName;

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
}
