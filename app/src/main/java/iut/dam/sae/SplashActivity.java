package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 secondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);

        // Animation de fade-in
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(1000); // 1 seconde
        logo.startAnimation(fadeIn);
        logo.setAlpha(1f); // Important pour qu’il reste visible après l’animation

        // Redirection après quelques secondes
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }
}