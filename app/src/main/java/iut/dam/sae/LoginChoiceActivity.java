package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginChoiceActivity extends AppCompatActivity {

    private Button btnFaireDon, btnSeConnecter, btnSinscrire, btnAdmin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        // Initialiser Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupération des boutons
        btnFaireDon = findViewById(R.id.btn_faire_don);
        btnSeConnecter = findViewById(R.id.btn_se_connecter);
        btnSinscrire = findViewById(R.id.btn_sinscrire);
        btnAdmin = findViewById(R.id.btn_admin_panel); // Ajout du bouton Admin (doit être dans l'XML)

        // Vérifier si un utilisateur est connecté
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // Masquer les boutons de connexion/inscription si déjà connecté
            btnSeConnecter.setVisibility(View.GONE);
            btnSinscrire.setVisibility(View.GONE);

            // Vérifier si l'utilisateur est un admin
            checkIfUserIsAdmin(user.getUid());
        } else {
            // Cacher le bouton admin si personne n'est connecté
            btnAdmin.setVisibility(View.GONE);
        }

        // Gestion des clics
        btnFaireDon.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                startActivity(new Intent(LoginChoiceActivity.this, DonsActivity.class));
            } else {
                Toast.makeText(LoginChoiceActivity.this, "Veuillez vous connecter pour faire un don", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginChoiceActivity.this, ConnexionActivity.class));
            }
        });

        btnSeConnecter.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, ConnexionActivity.class)));

        btnSinscrire.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, InscriptionActivity.class)));

        btnAdmin.setOnClickListener(v -> startActivity(new Intent(LoginChoiceActivity.this, AdminActivity.class)));
    }

    private void checkIfUserIsAdmin(String userId) {
        db.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DocumentSnapshot document = task.getResult();
                if (document.exists() && Boolean.TRUE.equals(document.getBoolean("isAdmin"))) {
                    btnAdmin.setVisibility(View.VISIBLE);
                } else {
                    btnAdmin.setVisibility(View.GONE);
                }
            }
        });
    }
}
