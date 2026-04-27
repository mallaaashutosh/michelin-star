package com.restaurant.entity;

import java.sql.Timestamp;

public class User {

    private int id;
    private String name;
    private String phoneNumber;
    private String email;
    private String password;
    private String profileImage;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public User(String name, String phoneNumber, String email, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
    }


    public User(int id, String name, String phoneNumber, String email,
                String password, String profileImage, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getProfileImage() { return profileImage; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }


    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }


    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}