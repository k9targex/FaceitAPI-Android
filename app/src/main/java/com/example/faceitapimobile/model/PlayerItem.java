package com.example.faceitapimobile.model;


import com.google.gson.annotations.SerializedName; // Импортируем нужные аннотации Gson
import java.util.List;

public class PlayerItem {

    @SerializedName("player_id")
    private String playerId;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("avatar")
    private String avatarUrl;

    @SerializedName("country")
    private String country;

    @SerializedName("games")
    private List<Game> games;

    // Конструктор без параметров
    public PlayerItem() {
    }

    // Конструктор с параметрами
    public PlayerItem(String playerId, String nickname, String avatarUrl, String country, List<Game> games) {
        this.playerId = playerId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.country = country;
        this.games = games;
    }

    // Геттеры и сеттеры
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }
    public String getSkillLevel() {
        if (games != null) {
            for (Game game : games) {
                if (game.getName().equals("cs2")) {
                    return game.getSkillLevel();
                }
            }
        }
        return "3";
    }
}