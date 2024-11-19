package com.example.faceitapimobile.model;

import com.google.gson.annotations.SerializedName; // Импортируем нужные аннотации Gson

public class PlayerInfoAndStats {

    @SerializedName("playerInfo")
    private PlayerInfo playerInfo;

    @SerializedName("playerStats")
    private PlayerStats playerStats;

    @SerializedName("skillLevel")
    private String skillLevel;

    // Конструктор без параметров
    public PlayerInfoAndStats() {
    }

    // Геттеры и сеттеры
    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public void setPlayerStats(PlayerStats playerStats) {
        this.playerStats = playerStats;
    }

    public String getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }
}