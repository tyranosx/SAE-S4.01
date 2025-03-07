package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaimentCarteActivity extends AppCompatActivity {

    private EditText etNumeroCarte, etDateExpiration, etCVV, etTitulaireCarte;
    private Button btnPayerCarte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_carte);

        // Récupérer le montant passé depuis Don3Activity
        Intent intent = getIntent();
        String montant = intent.getStringExtra("montant");

        // Initialisation des champs
        etNumeroCarte = findViewById(R.id.et_card_number);
        etDateExpiration = findViewById(R.id.et_expiry_date);
        etCVV = findViewById(R.id.et_cvv);
        etTitulaireCarte = findViewById(R.id.et_card_holder);
        btnPayerCarte = findViewById(R.id.btn_payer);

        // Bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish()); // Retour à Don3Activity

        // Gestion du paiement
        btnPayerCarte.setOnClickListener(v -> {
            String numeroCarte = etNumeroCarte.getText().toString().trim();
            String dateExpiration = etDateExpiration.getText().toString().trim();
            String cvv = etCVV.getText().toString().trim();
            String titulaire = etTitulaireCarte.getText().toString().trim();

            // Vérification simple des champs
            if (numeroCarte.isEmpty() || dateExpiration.isEmpty() || cvv.isEmpty() || titulaire.isEmpty()) {
                Toast.makeText(PaimentCarteActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simuler un paiement réussi
            Toast.makeText(PaimentCarteActivity.this, "Paiement de " + montant + "€ effectué avec succès !", Toast.LENGTH_LONG).show();

            // Redirection vers ActivityDon après le paiement
            Intent retourIntent = new Intent(PaimentCarteActivity.this, DonsActivity.class);
            startActivity(retourIntent);
            finish();
        });
    }
}
