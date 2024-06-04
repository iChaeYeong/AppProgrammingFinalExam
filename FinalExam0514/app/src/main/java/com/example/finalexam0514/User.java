package com.example.finalexam0514;

public class User {
    private String username;
    private String email;
    private String petBreed;

    public User(String username, String email, String petBreed) {
        this.username = username;
        this.email = email;
        this.petBreed = petBreed;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPetBreed() {
        return petBreed;
    }
}