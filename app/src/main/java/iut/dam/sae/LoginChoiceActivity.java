package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginChoiceActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnFaireDon, btnSeConnecter, btnSinscrire, btnAdminPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Récupération des boutons
        btnFaireDon = findViewById(R.id.btn_faire_don);
        btnSeConnecter = findViewById(R.id.btn_se_connecter);
        btnSinscrire = findViewById(R.id.btn_sinscrire);
        btnAdminPanel = findViewById(R.id.btn_admin_panel); // Ajout du bouton admin

        // Vérifier si l'utilisateur est connecté
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // Désactiver le bouton "Faire un don" si l'utilisateur n'est pas connecté
            btnFaireDon.setEnabled(false);
            btnFaireDon.setOnClickListener(v -> {
                Toast.makeText(this, "Vous devez être connecté pour faire un don", Toast.LENGTH_SHORT).show();
            });

            // Masquer le bouton Admin Panel si non connecté
            btnAdminPanel.setVisibility(View.GONE);
        } else {
            // Activer le bouton "Faire un don"
            btnFaireDon.setEnabled(true);
            btnFaireDon.setOnClickListener(v -> {
                Intent intent = new Intent(LoginChoiceActivity.this, DonsActivity.class);
                startActivity(intent);
            });

            // Vérifier si l'utilisateur est un administrateur (Exemple : Vérifier l'email)
            String email = currentUser.getEmail();
            if (email != null && email.equals("admin@example.com")) { // Remplace par les vrais emails admin
                btnAdminPanel.setVisibility(View.VISIBLE);
                btnAdminPanel.setOnClickListener(v -> {
                    Intent intent = new Intent(LoginChoiceActivity.this, AdminActivity.class);
                    startActivity(intent);
                });
            } else {
                btnAdminPanel.setVisibility(View.GONE);
            }
        }

        // Gestion des autres boutons
        btnSeConnecter.setOnClickListener(v -> {
            Intent intent = new Intent(LoginChoiceActivity.this, ConnexionActivity.class);
            startActivity(intent);
        });

        btnSinscrire.setOnClickListener(v -> {
            Intent intent = new Intent(LoginChoiceActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });
    }
}
