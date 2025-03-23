package com.example.petfoodstore_app.DTO.Login;

public class LoginResponse {
    private int id;
    private String email;
    private String name;
    private String token;

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getToken() {
        return token;
    }
}