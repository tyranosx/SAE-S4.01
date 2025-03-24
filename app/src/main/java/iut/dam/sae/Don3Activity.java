package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Don3Activity extends AppCompatActivity {

    private static final int REQUEST_PRENOM = 1;

    private EditText etMontant;
    private String montantSelectionne = "";
    private boolean paiementParCarteSelectionne = false;
    private boolean paiementParIbanSelectionne = false;
    private String nomAssociation = "INCONNU";
    private String prenomUtilisateur = "ANONYME";  // Par défaut à "ANONYME"

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don3);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();

        // Gestion du bouton Profil
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        if (user == null) {
            btnProfil.setVisibility(ImageButton.GONE); // Masquer si l'utilisateur n'est pas connecté
        } else {
            btnProfil.setOnClickListener(v -> {
                startActivity(new Intent(Don3Activity.this, ProfilActivity.class));
            });
        }

        // Récupération du prénom via Intent ou valeur par défaut
        String prenom = getIntent().getStringExtra("prenom");
        if (prenom != null && !prenom.equals("ANONYME")) {
            prenomUtilisateur = prenom; // Utilise le prénom transmis uniquement s'il est valide
        } else {
            prenomUtilisateur = "ANONYME"; // Par défaut à "ANONYME"
        }

        // Si l'utilisateur est connecté, récupérer son prénom depuis Firestore
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            prenomUtilisateur = documentSnapshot.getString("prenom");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Erreur lors de la récupération du prénom", e));
        }

        // Récupération du nom de l'association
        String intentNomAssociation = getIntent().getStringExtra("nomAssociation");
        if (intentNomAssociation != null && !intentNomAssociation.isEmpty()) {
            nomAssociation = intentNomAssociation;
        }

        etMontant = findViewById(R.id.et_montant);

        // Gestion des montants prédéfinis
        TextView btn10 = findViewById(R.id.btn_10);
        TextView btn20 = findViewById(R.id.btn_20);
        TextView btn50 = findViewById(R.id.btn_50);

        View.OnClickListener montantClickListener = v -> {
            montantSelectionne = ((TextView) v).getText().toString().replace("€", "").trim();
            etMontant.setText(montantSelectionne);
        };

        btn10.setOnClickListener(montantClickListener);
        btn20.setOnClickListener(montantClickListener);
        btn50.setOnClickListener(montantClickListener);

        // Gestion des modes de paiement
        LinearLayout btnCarte = findViewById(R.id.btn_carte);
        LinearLayout btnIban = findViewById(R.id.btn_iban);

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

        // Gestion du bouton "Donner"
        Button btnDonner = findViewById(R.id.btn_donner);
        // Gestion du bouton "Donner"
        btnDonner.setOnClickListener(v -> {
            String montant = etMontant.getText().toString().trim();
            String category = getIntent().getStringExtra("category");

            if (montant.isEmpty()) {
                Toast.makeText(Don3Activity.this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nomAssociation == null || nomAssociation.isEmpty()) {
                Toast.makeText(Don3Activity.this, "Erreur : Nom de l'association manquant", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent;
            if (paiementParCarteSelectionne) {
                intent = new Intent(Don3Activity.this, PaimentCarteActivity.class);
            } else if (paiementParIbanSelectionne) {
                intent = new Intent(Don3Activity.this, PaimentRibActivity.class);
            } else {
                Toast.makeText(Don3Activity.this, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtra("montant", montant);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("prenom", prenomUtilisateur);
            intent.putExtra("category", category); // Ajout de la catégorie
            startActivity(intent);
        });

        // Gestion du bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());
    }

    // Récupérer les données du prénom depuis DemandePrenomActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PRENOM && resultCode == RESULT_OK && data != null) {
            prenomUtilisateur = data.getStringExtra("prenom");
        }
    }
}
