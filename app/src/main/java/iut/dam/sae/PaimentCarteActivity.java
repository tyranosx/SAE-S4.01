package iut.dam.sae;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class PaimentCarteActivity extends AppCompatActivity {

    private EditText etNumeroCarte, etDateExpiration, etCVV, etTitulaireCarte;
    private Button btnPayerCarte;
    private FirebaseFirestore db;
    private String prenomUtilisateur = "ANONYME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_carte);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        final String montant = intent.getStringExtra("montant");
        final String association = intent.getStringExtra("nomAssociation");
        final String prenom = intent.getStringExtra("prenom");

        if (prenom != null && !prenom.equals("ANONYME")) {
            prenomUtilisateur = prenom;
        }

        // 🔌 Firebase user (pour prénom si co)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            prenomUtilisateur = documentSnapshot.getString("prenom");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Erreur prénom: " + e.getMessage()));
        }

        // 🎯 UI
        etNumeroCarte = findViewById(R.id.et_card_number);
        etDateExpiration = findViewById(R.id.et_expiry_date);
        etCVV = findViewById(R.id.et_cvv);
        etTitulaireCarte = findViewById(R.id.et_card_holder);
        btnPayerCarte = findViewById(R.id.btn_payer);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        ImageView logo = findViewById(R.id.logo);
        TextView titre = findViewById(R.id.tv_paiement_info);

        // ✨ Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        logo.startAnimation(zoomIn);
        titre.startAnimation(fadeIn);
        etNumeroCarte.startAnimation(fadeIn);
        etDateExpiration.startAnimation(fadeIn);
        etCVV.startAnimation(fadeIn);
        etTitulaireCarte.startAnimation(fadeIn);
        btnPayerCarte.startAnimation(fadeIn);

        // 🔙 Retour
        btnRetour.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
        });

        // 🧠 Auto format date
        etDateExpiration.addTextChangedListener(new TextWatcher() {
            private boolean isEditing;

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;
                String input = s.toString().replaceAll("[^\\d]", "");
                if (input.length() >= 2) {
                    String month = input.substring(0, 2);
                    String year = input.length() > 2 ? input.substring(2) : "";
                    String formatted = month + "/" + year;
                    etDateExpiration.setText(formatted);
                    etDateExpiration.setSelection(formatted.length());
                } else {
                    etDateExpiration.setText(input);
                    etDateExpiration.setSelection(input.length());
                }
                isEditing = false;
            }
        });

        // 🧾 Paiement
        btnPayerCarte.setOnClickListener(v -> {
            if (validatePaymentInformation()) {
                ajouterDocumentDon(Double.parseDouble(montant), association, prenomUtilisateur);

                Toast.makeText(this, "Paiement de " + montant + "€ effectué avec succès !", Toast.LENGTH_LONG).show();
                Intent retourIntent = new Intent(this, DonsActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(retourIntent);
                finish();
            } else {
                // Tremble les champs invalides
                etNumeroCarte.startAnimation(shake);
                etDateExpiration.startAnimation(shake);
                etCVV.startAnimation(shake);
                etTitulaireCarte.startAnimation(shake);
                vibrateError();
                playErrorSound();
            }
        });
    }

    private boolean validatePaymentInformation() {
        boolean isValid = true;

        String numeroCarte = etNumeroCarte.getText().toString().trim();
        String dateExpiration = etDateExpiration.getText().toString().trim();
        String cvv = etCVV.getText().toString().trim();
        String titulaire = etTitulaireCarte.getText().toString().trim();

        if (TextUtils.isEmpty(numeroCarte)) {
            etNumeroCarte.setError("Veuillez saisir le numéro de carte");
            isValid = false;
        }

        if (TextUtils.isEmpty(dateExpiration)) {
            etDateExpiration.setError("Veuillez saisir la date d'expiration");
            isValid = false;
        } else if (!isValidExpiryDate(dateExpiration)) {
            etDateExpiration.setError("Format invalide (MM/AA)");
            isValid = false;
        }

        if (TextUtils.isEmpty(cvv)) {
            etCVV.setError("Veuillez saisir le CVV");
            isValid = false;
        } else if (cvv.length() < 3 || cvv.length() > 4) {
            etCVV.setError("CVV invalide (3-4 chiffres)");
            isValid = false;
        }

        if (TextUtils.isEmpty(titulaire)) {
            etTitulaireCarte.setError("Veuillez saisir le nom du titulaire");
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Veuillez vérifier vos informations de paiement", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }

    private boolean isValidExpiryDate(String dateExpiration) {
        if (!Pattern.matches("^(0[1-9]|1[0-2])/([0-9]{2})$", dateExpiration)) return false;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.FRANCE);
            sdf.setLenient(false);
            Date expiryDate = sdf.parse(dateExpiration);
            Calendar cal = Calendar.getInstance();
            cal.setTime(expiryDate);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
            return !cal.before(Calendar.getInstance());
        } catch (ParseException e) {
            return false;
        }
    }

    private void ajouterDocumentDon(double montant, String association, String prenom) {
        String category = getIntent().getStringExtra("category");
        if (category == null) category = "donUnique";

        Map<String, Object> donData = new HashMap<>();
        donData.put("association", association);
        donData.put("date", new Timestamp(new Date()));
        donData.put("montant", montant);
        donData.put("prenom", prenom);
        donData.put("category", category);

        db.collection("dons").add(donData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Don ajouté : " + documentReference.getId());
                    Toast.makeText(this, "Don ajouté avec succès !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur don : " + e.getMessage());
                    Toast.makeText(this, "Erreur lors de l'ajout du don", Toast.LENGTH_SHORT).show();
                });
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