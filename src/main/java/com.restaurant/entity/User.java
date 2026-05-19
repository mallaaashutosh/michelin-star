/*
 * Plain Java bean representing a row in the users table.
 * Holds account info (name, email, phone, password hash) plus role and status for admin vs customer.
 * DAOs map ResultSet columns into this object; servlets pass it around in the session.
 */
package com.restaurant.entity; // entity package for database-backed models

import java.sql.Timestamp; // matches created_at / updated_at columns from MySQL

public class User { // model for registered accounts

    private int id; // primary key from database
    private String name; // display name
    private String phoneNumber; // contact number
    private String email; // login identifier
    private String password; // stored hash, not plain text in production
    private String profileImage; // path or URL to avatar image
    private String role; // e.g. admin or customer
    private String status; // active, blocked, etc.
    private Timestamp createdAt; // when the account was created
    private Timestamp updatedAt; // last profile update time

    public User() {} // no-arg constructor for frameworks and manual field setting

    public User(String name, String phoneNumber, String email, String password) { // handy ctor for signup before we have an id
        this.name = name; // store display name
        this.phoneNumber = phoneNumber; // store phone
        this.email = email; // store email
        this.password = password; // store hashed password
    }


    public User(int id, String name, String phoneNumber, String email,
                String password, String profileImage, Timestamp createdAt, Timestamp updatedAt) { // full row from SELECT
        this.id = id; // set primary key
        this.name = name; // set display name
        this.phoneNumber = phoneNumber; // set phone
        this.email = email; // set email
        this.password = password; // set password hash
        this.profileImage = profileImage; // set avatar path
        this.createdAt = createdAt; // set created timestamp
        this.updatedAt = updatedAt; // set updated timestamp
    }


    public int getId() { return id; } // accessor for id
    public String getName() { return name; } // accessor for name
    public String getPhoneNumber() { return phoneNumber; } // accessor for phone
    public String getEmail() { return email; } // accessor for email
    public String getPassword() { return password; } // accessor for password hash
    public String getProfileImage() { return profileImage; } // accessor for profile image
    public String getRole() { return role; } // accessor for role
    public String getStatus() { return status; } // accessor for status
    public Timestamp getCreatedAt() { return createdAt; } // accessor for createdAt
    public Timestamp getUpdatedAt() { return updatedAt; } // accessor for updatedAt


    public void setId(int id) { this.id = id; } // mutator for id
    public void setName(String name) { this.name = name; } // mutator for name
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; } // mutator for phone
    public void setEmail(String email) { this.email = email; } // mutator for email
    public void setPassword(String password) { this.password = password; } // mutator for password
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; } // mutator for image
    public void setRole(String role) { this.role = role; } // mutator for role
    public void setStatus(String status) { this.status = status; } // mutator for status
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; } // mutator for createdAt

    public boolean isAdmin() { // quick check used by servlets to gate admin pages
        return "admin".equalsIgnoreCase(role); // true when role string is admin (any case)
    }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; } // mutator for updatedAt


    @Override
    public String toString() { // friendly label for logs and debugging
        return name + " (" + email + ")"; // e.g. Jane Doe (jane@example.com)
    }
}
