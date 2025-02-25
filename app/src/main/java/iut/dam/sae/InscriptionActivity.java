package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InscriptionActivity extends AppCompatActivity {

    private EditText inputPrenom, inputNom, inputEmail, inputPassword;
    private Button btnInscription;
    private ImageButton btnRetour;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Initialisation de Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialisation des vues
        inputPrenom = findViewById(R.id.input_prenom);
        inputNom = findViewById(R.id.input_nom);
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnInscription = findViewById(R.id.btn_inscription);
        btnRetour = findViewById(R.id.btn_retour);

        // Bouton retour à la page précédente
        btnRetour.setOnClickListener(v -> finish());

        // Bouton S'inscrire
        btnInscription.setOnClickListener(v -> {
            String prenom = inputPrenom.getText().toString().trim();
            String nom = inputNom.getText().toString().trim();
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            // Validation des champs
            if (TextUtils.isEmpty(prenom)) {
                inputPrenom.setError("Veuillez entrer votre prénom");
                return;
            }
            if (TextUtils.isEmpty(nom)) {
                inputNom.setError("Veuillez entrer votre nom");
                return;
            }
            if (TextUtils.isEmpty(email) || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.setError("Veuillez entrer un email valide");
                return;
            }
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                inputPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
                return;
            }

            // Inscription de l'utilisateur avec Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Utilisateur créé avec succès
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();

                            // Redirection vers LoginChoiceActivity après inscription
                            Intent intent = new Intent(InscriptionActivity.this, LoginChoiceActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Inscription échouée
                            Toast.makeText(InscriptionActivity.this, "Échec de l'inscription : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
