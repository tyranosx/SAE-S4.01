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
    private boolean paiementParCarteSelectionne = false;
    private boolean paiementParIbanSelectionne = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don3);

        // Bouton retour qui renvoie à Don1Activity
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don3Activity.this, Don1Activity.class);
            startActivity(intent);
            finish(); // Fermer Don3Activity pour éviter un retour ici après Don1Activity
        });

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

        // Bouton "Donner"
        Button btnDonner = findViewById(R.id.btn_donner);
        btnDonner.setOnClickListener(v -> {
            String montant = etMontant.getText().toString().trim();

            if (montant.isEmpty()) {
                Toast.makeText(Don3Activity.this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
                return;
            }

            // Vérifier quel mode de paiement est sélectionné
            if (paiementParCarteSelectionne) {
                Intent intent = new Intent(Don3Activity.this, PaimentCarteActivity.class);
                intent.putExtra("montant", montant); // Passer le montant à la page suivante
                startActivity(intent);
            } else if (paiementParIbanSelectionne) {
                Intent intent = new Intent(Don3Activity.this, PaimentRibActivity.class);
                intent.putExtra("montant", montant); // Passer le montant à la page suivante
                startActivity(intent);
            } else {
                Toast.makeText(Don3Activity.this, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
            }
        });

        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don3Activity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
