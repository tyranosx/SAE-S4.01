package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Don2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don2); // Assure-toi que le nom du fichier XML est correct

        // Initialisation des boutons
        Button btnMensuels = findViewById(R.id.btn_don_mensuels);
        Button btnAnnuels = findViewById(R.id.btn_don_annuels);


        // Ajout des listeners avec lambdas
        btnMensuels.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don1Activity.class);
            startActivity(intent);
        });

        btnAnnuels.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don1Activity.class);
            startActivity(intent);
        });
    }
}
