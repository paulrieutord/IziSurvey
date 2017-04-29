package com.udp.paul.izisurvey.model;

public class User {
    private String name;
    private String lastName;
    private String userName;
    private String email;

    public User (){
    }

    public User (String name, String lastName, String userName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }
}