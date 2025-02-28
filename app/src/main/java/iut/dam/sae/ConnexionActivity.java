package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ConnexionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword;
    private Button btnConnexion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Récupérer les éléments de l'interface utilisateur
        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        btnConnexion = findViewById(R.id.btn_connexion);

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
                            // Connexion réussie
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(ConnexionActivity.this, "Connexion réussie !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ConnexionActivity.this, LoginChoiceActivity.class));
                            finish();
                        } else {
                            // Connexion échouée
                            Toast.makeText(ConnexionActivity.this, "Échec de la connexion : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}