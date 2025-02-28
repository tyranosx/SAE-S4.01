package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private Button btnSinscrire;
    private ImageButton btnRetour; // Ajout du bouton retour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Récupérer les éléments de l'interface utilisateur
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnSinscrire = findViewById(R.id.btn_inscription);
        btnRetour = findViewById(R.id.btn_retour); // Initialisation du bouton retour

        // Gérer l'inscription
        btnSinscrire.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Création de l'utilisateur avec Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Inscription réussie
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InscriptionActivity.this, LoginChoiceActivity.class));
                            finish();
                        } else {
                            // Inscription échouée
                            Toast.makeText(InscriptionActivity.this, "Inscription échouée : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Gérer le retour à LoginChoiceActivity
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(InscriptionActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });
    }
}