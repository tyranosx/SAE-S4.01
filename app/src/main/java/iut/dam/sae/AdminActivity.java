package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Récupérer l’icône admin et ajouter un listener pour aller à ProfilActivity
        ImageButton iconAdmin = findViewById(R.id.icon_admin);
        iconAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProfilActivity.class);
            startActivity(intent);
        });

        // Ajout du bouton retour qui renvoie à LoginChoiceActivity
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish(); // Fermer AdminActivity pour éviter de revenir dessus après retour
        });
    }
}
