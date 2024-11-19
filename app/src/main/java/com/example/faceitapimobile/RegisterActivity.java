package com.example.faceitapimobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.faceitapimobile.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailEt.getText().toString().trim();
                String password = binding.passwordEt.getText().toString().trim();
                String username = binding.usernameEt.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (!email.contains("@")) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password should be at least 6 characters", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(email, password, username);
                }
            }
        });
    }

    private void registerUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String userId = mAuth.getCurrentUser().getUid();

                            // Информация о пользователе
                            HashMap<String, Object> userInfo = new HashMap<>();
                            userInfo.put("email", email);
                            userInfo.put("username", username);

                            // Пустой список для любимых игроков
                            List<String> favoritePlayers = new ArrayList<>();
                            userInfo.put("favoritePlayers", favoritePlayers);

                            // Сохранение данных пользователя в Firebase Realtime Database
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(userId)
                                    .setValue(userInfo)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // Переход на экран логина после успешной регистрации
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();  // Закрытие RegisterActivity
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Error saving user data: " + task1.getException(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // Выводим сообщение об ошибке при регистрации
                            Toast.makeText(getApplicationContext(), "Registration failed: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
