package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginChoiceActivity extends AppCompatActivity {

    private Button btnFaireDon, btnSeConnecter, btnSinscrire, btnAdminPanel, btnDeconnexion;
    private ImageButton btnProfil; // Bouton Profil ajouté
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        // Initialisation Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupération des boutons
        btnFaireDon = findViewById(R.id.btn_faire_don);
        btnSeConnecter = findViewById(R.id.btn_se_connecter);
        btnSinscrire = findViewById(R.id.btn_sinscrire);
        btnAdminPanel = findViewById(R.id.btn_admin_panel);
        btnDeconnexion = findViewById(R.id.btn_deconnexion);
        btnProfil = findViewById(R.id.btn_profil); // Bouton Profil ajouté

        // Vérifier l'état de connexion
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Si connecté, cacher les boutons "Se connecter" et "S'inscrire"
            btnSeConnecter.setVisibility(View.GONE);
            btnSinscrire.setVisibility(View.GONE);

            // Afficher les boutons de déconnexion et profil
            btnDeconnexion.setVisibility(View.VISIBLE);
            btnProfil.setVisibility(View.VISIBLE);

            // Vérifier si l'utilisateur est admin dans Firestore
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && Boolean.TRUE.equals(documentSnapshot.getBoolean("isAdmin"))) {
                            btnAdminPanel.setVisibility(View.VISIBLE);  // Si admin, affiche l'accès admin
                        } else {
                            btnAdminPanel.setVisibility(View.GONE); // Sinon, cache l'accès admin
                        }
                    });
        } else {
            // Si non connecté, cacher les boutons admin, déconnexion et profil
            btnAdminPanel.setVisibility(View.GONE);
            btnDeconnexion.setVisibility(View.GONE);
            btnProfil.setVisibility(View.GONE);  // Bouton profil caché pour les non-connectés
        }

        // Rediriger vers DonsActivity
        btnFaireDon.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, DonsActivity.class));
        });

        // Rediriger vers ConnexionActivity
        btnSeConnecter.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, ConnexionActivity.class));
        });

        // Rediriger vers InscriptionActivity
        btnSinscrire.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, InscriptionActivity.class));
        });

        // Rediriger vers AdminActivity
        btnAdminPanel.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, AdminActivity.class));
        });

        // Rediriger vers ProfilActivity
        btnProfil.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, ProfilActivity.class));
        });

        // Déconnexion
        btnDeconnexion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(LoginChoiceActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish(); // Ferme l'activité actuelle après déconnexion
        });
    }
}
