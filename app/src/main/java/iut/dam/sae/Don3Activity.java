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

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Don3Activity extends AppCompatActivity {

    private EditText etMontant;
    private String montantSelectionne = "";
    private boolean paiementParCarteSelectionne = false;
    private boolean paiementParIbanSelectionne = false;
    private String nomAssociation = "INCONNU";  // Par défaut en cas d'erreur

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don3);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupération du nom de l'association via Intent
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
            etMontant.setText(montantSelectionne);  // Affiche le montant sélectionné dans l'EditText
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
        btnDonner.setOnClickListener(v -> {
            String montant = etMontant.getText().toString().trim();

            if (montant.isEmpty()) {
                Toast.makeText(Don3Activity.this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                return;
            }

            if (nomAssociation == null || nomAssociation.isEmpty()) {
                Toast.makeText(Don3Activity.this, "Erreur : Nom de l'association manquant", Toast.LENGTH_SHORT).show();
                return;
            }

            // Préparation des données du don
            Map<String, Object> donData = new HashMap<>();
            donData.put("association", nomAssociation);  // Utilisation du bon nom
            donData.put("montant", Integer.parseInt(montant));
            donData.put("prenom", "ANONYME");
            donData.put("date", new Timestamp(new Date()));

            db.collection("dons")
                    .add(donData)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(Don3Activity.this, "Don enregistré avec succès", Toast.LENGTH_SHORT).show();

                        // Redirige vers la page adéquate en fonction du mode de paiement choisi
                        if (paiementParCarteSelectionne) {
                            Intent intent = new Intent(Don3Activity.this, PaimentCarteActivity.class);
                            intent.putExtra("montant", montant);
                            intent.putExtra("nomAssociation", nomAssociation);
                            startActivity(intent);
                        } else if (paiementParIbanSelectionne) {
                            Intent intent = new Intent(Don3Activity.this, PaimentRibActivity.class);
                            intent.putExtra("montant", montant);
                            intent.putExtra("nomAssociation", nomAssociation);
                            startActivity(intent);
                        } else {
                            Toast.makeText(Don3Activity.this, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(Don3Activity.this, "Erreur lors de l'enregistrement du don", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", "Erreur lors de l'ajout du don", e);
                    });
        });

        // Gestion du bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don3Activity.this, Don1Activity.class);
            startActivity(intent);
            finish();
        });

        // Gestion du bouton Profil
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don3Activity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
