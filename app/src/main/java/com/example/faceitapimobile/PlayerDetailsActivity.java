package com.example.faceitapimobile;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class PlayerDetailsActivity extends AppCompatActivity {
    private ImageView imageViewAvatar, likeIcon;
    private TextView nicknameTextView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private boolean isDoubleClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация элементов и Firebase
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        likeIcon = findViewById(R.id.likeIcon);
        nicknameTextView = findViewById(R.id.nickname);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        imageViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDoubleClick) {
                    String nickname = nicknameTextView.getText().toString();
                    addPlayerToFavorites(nickname);
                    showLikeAnimation();
                    isDoubleClick = false;
                } else {
                    isDoubleClick = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isDoubleClick = false;
                        }
                    }, 300); // Тайм-аут для двойного клика
                }
            }
        });
    }

    private void addPlayerToFavorites(String playerNickname) {
        String userId = mAuth.getCurrentUser().getUid();
        db.collection("users").document(userId)
                .update("favoritePlayers", FieldValue.arrayUnion(playerNickname))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Player added to favorites", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showLikeAnimation() {
        likeIcon.setVisibility(View.VISIBLE);

        // Анимация масштабирования
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.7f, 1.2f, 0.7f, 1.2f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleAnimation.setDuration(300);

        // Анимация исчезновения
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setStartOffset(300);
        alphaAnimation.setDuration(300);

        // Запуск анимации
        likeIcon.startAnimation(scaleAnimation);
        likeIcon.startAnimation(alphaAnimation);

        new Handler().postDelayed(() -> likeIcon.setVisibility(View.GONE), 600);
    }
}
