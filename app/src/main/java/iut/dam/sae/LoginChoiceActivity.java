package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginChoiceActivity extends AppCompatActivity {

    private Button btnFaireDon, btnSeConnecter, btnSinscrire, btnAdminPanel, btnDeconnexion;
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
        btnDeconnexion = findViewById(R.id.btn_deconnexion); // Bouton Déconnexion

        // Vérifier l'état de connexion
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Si connecté, cacher les boutons "Se connecter" et "S'inscrire"
            btnSeConnecter.setVisibility(View.GONE);
            btnSinscrire.setVisibility(View.GONE);

            // Afficher le bouton de déconnexion
            btnDeconnexion.setVisibility(View.VISIBLE);

            // Vérifier si l'utilisateur est admin dans Firestore
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && Boolean.TRUE.equals(documentSnapshot.getBoolean("isAdmin"))) {
                            // Si c'est un admin, afficher "Accès Admin"
                            btnAdminPanel.setVisibility(View.VISIBLE);
                        } else {
                            // Si ce n'est pas un admin, cacher "Accès Admin"
                            btnAdminPanel.setVisibility(View.GONE);
                        }
                    });
        } else {
            // Si aucun utilisateur connecté, cacher "Accès Admin" et le bouton de déconnexion
            btnAdminPanel.setVisibility(View.GONE);
            btnDeconnexion.setVisibility(View.GONE);
        }

        // Rediriger vers l'activité de dons
        btnFaireDon.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, DonsActivity.class));
        });

        // Rediriger vers la page de connexion
        btnSeConnecter.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, ConnexionActivity.class));
        });

        // Rediriger vers la page d'inscription
        btnSinscrire.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, InscriptionActivity.class));
        });

        // Rediriger vers le panneau admin
        btnAdminPanel.setOnClickListener(v -> {
            startActivity(new Intent(LoginChoiceActivity.this, AdminActivity.class));
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
