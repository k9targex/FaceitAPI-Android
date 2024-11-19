package com.example.faceitapimobile.model;

import com.google.gson.annotations.SerializedName; // Импортируем нужные аннотации Gson
import java.util.List;

public class LifetimeStats {

    @SerializedName("Average K/D Ratio")
    private String averageKDRatio; // Среднее соотношение убийств к смертям

    @SerializedName("Average Headshots %")
    private String averageHeadshots; // Средний процент попаданий в голову

    @SerializedName("Total Headshots %")
    private String totalHeadshots; // Общий процент попаданий в голову

    @SerializedName("Matches")
    private String matches; // Количество матчей

    @SerializedName("Wins")
    private String wins; // Количество побед

    @SerializedName("Win Rate %")
    private String winRate; // Процент побед

    @SerializedName("Current Win Streak")
    private String currentWinStreak; // Текущая серия побед

    @SerializedName("Longest Win Streak")
    private String longestWinStreak; // Долгосрочная серия побед

    // Не сериализуемые поля
    @SerializedName("kDRatio")
    private String kDRatio; // Поле K/D, если требуется, просто уберите аннотацию, если не нужно.

    @SerializedName("recentResults")
    private List<String> recentResults; // Список недавних результатов

    // Геттеры и сеттеры для полей (можно использовать IDE для их генерации)
    public String getAverageKDRatio() {
        return averageKDRatio;
    }

    public void setAverageKDRatio(String averageKDRatio) {
        this.averageKDRatio = averageKDRatio;
    }

    public String getAverageHeadshots() {
        return averageHeadshots;
    }

    public void setAverageHeadshots(String averageHeadshots) {
        this.averageHeadshots = averageHeadshots;
    }

    public String getTotalHeadshots() {
        return totalHeadshots;
    }

    public void setTotalHeadshots(String totalHeadshots) {
        this.totalHeadshots = totalHeadshots;
    }

    public String getMatches() {
        return matches;
    }

    public void setMatches(String matches) {
        this.matches = matches;
    }

    public String getWins() {
        return wins;
    }

    public void setWins(String wins) {
        this.wins = wins;
    }

    public String getWinRate() {
        return winRate;
    }

    public void setWinRate(String winRate) {
        this.winRate = winRate;
    }

    public String getCurrentWinStreak() {
        return currentWinStreak;
    }

    public void setCurrentWinStreak(String currentWinStreak) {
        this.currentWinStreak = currentWinStreak;
    }

    public String getLongestWinStreak() {
        return longestWinStreak;
    }

    public void setLongestWinStreak(String longestWinStreak) {
        this.longestWinStreak = longestWinStreak;
    }

    public String getKDRatio() {
        return kDRatio;
    }

    public void setKDRatio(String kDRatio) {
        this.kDRatio = kDRatio;
    }

    public List<String> getRecentResults() {
        return recentResults;
    }

    public void setRecentResults(List<String> recentResults) {
        this.recentResults = recentResults;
    }
}