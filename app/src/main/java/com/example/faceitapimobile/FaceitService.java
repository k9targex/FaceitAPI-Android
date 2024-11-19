package com.example.faceitapimobile;
import android.util.Log;

import com.example.faceitapimobile.model.PlayerInfo;
import com.example.faceitapimobile.model.PlayerInfoAndStats;
import com.example.faceitapimobile.model.PlayerStats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import java.io.IOException;

public class FaceitService {
    private static final String TAG = "FaceitService";
    private static final String TOKEN = "Bearer 88887511-8b30-4eaa-a38a-ad593868dfac";  // Укажите токен
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();

    public interface Callback {
        void onSuccess(PlayerInfoAndStats playerInfoAndStats);
        void onFailure(String errorMessage);
    }

    public void getPlayerInfo(String nickname, Callback callback) {
        String url = String.format(
                "https://open.faceit.com/data/v4/search/players?nickname=%s&game=cs2&offset=0&limit=1",
                nickname
        );

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", TOKEN)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    PlayerInfo playerInfo = gson.fromJson(response.body().string(), PlayerInfo.class);
                    if (!playerInfo.getItems().isEmpty()) {
                        String playerId = playerInfo.getItems().get(0).getPlayerId();
                        getPlayerStats(playerId, playerInfo, callback);
                    } else {
                        runOnMainThread(() -> callback.onFailure("Player not found"));
                    }
                } else {
                    if (response.code() == 400) {
                        runOnMainThread(() -> callback.onFailure("Invalid request: Code 400. Please check the player nickname."));
                    } else {
                        runOnMainThread(() -> callback.onFailure("Failed to fetch player info: Error " + response.code()));
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching player info", e);
                runOnMainThread(() -> callback.onFailure(e.getMessage()));
            }
        });
    }

    private void getPlayerStats(String playerId, PlayerInfo playerInfo, Callback callback) {
        String url = String.format("https://open.faceit.com/data/v4/players/%s/stats/cs2", playerId);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", TOKEN)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    PlayerStats playerStats = gson.fromJson(response.body().string(), PlayerStats.class);

                    PlayerInfoAndStats infoAndStats = new PlayerInfoAndStats();
                    infoAndStats.setPlayerInfo(playerInfo);
                    infoAndStats.setPlayerStats(playerStats);
                    infoAndStats.setSkillLevel(playerInfo.getItems().get(0).getSkillLevel());

                    runOnMainThread(() -> callback.onSuccess(infoAndStats));
                } else {
                    runOnMainThread(() -> callback.onFailure("Failed to fetch player stats"));
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error fetching player stats", e);
                runOnMainThread(() -> callback.onFailure(e.getMessage()));
            }
        });
    }

    // Метод для выполнения кода в основном потоке
    private void runOnMainThread(Runnable action) {
        new android.os.Handler(android.os.Looper.getMainLooper()).post(action);
    }

}
