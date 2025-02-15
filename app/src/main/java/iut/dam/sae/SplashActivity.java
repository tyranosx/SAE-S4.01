package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 secondes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Redirection après quelques secondes
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish(); // Ferme cette activité pour éviter de revenir en arrière
        }, SPLASH_TIME_OUT);
    }
}
