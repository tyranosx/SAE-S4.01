package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaimentRibActivity extends AppCompatActivity {

    private EditText etIBAN, etBIC, etTitulaireCompte;
    private Button btnPayerRIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_rib);

        // Récupérer le montant depuis Don3Activity
        Intent intent = getIntent();
        String montant = intent.getStringExtra("montant");

        // Initialisation des champs
        etIBAN = findViewById(R.id.et_iban);
        etBIC = findViewById(R.id.et_bic);
        etTitulaireCompte = findViewById(R.id.et_titulaire);
        btnPayerRIB = findViewById(R.id.btn_valider);

        // Bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish()); // Retour à Don3Activity

        // Gestion du paiement
        btnPayerRIB.setOnClickListener(v -> {
            String iban = etIBAN.getText().toString().trim();
            String bic = etBIC.getText().toString().trim();
            String titulaire = etTitulaireCompte.getText().toString().trim();

            // Vérification simple des champs
            if (iban.isEmpty() || bic.isEmpty() || titulaire.isEmpty()) {
                Toast.makeText(PaimentRibActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simuler un paiement réussi
            Toast.makeText(PaimentRibActivity.this, "Paiement de " + montant + "€ effectué avec succès via IBAN !", Toast.LENGTH_LONG).show();

            // Redirection vers ActivityDon après le paiement
            Intent retourIntent = new Intent(PaimentRibActivity.this, DonsActivity.class);
            startActivity(retourIntent);
            finish();
        });
    }
}
