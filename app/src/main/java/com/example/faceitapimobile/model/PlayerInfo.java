package com.example.faceitapimobile.model;

import com.google.gson.annotations.SerializedName; // Импортируем нужные аннотации Gson
import java.util.ArrayList;

public class PlayerInfo {

    @SerializedName("items")
    private ArrayList<PlayerItem> items; // Список объектов PlayerItem

    // Конструкторы
    public PlayerInfo() {
    }

    public PlayerInfo(ArrayList<PlayerItem> items) {
        this.items = items;
    }

    // Геттеры и сеттеры
    public ArrayList<PlayerItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<PlayerItem> items) {
        this.items = items;
    }

}