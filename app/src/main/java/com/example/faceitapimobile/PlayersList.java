package com.example.faceitapimobile;

import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class PlayersList extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> favoritePlayersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players_list);

        // Настройка Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Настройка ListView
        listView = findViewById(R.id.listView);
        favoritePlayersList = new ArrayList<>();
        adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item,
                R.id.itemText,
                favoritePlayersList
        );
        listView.setAdapter(adapter);

        // Получаем ссылку на ImageView для фона и применяем размытие
        ImageView backgroundImageView = findViewById(R.id.backgroundImageView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applyBlurEffect(backgroundImageView);
        }

        // Загружаем список любимых игроков
        loadFavoritePlayers();
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    private void applyBlurEffect(ImageView imageView) {
        imageView.setRenderEffect(RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP));
    }

    private void loadFavoritePlayers() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Извлекаем список любимых игроков из документа
                            List<String> favoritePlayers = (List<String>) document.get("favoritePlayers");
                            if (favoritePlayers != null) {
                                favoritePlayersList.clear();
                                favoritePlayersList.addAll(favoritePlayers);
                                adapter.notifyDataSetChanged(); // Обновляем адаптер
                            } else {
                                Toast.makeText(this, "No favorite players found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
