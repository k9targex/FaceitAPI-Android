package com.example.faceitapimobile.model;

import com.google.gson.annotations.SerializedName;

public class Game {
    @SerializedName("name")
    private String name;

    @SerializedName("skill_level")
    private String skillLevel;

    // Конструктор без параметров
    public Game() {}

    // Конструктор с параметрами
    public Game(String name, String skillLevel) {
        this.name = name;
        this.skillLevel = skillLevel;
    }

    // Геттер для name
    public String getName() {
        return name;
    }

    // Сеттер для name
    public void setName(String name) {
        this.name = name;
    }

    // Геттер для skillLevel
    public String getSkillLevel() {
        return skillLevel;
    }

    // Сеттер для skillLevel
    public void setSkillLevel(String skillLevel) {
        this.skillLevel = skillLevel;
    }

    @Override
    public String toString() {
        return "Game{" +
                "name='" + name + '\'' +
                ", skillLevel='" + skillLevel + '\'' +
                '}';
    }
}
