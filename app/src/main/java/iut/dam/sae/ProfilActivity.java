package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ProfilActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Initialisation de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Récupération des boutons
        Button btnConsulterActivite = findViewById(R.id.btn_consulteractivite);
        Button btnFaireDon = findViewById(R.id.btn_faire_un_non);
        Button btnDeconnexion = findViewById(R.id.btn_deconnexion);
        ImageButton btnRetour = findViewById(R.id.btn_retour);

        // Rediriger vers MonActiviteActivity
        btnConsulterActivite.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, MonActiviteActivity.class);
            startActivity(intent);
        });

        // Rediriger vers LoginChoiceActivity
        btnFaireDon.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
        });

        // Déconnexion
        btnDeconnexion.setOnClickListener(v -> {
            mAuth.signOut(); // Déconnexion Firebase
            Toast.makeText(ProfilActivity.this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show();

            // Rediriger vers LoginChoiceActivity après déconnexion
            Intent intent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Efface l'historique de navigation
            startActivity(intent);
            finish(); // Fermer ProfilActivity
        });

        // Bouton retour pour fermer l'activité actuelle
        btnRetour.setOnClickListener(v -> finish());
    }
}
