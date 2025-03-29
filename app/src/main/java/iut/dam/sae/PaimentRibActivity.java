package iut.dam.sae;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaimentRibActivity extends AppCompatActivity {

    private EditText etIban, etBic, etTitulaire;
    private FirebaseFirestore db;
    private String prenomUtilisateur = "ANONYME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_rib);

        db = FirebaseFirestore.getInstance();

        // 🔌 UI Elements
        etIban = findViewById(R.id.et_iban);
        etBic = findViewById(R.id.et_bic);
        etTitulaire = findViewById(R.id.et_titulaire);
        ImageView logo = findViewById(R.id.logo);
        TextView titre = findViewById(R.id.tv_paiement_info);
        Button btnValider = findViewById(R.id.btn_valider);
        ImageButton btnRetour = findViewById(R.id.btn_retour);

        // ✨ Animations
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);

        logo.startAnimation(zoomIn);
        titre.startAnimation(fadeIn);
        etIban.startAnimation(fadeIn);
        etBic.startAnimation(fadeIn);
        etTitulaire.startAnimation(fadeIn);
        btnValider.startAnimation(fadeIn);

        // 🔙 Bouton retour
        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
        });

        // 🔁 Récupération des extras
        Intent intent = getIntent();
        final String montant = intent.getStringExtra("montant");
        final String association = intent.getStringExtra("nomAssociation");
        final String prenom = intent.getStringExtra("prenom");

        if (prenom != null && !prenom.equals("ANONYME")) {
            prenomUtilisateur = prenom;
        }

        // 🔐 Firebase user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            prenomUtilisateur = doc.getString("prenom");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Erreur prénom : " + e.getMessage()));
        }

        // 💳 Bouton de paiement
        btnValider.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            if (validateIbanInformation()) {
                ajouterDocumentDon(Double.parseDouble(montant), association, prenomUtilisateur);
                Toast.makeText(this, "Paiement de " + montant + "€ effectué via IBAN !", Toast.LENGTH_LONG).show();

                Intent retourIntent = new Intent(this, DonsActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(retourIntent);
                finish();
            } else {
                etIban.startAnimation(shake);
                etBic.startAnimation(shake);
                etTitulaire.startAnimation(shake);
                vibrateError();
                playErrorSound();
            }
        });
    }

    // ✅ Validation
    private boolean validateIbanInformation() {
        boolean isValid = true;

        String iban = etIban.getText().toString().trim().replaceAll("\\s", "");
        if (TextUtils.isEmpty(iban)) {
            etIban.setError("Veuillez saisir votre IBAN");
            isValid = false;
        } else if (iban.length() < 14 || iban.length() > 34) {
            etIban.setError("IBAN invalide (14-34 caractères)");
            isValid = false;
        }

        String bic = etBic.getText().toString().trim().replaceAll("\\s", "");
        if (TextUtils.isEmpty(bic)) {
            etBic.setError("Veuillez saisir votre BIC/SWIFT");
            isValid = false;
        } else if (bic.length() < 8 || bic.length() > 11) {
            etBic.setError("BIC invalide (8-11 caractères)");
            isValid = false;
        }

        String titulaire = etTitulaire.getText().toString().trim();
        if (TextUtils.isEmpty(titulaire)) {
            etTitulaire.setError("Veuillez saisir le nom du titulaire du compte");
            isValid = false;
        }

        if (!isValid) {
            Toast.makeText(this, "Veuillez vérifier vos informations bancaires", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }

    // 🔥 Ajouter le don
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
                .addOnSuccessListener(docRef -> {
                    Log.d("Firestore", "Don ajouté : " + docRef.getId());
                    Toast.makeText(this, "Don ajouté avec succès !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur lors de l'ajout du don : " + e.getMessage());
                    Toast.makeText(this, "Erreur lors de l'ajout du don", Toast.LENGTH_SHORT).show();
                });
    }

    // 📳 Vibration
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

    // 🔊 Son erreur
    private void playErrorSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.error_sound);
        mediaPlayer.setOnCompletionListener(MediaPlayer::release);
        mediaPlayer.start();
    }
}