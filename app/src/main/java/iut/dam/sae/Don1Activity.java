package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Don1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don1); // Assure-toi que le nom du fichier XML est correct

        // Initialisation des boutons
        Button btnOui = findViewById(R.id.btn_don_oui);
        Button btnNon = findViewById(R.id.btn_don_non);


        // Ajout des listeners avec lambdas
        btnOui.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don2Activity.class);
            startActivity(intent);
        });

        btnNon.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don2Activity.class);
            startActivity(intent);
        });
    }
}