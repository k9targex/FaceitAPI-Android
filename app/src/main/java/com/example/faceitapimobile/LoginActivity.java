package com.example.faceitapimobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.faceitapimobile.databinding.ActivityLogginBinding;
import com.example.faceitapimobile.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private ActivityLogginBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Инициализация Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEt.getText().toString().trim();
                String password = binding.passwordEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    signInUser(email, password);
                }
            }
        });

        binding.goToRegisterActivityTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void signInUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadUserFromDatabase();
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void loadUserFromDatabase() {
        String userId = mAuth.getCurrentUser().getUid();

        // Загрузка данных пользователя из Firestore
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Получаем данные пользователя
                                User user = document.toObject(User.class);
                                Toast.makeText(LoginActivity.this, "Welcome, " + user.getUserId(), Toast.LENGTH_SHORT).show();

                                // Переход в основное приложение
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish(); // Закрытие LoginActivity после успешного входа
                            } else {
                                Toast.makeText(LoginActivity.this, "User data not found.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
