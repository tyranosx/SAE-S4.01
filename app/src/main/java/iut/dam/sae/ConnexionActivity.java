package iut.dam.sae;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.text.InputType;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConnexionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText inputEmail, inputPassword;
    private Button btnConnexion;
    private ImageButton btnRetour, btnTogglePassword;
    private TextView txtMdpOublie, txtSinscrire;
    private boolean isPasswordVisible = false;
    private LinearLayout loadingContainer;
    private ProgressBar progressConnexion;
    private TextView textLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Initialisation Firebase Auth et Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupérer les éléments UI
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnConnexion = findViewById(R.id.btn_connexion);
        btnRetour = findViewById(R.id.btn_retour);
        txtMdpOublie = findViewById(R.id.txt_mdp_oublie);
        txtSinscrire = findViewById(R.id.txt_sinscrire);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        loadingContainer = findViewById(R.id.loading_container);
        progressConnexion = findViewById(R.id.progress_connexion);
        textLoading = findViewById(R.id.text_loading);

        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        ImageView logo = findViewById(R.id.logo);
        LinearLayout container = findViewById(R.id.connexion_container);

        // Appliquer animations
        logo.startAnimation(zoomIn);
        container.startAnimation(slideUp);
        btnConnexion.startAnimation(fadeIn);


        // Gérer la connexion
        btnConnexion.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(ConnexionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            showLoading(true);

            // Connexion avec Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Retourner à LoginChoiceActivity au lieu d'aller directement à AdminActivity
                                Intent intent = new Intent(ConnexionActivity.this, LoginChoiceActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            inputEmail.startAnimation(shake);
                            inputPassword.startAnimation(shake);
                            vibrateError();
                            playErrorSound();
                            Toast.makeText(ConnexionActivity.this, "Échec de la connexion : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Redirige vers l'activité "Mot de passe oublié"
        txtMdpOublie.setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Redirige vers l'activité "Inscription"
        txtSinscrire.setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });

        // Bouton retour vers LoginChoiceActivity
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_eye_closed); // Icône rouge (fermé)
        } else {
            inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_eye_open); // Icône verte (ouvert)
        }

        inputPassword.setSelection(inputPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    private void showLoading(boolean isLoading) {
        loadingContainer.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        btnConnexion.setEnabled(!isLoading);
        inputEmail.setEnabled(!isLoading);
        inputPassword.setEnabled(!isLoading);
        btnTogglePassword.setEnabled(!isLoading);
    }

    private void vibrateError() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrator.vibrate(200);
            }
        }
    }

    private void playErrorSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.error_sound);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }
}