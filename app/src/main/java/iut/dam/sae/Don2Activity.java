package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Don2Activity extends AppCompatActivity {

    private String nomAssociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don2);

        nomAssociation = getIntent().getStringExtra("nomAssociation");

        ImageButton btnProfil = findViewById(R.id.btn_profil);
        Button btnMensuels = findViewById(R.id.btn_don_mensuels);
        Button btnAnnuels = findViewById(R.id.btn_don_annuels);
        ImageButton btnRetour = findViewById(R.id.btn_retour);

        btnMensuels.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            startActivity(intent);
        });

        btnAnnuels.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            startActivity(intent);
        });

        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, Don1Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            startActivity(intent);
            finish();
        });

        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don2Activity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }
}
