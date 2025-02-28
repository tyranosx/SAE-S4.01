package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Don2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don2); // Assure-toi que le fichier XML est bien nommé

        // Initialisation des boutons
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        Button btnMensuels = findViewById(R.id.btn_don_mensuels);
        Button btnAnnuels = findViewById(R.id.btn_don_annuels);
        ImageButton btnRetour = findViewById(R.id.btn_retour); // Ajout du bouton retour

        // Ajout des listeners pour rediriger vers Don3Activity
        btnMensuels.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don3Activity.class);
            startActivity(intent);
        });

        btnAnnuels.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don3Activity.class);
            startActivity(intent);
        });

        // Gestion du bouton retour qui ramène à Don1Activity
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don1Activity.class);
            startActivity(intent);
            finish(); // Ferme Don2Activity pour éviter un retour en boucle
        });

        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
