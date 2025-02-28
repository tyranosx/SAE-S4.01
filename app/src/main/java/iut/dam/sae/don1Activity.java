package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Don1Activity extends AppCompatActivity {

    private TextView nomAssociationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don1); // Vérifie que ce fichier XML existe bien

        ImageButton btnProfil = findViewById(R.id.btn_profil);

        // Récupérer le nom de l'association sélectionnée
        nomAssociationView = findViewById(R.id.nom_association);
        String nomAssociation = getIntent().getStringExtra("nomAssociation");

        // Afficher le nom de l'association si disponible
        if (nomAssociation != null) {
            nomAssociationView.setText(nomAssociation);
        }

        // Initialisation des boutons
        Button btnOui = findViewById(R.id.btn_don_oui);
        Button btnNon = findViewById(R.id.btn_don_non);
        ImageButton btnRetour = findViewById(R.id.btn_retour);

        // Listener pour le bouton "Oui" (redirection vers Don2Activity)
        btnOui.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don2Activity.class);
            intent.putExtra("nomAssociation", nomAssociation); // Passer le nom à Don2Activity
            startActivity(intent);
        });

        // Listener pour le bouton "Non" (redirection vers Don3Activity)
        btnNon.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation); // Passer le nom à Don3Activity
            startActivity(intent);
        });

        // Listener pour le bouton retour (flèche) (redirection vers DonsActivity)
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, DonsActivity.class);
            startActivity(intent);
            finish(); // Fermer cette activité pour éviter un retour involontaire
        });

        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
