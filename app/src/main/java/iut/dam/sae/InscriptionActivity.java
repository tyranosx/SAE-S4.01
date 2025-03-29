package iut.dam.sae;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Build;
import android.os.Vibrator;
import android.os.VibrationEffect;
import android.media.MediaPlayer;
import android.text.InputType;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Ajout de la base de données Firestore

    private EditText inputEmail, inputPassword, inputPrenom, inputNom, inputDateNaissance;
    private Button btnSinscrire;
    private ImageButton btnRetour, btnTogglePassword;

    private Calendar calendar;
    private boolean isPasswordVisible = false;
    private LinearLayout loadingContainer;
    private ProgressBar progressInscription;
    private TextView textLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();  // Initialisation de Firestore

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputPrenom = findViewById(R.id.input_prenom);  // Champ Prénom
        inputNom = findViewById(R.id.input_nom);        // Champ Nom
        inputDateNaissance = findViewById(R.id.input_date_naissance);

        btnSinscrire = findViewById(R.id.btn_inscription);
        btnRetour = findViewById(R.id.btn_retour);
        btnTogglePassword = findViewById(R.id.btn_toggle_password);
        loadingContainer = findViewById(R.id.loading_container);
        progressInscription = findViewById(R.id.progress_inscription);
        textLoading = findViewById(R.id.text_loading);

        calendar = Calendar.getInstance();

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake); // pour erreurs
        Animation bounce = AnimationUtils.loadAnimation(this, R.anim.bounce);
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse);

        ImageView logo = findViewById(R.id.logo);
        LinearLayout container = findViewById(R.id.inscription_container);
        Button btnSinscrire = findViewById(R.id.btn_inscription);

        logo.startAnimation(zoomIn);
        logo.startAnimation(pulse);
        container.startAnimation(slideUp);
        btnSinscrire.startAnimation(fadeIn);



        // Gestion du DatePicker pour la date de naissance
        inputDateNaissance.setOnClickListener(v -> {
            vibrateError();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    InscriptionActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                        inputDateNaissance.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        btnTogglePassword.setOnClickListener(v -> togglePasswordVisibility());

        // Gestion de l'inscription
        btnSinscrire.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String prenom = inputPrenom.getText().toString().trim();
            String nom = inputNom.getText().toString().trim();
            String dateNaissance = inputDateNaissance.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || prenom.isEmpty() || nom.isEmpty() || dateNaissance.isEmpty()) {
                Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                if (prenom.isEmpty()) {
                    inputPrenom.startAnimation(shake);
                    vibrateError();
                    playErrorSound();
                }
                if (nom.isEmpty()) {
                    inputNom.startAnimation(shake);
                    vibrateError();
                    playErrorSound();
                }
                if (email.isEmpty()) {
                    inputEmail.startAnimation(shake);
                    vibrateError();
                    playErrorSound();
                }
                if (dateNaissance.isEmpty()) {
                    inputDateNaissance.startAnimation(shake);
                    vibrateError();
                    playErrorSound();
                }
                if (password.isEmpty()) {
                    inputPassword.startAnimation(shake);
                    vibrateError();
                    playErrorSound();
                }
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(InscriptionActivity.this, "Format d'email invalide", Toast.LENGTH_SHORT).show();
                inputEmail.startAnimation(shake);
                vibrateError();
                playErrorSound();
                return;
            }

            if (!isValidPassword(password)) {
                Toast.makeText(InscriptionActivity.this,
                        "Le mot de passe doit contenir au moins 8 caractères, avec une majuscule, une minuscule, un chiffre et un caractère spécial.",
                        Toast.LENGTH_LONG).show();
                inputPassword.startAnimation(shake);
                vibrateError();
                playErrorSound();
                return;
            }

            showLoading(true);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        showLoading(false);
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Enregistrer les informations supplémentaires dans Firestore
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("prenom", prenom);
                                userData.put("nom", nom);
                                userData.put("dateNaissance", dateNaissance);
                                userData.put("association", "NONE"); // Valeur par défaut
                                userData.put("isAdmin", false);     // Valeur par défaut

                                // Enregistrement dans Firestore
                                db.collection("users").document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            showLoading(false);
                                            Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                                            logo.clearAnimation();
                                            logo.startAnimation(bounce);

                                            new android.os.Handler().postDelayed(() -> {
                                                startActivity(new Intent(InscriptionActivity.this, LoginChoiceActivity.class));
                                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                finish();
                                            }, 400);
                                        })
                                        .addOnFailureListener(e -> {
                                            showLoading(false);
                                            Toast.makeText(InscriptionActivity.this, "Erreur lors de l'ajout des données", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(InscriptionActivity.this, "Inscription échouée : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Gestion du bouton retour
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(InscriptionActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_right);
            finish();
        });
    }

    // Vérification du format de l'email
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Vérification de la sécurité du mot de passe
    private boolean isValidPassword(String password) {
        // Minimum 8 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial
        String passwordPattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return password.matches(passwordPattern);
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_eye_closed); // Icône rouge
        } else {
            inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btnTogglePassword.setImageResource(R.drawable.ic_eye_open); // Icône verte
        }

        inputPassword.setSelection(inputPassword.getText().length());
        isPasswordVisible = !isPasswordVisible;
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

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            btnSinscrire.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out));
            loadingContainer.setVisibility(View.VISIBLE);
        } else {
            btnSinscrire.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
            loadingContainer.setVisibility(View.GONE);
        }

        btnSinscrire.setEnabled(!isLoading);
        inputEmail.setEnabled(!isLoading);
        inputPassword.setEnabled(!isLoading);
        inputPrenom.setEnabled(!isLoading);
        inputNom.setEnabled(!isLoading);
        inputDateNaissance.setEnabled(!isLoading);
        btnTogglePassword.setEnabled(!isLoading);
    }
}