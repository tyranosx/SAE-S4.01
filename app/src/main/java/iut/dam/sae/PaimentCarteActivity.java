package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PaimentCarteActivity extends AppCompatActivity {

    private EditText etNumeroCarte, etDateExpiration, etCVV, etTitulaireCarte;
    private Button btnPayerCarte;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_carte);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        final String montant = intent.getStringExtra("montant");   // Ajouter 'final'
        final String association = intent.getStringExtra("nomAssociation"); // Ajouter 'final'

        etNumeroCarte = findViewById(R.id.et_card_number);
        etDateExpiration = findViewById(R.id.et_expiry_date);
        etCVV = findViewById(R.id.et_cvv);
        etTitulaireCarte = findViewById(R.id.et_card_holder);
        btnPayerCarte = findViewById(R.id.btn_payer);

        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());

        btnPayerCarte.setOnClickListener(v -> {
            ajouterDocumentDon(Double.parseDouble(montant), association);

            Toast.makeText(this, "Paiement de " + montant + "€ effectué avec succès !", Toast.LENGTH_LONG).show();

            Intent retourIntent = new Intent(this, DonsActivity.class);
            retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(retourIntent);
            finish();
        });
    }

    private void ajouterDocumentDon(double montant, String association) {
        Map<String, Object> donData = new HashMap<>();
        donData.put("association", association);
        donData.put("date", new Timestamp(new Date()));
        donData.put("montant", montant);
        donData.put("prenom", "ANONYME");

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