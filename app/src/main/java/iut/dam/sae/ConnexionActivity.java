package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ConnexionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText inputEmail, inputPassword;
    private Button btnConnexion;
    private ImageButton btnRetour;
    private TextView txtMdpOublie, txtSinscrire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Initialisation Firebase Auth et Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Récupérer les éléments UI
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnConnexion = findViewById(R.id.btn_connexion);
        btnRetour = findViewById(R.id.btn_retour);
        txtMdpOublie = findViewById(R.id.txt_mdp_oublie);
        txtSinscrire = findViewById(R.id.txt_sinscrire);

        // Gérer la connexion
        btnConnexion.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(ConnexionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Connexion avec Firebase
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Retourner à LoginChoiceActivity au lieu d'aller directement à AdminActivity
                                Intent intent = new Intent(ConnexionActivity.this, LoginChoiceActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            Toast.makeText(ConnexionActivity.this, "Échec de la connexion : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Redirige vers l'activité "Mot de passe oublié"
        txtMdpOublie.setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Redirige vers l'activité "Inscription"
        txtSinscrire.setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
            startActivity(intent);
        });

        // Bouton retour vers LoginChoiceActivity
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(ConnexionActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });
    }
}