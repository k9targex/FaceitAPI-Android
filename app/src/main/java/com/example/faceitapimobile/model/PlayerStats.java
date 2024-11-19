package com.example.faceitapimobile.model;

import com.google.gson.annotations.SerializedName; // Импортируем нужные аннотации Gson

public class PlayerStats {

    @SerializedName("lifetime")
    private LifetimeStats lifetime;

    // Конструктор без параметров
    public PlayerStats() {
    }

    // Конструктор с параметрами
    public PlayerStats(LifetimeStats lifetime) {
        this.lifetime = lifetime;
    }

    // Геттеры и сеттеры
    public LifetimeStats getLifetime() {
        return lifetime;
    }

    public void setLifetime(LifetimeStats lifetime) {
        this.lifetime = lifetime;
    }
}