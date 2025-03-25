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
        btnProfil = findViewById(R.id.btn_profil);
        txtBienvenue = findViewById(R.id.txt_bienvenue);

        // Vérifier l'état de connexion
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            btnSeConnecter.setVisibility(View.GONE);
            btnSinscrire.setVisibility(View.GONE);
            btnDeconnexion.setVisibility(View.VISIBLE);
            btnProfil.setVisibility(View.VISIBLE);

            // Récupération du prénom
            db.collection("users").document(currentUser.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String prenom = documentSnapshot.getString("prenom");
                            if (prenom != null && !prenom.isEmpty()) {
                                txtBienvenue.setText("Bienvenue, " + prenom);
                                txtBienvenue.setVisibility(View.VISIBLE);
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
            btnAdminPanel.setVisibility(View.GONE);
            btnDeconnexion.setVisibility(View.GONE);
            btnProfil.setVisibility(View.GONE);
            txtBienvenue.setVisibility(View.GONE);
        }

        // Gestion des redirections
        btnFaireDon.setOnClickListener(v -> {
            if (currentUser != null) {
                // Si connecté → Envoyer directement vers Don3Activity
                Intent intent = new Intent(LoginChoiceActivity.this, DonsActivity.class);
                startActivity(intent);
            } else {
                // Si non connecté → Demander prénom ou don anonyme
                Intent intent = new Intent(LoginChoiceActivity.this, DemandePrenomActivity.class);
                startActivity(intent);
            }
        });

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
            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                db.collection("users").document(user.getUid()).get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if (documentSnapshot.exists()) {
                                String prenom = documentSnapshot.getString("prenom");
                                Intent intent = new Intent(LoginChoiceActivity.this, ProfilActivity.class);

                                // Envoi du prénom via l'intent
                                intent.putExtra("prenom", prenom);

                                // Empêche ProfilActivity de se relancer plusieurs fois
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                startActivity(intent);
                            } else {
                                Toast.makeText(this, "Prénom introuvable", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Erreur lors de la récupération du prénom", Toast.LENGTH_SHORT).show();

                            // Empêche les doublons d'activités
                            Intent intent = new Intent(LoginChoiceActivity.this, ProfilActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                        });
            } else {
                Intent intent = new Intent(LoginChoiceActivity.this, ProfilActivity.class);

                // Empêche les doublons d'activités
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }
}
