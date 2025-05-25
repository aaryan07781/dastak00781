package com.aaryan7.dastakmobile;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Find views
        TextView tvShopName = findViewById(R.id.tvShopName);
        TextView tvDeveloper = findViewById(R.id.tvDeveloper);

        // Create fade-in animation for shop name
        AlphaAnimation fadeInShopName = new AlphaAnimation(0.0f, 1.0f);
        fadeInShopName.setDuration(1500);
        fadeInShopName.setFillAfter(true);
        tvShopName.startAnimation(fadeInShopName);

        // Create fade-in animation for developer credit with delay
        AlphaAnimation fadeInDeveloper = new AlphaAnimation(0.0f, 1.0f);
        fadeInDeveloper.setDuration(1000);
        fadeInDeveloper.setStartOffset(1000);
        fadeInDeveloper.setFillAfter(true);
        tvDeveloper.startAnimation(fadeInDeveloper);

        // Set listener to navigate to MainActivity after animations
        fadeInDeveloper.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                // Delay a bit after animation ends
                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }, 1000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
    }
}
