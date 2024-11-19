package com.example.faceitapimobile;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.LinearLayout;
import android.view.Gravity;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.faceitapimobile.databinding.ActivityMainBinding;
import com.example.faceitapimobile.model.LifetimeStats;
import com.example.faceitapimobile.model.PlayerInfoAndStats;
import com.example.faceitapimobile.model.PlayerStats;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f; // Начальный масштаб
    private PopupWindow previewPopup;
    private TextView stat1, stat2, stat3, stat4, nickname;

    private FaceitService faceitService;










    @RequiresApi(api = Build.VERSION_CODES.S)
    private void applyBlurEffect(ImageView imageView) {
        // Установка эффекта размытия
        imageView.setRenderEffect(RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        // Кнопка выхода
        binding.exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });

        // Короткое нажатие — переход на страницу списка игроков
        binding.listOfPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayersList.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
                startActivity(intent, options.toBundle());
            }
        });

        // Долгое нажатие для предпросмотра
        binding.listOfPlayers.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPreviewPopup(v); // Показать окно предпросмотра
                return true;
            }
        });

        // Скрытие предпросмотра при отпускании кнопки
        binding.listOfPlayers.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && previewPopup != null && previewPopup.isShowing()) {
                    previewPopup.dismiss();
                }
                return false;
            }
        });

        // Инициализация жестов для увеличения изображения
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());

        // Установка слушателя касаний на ImageView
        binding.imageViewAvatar.setOnTouchListener((v, event) -> {
            scaleGestureDetector.onTouchEvent(event);
            return true;
        });




        faceitService = new FaceitService(); // Инициализация сервиса

        // Установка обработчика для кнопки поиска
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Получаем текст из EditText
                String nickname = binding.playerFound.getText().toString().trim();
                if (!nickname.isEmpty()) {
                    // Отправляем запрос с ником
                    fetchPlayerInfo(nickname);
                } else {
                    // Обработка случая, когда поле ввода пустое
                    Toast.makeText(MainActivity.this, "Введите ник игрока", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Если необходимо, можно добавить обработчик для EditText
        // Например, для получения текста при нажатии кнопки "Enter"
        binding.playerFound.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String nickname = binding.playerFound.getText().toString();
                fetchPlayerInfo(nickname);
                return true;
            }
            return false;
        });
    }

    private void fetchPlayerInfo(String nickname) {
        FaceitService faceitService = new FaceitService();
        faceitService.getPlayerInfo(nickname, new FaceitService.Callback() {
            @Override
            public void onSuccess(PlayerInfoAndStats playerInfoAndStats) {
                // Обработка успешного ответа и обновление UI
                updateStats(playerInfoAndStats);
            }

            @Override
            public void onFailure(String errorMessage) {
                // Обработка ошибки
                Log.e("MainActivity", "Error: " + errorMessage);
                Toast.makeText(MainActivity.this, "Ошибка: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStats(PlayerInfoAndStats infoAndStats) {
        // Получите необходимые статистические данные
        PlayerStats playerStats = infoAndStats.getPlayerStats();
        LifetimeStats lifetimeStats = playerStats.getLifetime();

        // Обновление UI должно происходить в основном потоке
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (lifetimeStats != null) {
                    // Пример получения статистики
                    binding.stat1.setText(lifetimeStats.getAverageKDRatio()); // Предполагается, что у вас есть этот метод
                    binding.stat2.setText( lifetimeStats.getWins()); // Пример для побед
                    binding.stat3.setText( lifetimeStats.getMatches()); // Пример для матчей
                    binding.stat4.setText( lifetimeStats.getWinRate()); // Пример для винрейта
                    binding.nickname.setText(infoAndStats.getPlayerInfo().getItems().get(0).getNickname()); // Имя игрока
                    String avatarUrl = infoAndStats.getPlayerInfo().getItems().get(0).getAvatarUrl(); // Получите URL аватарки

                    if(avatarUrl!=null) {
                        Picasso.get()
                                .load(avatarUrl)

                                .placeholder(R.drawable.download) // Укажите изображение-заглушку (если нужно)
                                .error(R.drawable.download) // Укажите изображение, если произошла ошибка загрузки\// Подгоняет изображение под размеры ImageView
                                .fit() // Подгоняет изображение под размеры ImageView

                                .into(binding.imageViewAvatar); // Укажите ваш ImageView
                    }

                } else {
                    // Если lifetimeStats равен null, вы можете обработать это
                    Log.e("MainActivity", "LifetimeStats is null");
                }
            }
        });
    }






    private void showPreviewPopup(View anchorView) {
        // Раздуваем layout для всплывающего окна предпросмотра
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.preview_players_list, null);

        // Создаем новый ImageView для размытого фона
        ImageView blurredBackgroundImageView = popupView.findViewById(R.id.backgroundImageView);

        // Применяем эффект размытия к фоновому изображению
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            applyBlurEffect(blurredBackgroundImageView);
        }

        // Получаем ссылку на ListView внутри предпросмотра
        ListView previewListView = popupView.findViewById(R.id.previewlistView);




        // Данные игроков
        final String[] catNames = new String[]{
                "niko", "b1t", "m0nesy", "jl", "hunter", "s1mple","iM"
        };

        // Адаптер для списка игроков
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.list_item,
                R.id.itemText,
                catNames
        );

        previewListView.setAdapter(adapter);

        // Создаем и показываем PopupWindow
        int popupWidth = 1000;  // ширина окна предпросмотра
        int popupHeight = 950;  // высота окна предпросмотра
        previewPopup = new PopupWindow(popupView, popupWidth, popupHeight, true);

        // Применяем анимацию прозрачности
        popupView.setAlpha(0f);
        popupView.animate().alpha(1f).setDuration(300).start();

        // Устанавливаем закругленные углы
//        popupView.setBackgroundResource(R.drawable.firewatch);

        // Получаем ширину экрана
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        // Вычисляем X для центрирования относительно экрана
        int popupX = (screenWidth - popupWidth) / 2;

        // Получаем координаты Y позиции кнопки
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int popupY = location[1] + 170; // оставляем Y такой же как у кнопки

        // Показываем PopupWindow
        previewPopup.showAtLocation(anchorView, Gravity.NO_GRAVITY, popupX, popupY);
    }


    // Обработка событий увеличения
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float scaleFactor = 1.0f;  // Текущий масштаб изображения

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // Сбрасываем масштаб к 1.0f в начале нового жеста
            scaleFactor = 1.0f;
            return true; // Возвращаем true, чтобы обработка жеста продолжилась
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // Получаем фактор изменения масштаба
            float scaleFactorDelta = detector.getScaleFactor();

            // Применяем сглаживание через интерполяцию (например, 0.15f)
            scaleFactor += (scaleFactorDelta - 1) * 0.5f;

            // Ограничиваем минимальный и максимальный масштабы
            scaleFactor = Math.max(1.0f, Math.min(scaleFactor, 10.0f));

            // Применяем масштаб к ImageView
            binding.imageViewAvatar.setScaleX(scaleFactor);
            binding.imageViewAvatar.setScaleY(scaleFactor);

            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // Плавно возвращаем изображение к исходному размеру (масштаб 1.0f)
            binding.imageViewAvatar.animate()
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(300) // Длительность анимации в миллисекундах
                    .start();
        }
    }

}
