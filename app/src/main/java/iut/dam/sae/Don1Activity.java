package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Don1Activity extends AppCompatActivity {

    private TextView nomAssociationView;
    private String nomAssociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don1);

        ImageButton btnProfil = findViewById(R.id.btn_profil);
        nomAssociationView = findViewById(R.id.nom_association);

        // Récupération du nom de l'association
        nomAssociation = getIntent().getStringExtra("nomAssociation");

        if (nomAssociation != null) {
            nomAssociationView.setText(nomAssociation);
        } else {
            nomAssociationView.setText("Nom de l'association inconnu");
        }

        Button btnOui = findViewById(R.id.btn_don_oui);
        Button btnNon = findViewById(R.id.btn_don_non);
        ImageButton btnRetour = findViewById(R.id.btn_retour);

        btnOui.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don2Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("category", "donRegulier");  // Catégorie pour les dons réguliers
            startActivity(intent);
        });

        btnNon.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("category", "donUnique");  // Catégorie pour les dons uniques
            startActivity(intent);
        });

        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, DonsActivity.class);
            startActivity(intent);
            finish();
        });

        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
