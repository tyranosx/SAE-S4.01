package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.View;
import android.view.View.OnClickListener;

public class ProfilActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // Initialisation de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Récupération des boutons
        Button btnConsulterActivite = findViewById(R.id.btn_consulteractivite);
        Button btnConsulterDonsAnnuels = findViewById(R.id.btn_consulter_dons_annuels);
        Button btnConsulterDonsUniques = findViewById(R.id.btn_consulter_dons_uniques); // Nouveau bouton
        Button btnFaireDon = findViewById(R.id.btn_faire_un_don);
        Button btnDeconnexion = findViewById(R.id.btn_deconnexion);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        Button btnQrCode = findViewById(R.id.btn_qr_code);

        // Rediriger vers MonActiviteActivity
        btnConsulterActivite.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, MonActiviteActivity.class);
            String prenom = getIntent().getStringExtra("prenom");

            if (prenom != null && !prenom.isEmpty()) {
                intent.putExtra("prenom", prenom); // Transmission du prénom
                startActivity(intent);
            } else {
                Toast.makeText(this, "Prénom introuvable, retour à l'accueil", Toast.LENGTH_SHORT).show();
                Intent retourIntent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(retourIntent);
            }
        });

        // Rediriger vers DonsAnnuelActivity
        btnConsulterDonsAnnuels.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, DonsAnnuelActivity.class);
            String prenom = getIntent().getStringExtra("prenom");

            if (prenom != null && !prenom.isEmpty()) {
                intent.putExtra("prenom", prenom); // Transmission du prénom
                startActivity(intent);
            } else {
                Toast.makeText(this, "Prénom introuvable, retour à l'accueil", Toast.LENGTH_SHORT).show();
                Intent retourIntent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(retourIntent);
            }
        });

        // Rediriger vers DonsUniqueActivity
        btnConsulterDonsUniques.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, DonsUniqueActivity.class);
            String prenom = getIntent().getStringExtra("prenom");

            if (prenom != null && !prenom.isEmpty()) {
                intent.putExtra("prenom", prenom); // Transmission du prénom
                startActivity(intent);
            } else {
                Toast.makeText(this, "Prénom introuvable, retour à l'accueil", Toast.LENGTH_SHORT).show();
                Intent retourIntent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(retourIntent);
            }
        });

        // Rediriger vers DonsActivity
        btnFaireDon.setOnClickListener(v -> {
            FirebaseUser user = mAuth.getCurrentUser();
            Intent intent = new Intent(ProfilActivity.this, DonsActivity.class);

            if (user != null) {
                intent.putExtra("prenom", user.getDisplayName() != null ? user.getDisplayName() : "Utilisateur");
            }

            startActivity(intent);
        });

        // Déconnexion
        btnDeconnexion.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(ProfilActivity.this, "Déconnecté avec succès", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        // Bouton retour -> Redirige vers LoginChoiceActivity
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });

        // Gestion du clic sur le bouton QR Code
        btnQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir l'activité de génération de QR Code
                Intent intent = new Intent(ProfilActivity.this, ProfileQrCodeActivity.class);
                startActivity(intent);
            }
        });
    }
}