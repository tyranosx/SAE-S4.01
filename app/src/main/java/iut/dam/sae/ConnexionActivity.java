package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConnexionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);

        // Bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Ferme l'activité actuelle et retourne en arrière
            }
        });

        // Champs de saisie
        EditText inputEmail = findViewById(R.id.input_email);
        EditText inputPassword = findViewById(R.id.input_password);

        // Bouton Connexion
        Button btnConnexion = findViewById(R.id.btn_connexion);
        btnConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (email.equals("test@example.com") && password.equals("password123")) {
                    Intent intent = new Intent(ConnexionActivity.this, LoginChoiceActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConnexionActivity.this, "Connexion incorrecte", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Mot de passe oublié
        TextView txtMdpOublie = findViewById(R.id.txt_mdp_oublie);
        txtMdpOublie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Redirection vers l'inscription
        TextView txtSinscrire = findViewById(R.id.txt_sinscrire);
        txtSinscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnexionActivity.this, InscriptionActivity.class);
                startActivity(intent);
            }
        });
    }
}
