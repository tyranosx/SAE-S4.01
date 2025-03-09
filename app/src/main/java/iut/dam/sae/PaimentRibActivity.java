package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaimentRibActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String prenomUtilisateur = "ANONYME"; // Valeur par défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_rib);

        db = FirebaseFirestore.getInstance();

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
            ajouterDocumentDon(Double.parseDouble(montant), association, prenomUtilisateur);

            Toast.makeText(this, "Paiement de " + montant + "€ effectué avec succès via IBAN !", Toast.LENGTH_LONG).show();

            Intent retourIntent = new Intent(this, DonsActivity.class);
            retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(retourIntent);
            finish();
        });

        // Gestion du bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());
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