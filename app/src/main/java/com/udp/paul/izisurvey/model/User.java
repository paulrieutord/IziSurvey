package com.udp.paul.izisurvey.model;

public class User {
    private String name;
    private String lastName;
    private String userName;
    private String email;
    private long createdAt;

    public User (){
    }

    public User (String name, String lastName, String userName, String email, long createdAt) {
        this.name = name;
        this.lastName = lastName;
        this.userName = userName;
        this.email = email;
        this.createdAt = createdAt;
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

    public long getCreatedAt() {
        return createdAt;
    }
}