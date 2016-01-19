package no.bouvet.sandvika.myfriends.rest.model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by sondre.engell on 19.01.2016.
 */

@Entity
public class User {

    @Id
    private String userName;
    private String firstName;
    private String lastName;

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
