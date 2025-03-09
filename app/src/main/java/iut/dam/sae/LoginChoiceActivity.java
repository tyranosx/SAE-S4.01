package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginChoiceActivity extends AppCompatActivity {

    private Button btnFaireDon, btnSeConnecter, btnSinscrire, btnAdminPanel, btnDeconnexion;
    private ImageButton btnProfil;
    private TextView txtBienvenue; // Nouveau TextView pour le message de bienvenue
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupération des boutons
        btnFaireDon = findViewById(R.id.btn_faire_don);
        btnSeConnecter = findViewById(R.id.btn_se_connecter);
        btnSinscrire = findViewById(R.id.btn_sinscrire);
        btnAdminPanel = findViewById(R.id.btn_admin_panel);
        btnDeconnexion = findViewById(R.id.btn_deconnexion);
        btnProfil = findViewById(R.id.btn_profil);  // Bouton profil
        txtBienvenue = findViewById(R.id.txt_bienvenue);  // Nouveau TextView

        // Vérifier l'état de connexion
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Si connecté, cacher les boutons "Se connecter" et "S'inscrire"
            btnSeConnecter.setVisibility(View.GONE);
            btnSinscrire.setVisibility(View.GONE);

            // Afficher le bouton de déconnexion et le bouton profil
            btnDeconnexion.setVisibility(View.VISIBLE);
            btnProfil.setVisibility(View.VISIBLE);

            // Récupérer les données de Firestore pour afficher le prénom
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String prenom = documentSnapshot.getString("prenom");
                            if (prenom != null && !prenom.isEmpty()) {
                                txtBienvenue.setText("Bienvenue, " + prenom);
                                txtBienvenue.setVisibility(View.VISIBLE); // Affiche le message de bienvenue
                            }
                        }
                    });

            // Vérification du statut admin
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && Boolean.TRUE.equals(documentSnapshot.getBoolean("isAdmin"))) {
                            btnAdminPanel.setVisibility(View.VISIBLE);
                        } else {
                            btnAdminPanel.setVisibility(View.GONE);
                        }
                    });

        } else {
            // Si aucun utilisateur n'est connecté, cacher certains boutons
            btnAdminPanel.setVisibility(View.GONE);
            btnDeconnexion.setVisibility(View.GONE);
            btnProfil.setVisibility(View.GONE);
            txtBienvenue.setVisibility(View.GONE); // Cache le message de bienvenue
        }

        // Gestion des redirections
        btnFaireDon.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, DonsActivity.class)));
        btnSeConnecter.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, ConnexionActivity.class)));
        btnSinscrire.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, InscriptionActivity.class)));
        btnAdminPanel.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, AdminActivity.class)));

        // Déconnexion
        btnDeconnexion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(LoginChoiceActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });

        // Rediriger vers ProfilActivity via le bouton Profil
        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(LoginChoiceActivity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
