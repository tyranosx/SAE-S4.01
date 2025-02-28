package iut.dam.sae;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class don2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don2); // Assure-toi que le nom du fichier XML est correct

        // Initialisation des boutons
        Button btnMensuels = findViewById(R.id.btn_don_mensuels);
        Button btnAnnuels = findViewById(R.id.btn_don_annuels);


        // Ajout des listeners avec lambdas
        btnMensuels.setOnClickListener(v -> {
            Intent intent = new Intent(don2Activity.this, don1Activity.class);
            startActivity(intent);
        });

        btnAnnuels.setOnClickListener(v -> {
            Intent intent = new Intent(don2Activity.this, don1Activity.class);
            startActivity(intent);
        });
    }
}