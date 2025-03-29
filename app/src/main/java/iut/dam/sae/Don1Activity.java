package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Don1Activity extends AppCompatActivity {

    private TextView nomAssociationView;
    private String nomAssociation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don1);

        ImageButton btnProfil = findViewById(R.id.btn_profil);
        nomAssociationView = findViewById(R.id.nom_association);
        TextView introText = findViewById(R.id.intro_text);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        ImageView logo = findViewById(R.id.logo);
        Button btnOui = findViewById(R.id.btn_don_oui);
        Button btnNon = findViewById(R.id.btn_don_non);

        // 🎞️ Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        logo.startAnimation(zoomIn);
        nomAssociationView.startAnimation(fadeIn);
        introText.startAnimation(slideUp);
        btnOui.startAnimation(fadeIn);
        btnNon.startAnimation(fadeIn);

        // 🔁 Récupérer l’association
        nomAssociation = getIntent().getStringExtra("nomAssociation");

        if (nomAssociation != null) {
            nomAssociationView.setText(nomAssociation);
        } else {
            nomAssociationView.setText("Nom de l'association inconnu");
        }

        // ✅ Oui → Don2Activity
        btnOui.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don2Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("category", "donRegulier");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        // ❌ Non → Don3Activity
        btnNon.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("category", "donUnique");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        // 🔙 Retour → DonsActivity
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, DonsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
            finish();
        });

        // 👤 Profil
        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(Don1Activity.this, ProfilActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }
}