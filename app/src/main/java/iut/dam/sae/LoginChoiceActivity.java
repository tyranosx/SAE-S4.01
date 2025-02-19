package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class LoginChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choice);

        // Récupération des boutons
        Button btnFaireDon = findViewById(R.id.btn_faire_don);
        Button btnSeConnecter = findViewById(R.id.btn_se_connecter);
        Button btnSinscrire = findViewById(R.id.btn_sinscrire);

        // Ajout des listeners avec lambdas
        btnFaireDon.setOnClickListener(v -> {
            Intent intent = new Intent(LoginChoiceActivity.this, don1Activity.class);
            startActivity(intent);
        });

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
