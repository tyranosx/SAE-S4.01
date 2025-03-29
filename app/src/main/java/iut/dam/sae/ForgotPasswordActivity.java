package iut.dam.sae;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Patterns;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;
    private ImageButton btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.editTextEmail);
        resetPasswordButton = findViewById(R.id.btn_reset_password);
        btnRetour = findViewById(R.id.btn_retour);

        // Animations de démarrage
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        findViewById(R.id.title_text).startAnimation(fadeIn);
        emailEditText.startAnimation(slideIn);
        resetPasswordButton.startAnimation(slideIn);

        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        resetPasswordButton.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Tremblement
                Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
                emailEditText.startAnimation(shake);

                // Vibration
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                if (vibrator != null) {
                    vibrator.vibrate(200);
                }

                // Son
                ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
                toneGen.startTone(ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 300);

                // Message
                Toast.makeText(this, "Veuillez entrer une adresse mail valide.", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPasswordActivity.this, "Mail de réinitialisation envoyé !", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}