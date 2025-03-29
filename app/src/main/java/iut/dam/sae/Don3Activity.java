package iut.dam.sae;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class Don3Activity extends AppCompatActivity {

    private static final int REQUEST_PRENOM = 1;

    private EditText etMontant;
    private String montantSelectionne = "";
    private boolean paiementParCarteSelectionne = false;
    private boolean paiementParIbanSelectionne = false;
    private String nomAssociation = "INCONNU";
    private String prenomUtilisateur = "ANONYME";

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private LinearLayout btnCarte, btnIban;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don3);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        ImageButton btnProfil = findViewById(R.id.btn_profil);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        Button btnDonner = findViewById(R.id.btn_donner);
        btnCarte = findViewById(R.id.btn_carte);
        btnIban = findViewById(R.id.btn_iban);
        ImageView logo = findViewById(R.id.logo);

        TextView choixMontant = findViewById(R.id.choixmontant);
        TextView saisieMontantLabel = findViewById(R.id.saisiemontant);
        TextView modePaiementLabel = findViewById(R.id.modepaiement);

        TextView btn10 = findViewById(R.id.btn_10);
        TextView btn20 = findViewById(R.id.btn_20);
        TextView btn50 = findViewById(R.id.btn_50);
        etMontant = findViewById(R.id.et_montant);

        // 🎞️ Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake); // Tremblement 👊

        logo.startAnimation(zoomIn);
        choixMontant.startAnimation(slideUp);
        saisieMontantLabel.startAnimation(slideUp);
        modePaiementLabel.startAnimation(slideUp);
        btn10.startAnimation(fadeIn);
        btn20.startAnimation(fadeIn);
        btn50.startAnimation(fadeIn);
        btnCarte.startAnimation(fadeIn);
        btnIban.startAnimation(fadeIn);
        btnDonner.startAnimation(fadeIn);

        if (user == null) {
            btnProfil.setVisibility(ImageButton.GONE);
        } else {
            btnProfil.setOnClickListener(v -> {
                startActivity(new Intent(Don3Activity.this, ProfilActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        String prenom = getIntent().getStringExtra("prenom");
        if (prenom != null && !prenom.equals("ANONYME")) {
            prenomUtilisateur = prenom;
        }

        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            prenomUtilisateur = documentSnapshot.getString("prenom");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Erreur prénom", e));
        }

        String intentNomAssociation = getIntent().getStringExtra("nomAssociation");
        if (intentNomAssociation != null && !intentNomAssociation.isEmpty()) {
            nomAssociation = intentNomAssociation;
        }

        // 💶 Montants prédéfinis
        View.OnClickListener montantClickListener = v -> {
            montantSelectionne = ((TextView) v).getText().toString().replace("€", "").trim();
            etMontant.setText(montantSelectionne);
        };

        btn10.setOnClickListener(montantClickListener);
        btn20.setOnClickListener(montantClickListener);
        btn50.setOnClickListener(montantClickListener);

        // 💳 Modes de paiement
        btnCarte.setOnClickListener(v -> {
            paiementParCarteSelectionne = true;
            paiementParIbanSelectionne = false;
            Toast.makeText(this, "Paiement par carte sélectionné", Toast.LENGTH_SHORT).show();
        });

        btnIban.setOnClickListener(v -> {
            paiementParIbanSelectionne = true;
            paiementParCarteSelectionne = false;
            Toast.makeText(this, "Paiement par IBAN sélectionné", Toast.LENGTH_SHORT).show();
        });

        // 🎁 Donner
        btnDonner.setOnClickListener(v -> {
            String montant = etMontant.getText().toString().trim();
            String category = getIntent().getStringExtra("category");

            if (montant.isEmpty()) {
                etMontant.startAnimation(shake);
                vibrateError();
                playErrorSound();
                Toast.makeText(this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nomAssociation == null || nomAssociation.isEmpty()) {
                Toast.makeText(this, "Erreur : Nom de l'association manquant", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!paiementParCarteSelectionne && !paiementParIbanSelectionne) {
                btnCarte.startAnimation(shake);
                btnIban.startAnimation(shake);
                vibrateError();
                playErrorSound();
                Toast.makeText(this, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(Don3Activity.this,
                    paiementParCarteSelectionne ? PaimentCarteActivity.class : PaimentRibActivity.class);

            intent.putExtra("montant", montant);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("prenom", prenomUtilisateur);
            intent.putExtra("category", category);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        // 🔙 Retour
        btnRetour.setOnClickListener(v -> {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PRENOM && resultCode == RESULT_OK && data != null) {
            prenomUtilisateur = data.getStringExtra("prenom");
        }
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