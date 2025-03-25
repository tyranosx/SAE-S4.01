package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
    private String prenomUtilisateur = "ANONYME"; // Valeur par défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_rib);

        db = FirebaseFirestore.getInstance();

        // Initialiser les EditText pour les informations IBAN
        etIban = findViewById(R.id.et_iban);
        etBic = findViewById(R.id.et_bic);
        etTitulaire = findViewById(R.id.et_titulaire);

        Intent intent = getIntent();
        final String montant = intent.getStringExtra("montant");
        final String association = intent.getStringExtra("nomAssociation");
        final String prenom = intent.getStringExtra("prenom"); // Récupération du prénom

        // Gestion du prénom transmis ou par défaut
        if (prenom != null && !prenom.equals("ANONYME")) {
            prenomUtilisateur = prenom; // Utiliser le prénom transmis s'il est valide
        } else {
            prenomUtilisateur = "ANONYME"; // Par défaut à "ANONYME"
        }

        // Récupérer le prénom de l'utilisateur connecté si disponible
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            prenomUtilisateur = documentSnapshot.getString("prenom");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Erreur lors de la récupération du prénom : " + e.getMessage()));
        }

        // Gestion du bouton de paiement
        Button btnPayerRIB = findViewById(R.id.btn_valider);
        btnPayerRIB.setOnClickListener(v -> {
            // Validation des informations IBAN avant de procéder au paiement
            if (validateIbanInformation()) {
                ajouterDocumentDon(Double.parseDouble(montant), association, prenomUtilisateur);

                Toast.makeText(this, "Paiement de " + montant + "€ effectué avec succès via IBAN !", Toast.LENGTH_LONG).show();

                Intent retourIntent = new Intent(this, DonsActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(retourIntent);
                finish();
            }
        });

        // Gestion du bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());
    }

    // Méthode pour valider les informations IBAN
    private boolean validateIbanInformation() {
        boolean isValid = true;

        // Vérification de l'IBAN
        String iban = etIban.getText().toString().trim().replaceAll("\\s", "");
        if (TextUtils.isEmpty(iban)) {
            etIban.setError("Veuillez saisir votre IBAN");
            isValid = false;
        } else if (iban.length() < 14 || iban.length() > 34) {
            etIban.setError("IBAN invalide (14-34 caractères)");
            isValid = false;
        }

        // Vérification du BIC
        String bic = etBic.getText().toString().trim().replaceAll("\\s", "");
        if (TextUtils.isEmpty(bic)) {
            etBic.setError("Veuillez saisir votre BIC/SWIFT");
            isValid = false;
        } else if (bic.length() < 8 || bic.length() > 11) {
            etBic.setError("BIC invalide (8-11 caractères)");
            isValid = false;
        }

        // Vérification du titulaire
        if (TextUtils.isEmpty(etTitulaire.getText().toString().trim())) {
            etTitulaire.setError("Veuillez saisir le nom du titulaire du compte");
            isValid = false;
        }

        // Afficher un message Toast global si des champs sont manquants
        if (!isValid) {
            Toast.makeText(this, "Veuillez vérifier vos informations bancaires", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }

    // Méthode pour ajouter le don dans la base de données Firestore
    private void ajouterDocumentDon(double montant, String association, String prenom) {
        Map<String, Object> donData = new HashMap<>();
        donData.put("association", association);
        donData.put("date", new Timestamp(new Date()));
        donData.put("montant", montant);
        donData.put("prenom", prenom);

        db.collection("dons").add(donData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Don ajouté avec succès avec ID: " + documentReference.getId());
                    Toast.makeText(this, "Don ajouté avec succès !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur lors de l'ajout du don: " + e.getMessage());
                    Toast.makeText(this, "Erreur lors de l'ajout du don", Toast.LENGTH_SHORT).show();
                });
    }
}