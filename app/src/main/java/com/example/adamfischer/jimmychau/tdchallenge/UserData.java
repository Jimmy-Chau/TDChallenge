package com.example.adamfischer.jimmychau.tdchallenge;

import java.io.Serializable;

/**
 * Class to represent a user from the DB
 */
public class UserData implements Serializable {

    private long ID;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    public UserData(long ID, String userName, String password, String firstName, String lastName, String email) {
        this.ID = ID;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public long getID() {
        return ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
