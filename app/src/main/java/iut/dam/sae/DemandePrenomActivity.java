package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DemandePrenomActivity extends AppCompatActivity {

    private EditText etPrenom;
    private Button btnValider, btnAnonyme;
    private ImageButton btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demande_prenom);

        etPrenom = findViewById(R.id.et_prenom);
        btnValider = findViewById(R.id.btn_valider);
        btnAnonyme = findViewById(R.id.btn_anonyme);
        btnRetour = findViewById(R.id.btn_retour);

        // Gestion du bouton retour
        btnRetour.setOnClickListener(v -> finish());

        // Gestion du bouton "Valider"
        btnValider.setOnClickListener(v -> {
            String prenom = etPrenom.getText().toString().trim();

            if (prenom.isEmpty()) {
                Toast.makeText(DemandePrenomActivity.this, "Veuillez entrer votre prénom", Toast.LENGTH_SHORT).show();
                return;
            }

            // Redirige vers DonsActivity avec le prénom choisi
            Intent intent = new Intent(DemandePrenomActivity.this, DonsActivity.class);
            intent.putExtra("prenom", prenom);
            startActivity(intent);
            finish();
        });

        // Gestion du bouton "Don Anonyme"
        btnAnonyme.setOnClickListener(v -> {
            Intent intent = new Intent(DemandePrenomActivity.this, DonsActivity.class);
            intent.putExtra("prenom", "ANONYME");  // Transmettre "ANONYME"
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Supprimer les activités précédentes
            startActivity(intent);
            finish(); // Termine l'activité actuelle pour éviter qu'elle ne se relance
        });

    }
}
