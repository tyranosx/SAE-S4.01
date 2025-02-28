package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Don3Activity extends AppCompatActivity {

    private EditText etMontant;
    private String montantSelectionne = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don3);

        // Bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());

        // Champs pour entrer un montant personnalisé
        etMontant = findViewById(R.id.et_montant);

        // Boutons de montant prédéfinis
        TextView btn10 = findViewById(R.id.btn_10);
        TextView btn20 = findViewById(R.id.btn_20);
        TextView btn50 = findViewById(R.id.btn_50);

        // Sélection d'un montant prédéfini
        View.OnClickListener montantClickListener = v -> {
            montantSelectionne = ((TextView) v).getText().toString();
            etMontant.setText(montantSelectionne);
        };

        btn10.setOnClickListener(montantClickListener);
        btn20.setOnClickListener(montantClickListener);
        btn50.setOnClickListener(montantClickListener);

        // Mode de paiement
        LinearLayout btnCarte = findViewById(R.id.btn_carte);
        LinearLayout btnIban = findViewById(R.id.btn_iban);

        btnCarte.setOnClickListener(v -> Toast.makeText(this, "Paiement par carte sélectionné", Toast.LENGTH_SHORT).show());
        btnIban.setOnClickListener(v -> Toast.makeText(this, "Paiement par IBAN sélectionné", Toast.LENGTH_SHORT).show());

        // Bouton "Donner"
        Button btnDonner = findViewById(R.id.btn_donner);
        btnDonner.setOnClickListener(v -> {
            String montant = etMontant.getText().toString().trim();

            if (montant.isEmpty()) {
                Toast.makeText(Don3Activity.this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simuler un paiement réussi et redirection
            Toast.makeText(Don3Activity.this, "Don de " + montant + "€ enregistré avec succès", Toast.LENGTH_SHORT).show();

            // Retourner à LoginChoiceActivity après un don
            Intent intent = new Intent(Don3Activity.this, LoginChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
