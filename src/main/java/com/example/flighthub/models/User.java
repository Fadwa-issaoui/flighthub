package com.example.flighthub.models;

public class User {
    private int userId;
    private String username;
    private String password;
    private Role role;  // Add role here
    private String email;

    public User(){

    }

    public User(int userId, String name, String email, String password, Role role){
        this.userId=userId;
        this.username=name;
        this.email=email;
        this.password=password;
        this.role=role;
    }
    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getMail(){
        return email;
    }

    public void setMail(String email){
        this.email=email;
    }
}
