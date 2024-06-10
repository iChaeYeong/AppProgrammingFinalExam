package com.example.finalexam0514;

public class User {
    private String username;
    private String email;
    private String petBreed;
    private String password;

    public User(String username, String email, String petBreed, String password) {
        this.username = username;
        this.email = email;
        this.petBreed = petBreed;
        this.password = password;
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

    public boolean verifyPassword(String password) {
        // Plain text password verification
        return this.password.equals(password);
    }
}