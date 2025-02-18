package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class InscriptionActivity extends AppCompatActivity {

    private EditText inputPrenom, inputNom, inputEmail, inputDateNaissance, inputPassword;
    private Button btnInscription;
    private ImageButton btnRetour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        // Initialisation des vues
        inputPrenom = findViewById(R.id.input_prenom);
        inputNom = findViewById(R.id.input_nom);
        inputEmail = findViewById(R.id.input_email);
        inputDateNaissance = findViewById(R.id.input_date_naissance);
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
            String dateNaissance = inputDateNaissance.getText().toString().trim();
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
            if (TextUtils.isEmpty(dateNaissance)) {
                inputDateNaissance.setError("Veuillez entrer votre date de naissance");
                return;
            }
            if (TextUtils.isEmpty(password) || password.length() < 6) {
                inputPassword.setError("Le mot de passe doit contenir au moins 6 caractères");
                return;
            }

            // Inscription réussie (Simulation)
            Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();

            // Redirection vers la page de connexion après inscription
            Intent intent = new Intent(InscriptionActivity.this, ConnexionActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
