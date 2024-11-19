package com.example.faceitapimobile.model;

import java.util.List;

public class User {
    private String userId;
    private String email;
    private List<String> favoritePlayers; // Список любимых игроков, содержащий их идентификаторы или ники

    public User() {} // Конструктор по умолчанию для Firebase

    public User(String userId, String email, List<String> favoritePlayers) {
        this.userId = userId;
        this.email = email;
        this.favoritePlayers = favoritePlayers;
    }

    // Геттеры и сеттеры
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getFavoritePlayers() { return favoritePlayers; }
    public void setFavoritePlayers(List<String> favoritePlayers) { this.favoritePlayers = favoritePlayers; }
}
