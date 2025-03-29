package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.SoundEffectConstants;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DemandePrenomActivity extends AppCompatActivity {

    private EditText etPrenom;
    private Button btnValider, btnAnonyme;
    private ImageButton btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_prenom);

        etPrenom = findViewById(R.id.et_prenom);
        btnValider = findViewById(R.id.btn_valider);
        btnAnonyme = findViewById(R.id.btn_anonyme);
        btnRetour = findViewById(R.id.btn_retour);

        // Charger les animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);

        // Appliquer les animations d’apparition
        findViewById(R.id.tv_titre).startAnimation(fadeIn);
        etPrenom.startAnimation(slideIn);
        btnValider.startAnimation(slideIn);
        btnAnonyme.startAnimation(slideIn);

        // Gestion du bouton retour
        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale); // 👈 Animation de clic
            Intent intent = new Intent(DemandePrenomActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        // Bouton "Valider"
        btnValider.setOnClickListener(v -> {
            v.startAnimation(clickScale); // 👈 Animation de clic

            String prenom = etPrenom.getText().toString().trim();

            if (prenom.isEmpty()) {
                // Animation shake
                Animation shake = AnimationUtils.loadAnimation(DemandePrenomActivity.this, R.anim.shake);
                etPrenom.startAnimation(shake);

                // Vibration
                Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                if (vibrator != null) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        vibrator.vibrate(150);
                    }
                }

                // Son d’erreur
                etPrenom.playSoundEffect(SoundEffectConstants.CLICK);

                // Message
                Toast.makeText(DemandePrenomActivity.this, "Veuillez entrer votre prénom", Toast.LENGTH_SHORT).show();
                return;
            }

            // Redirige vers DonsActivity
            Intent intent = new Intent(DemandePrenomActivity.this, DonsActivity.class);
            intent.putExtra("prenom", prenom);
            startActivity(intent);
            finish();
        });

        // Bouton "Anonyme"
        btnAnonyme.setOnClickListener(v -> {
            v.startAnimation(clickScale); // 👈 Animation de clic

            Intent intent = new Intent(DemandePrenomActivity.this, DonsActivity.class);
            intent.putExtra("prenom", "ANONYME");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}