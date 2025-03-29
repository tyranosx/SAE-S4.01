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
        ImageView logo = findViewById(R.id.logo);
        TextView introText = findViewById(R.id.intro_text);

        // 🎞️ Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);

        logo.startAnimation(zoomIn);
        introText.startAnimation(slideUp);
        btnMensuels.startAnimation(fadeIn);
        btnAnnuels.startAnimation(fadeIn);

        // 👇 Boutons listeners
        btnMensuels.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            Intent intent = new Intent(Don2Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("category", "donMensuel");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        btnAnnuels.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            Intent intent = new Intent(Don2Activity.this, Don3Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            intent.putExtra("category", "donAnnuel");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            Intent intent = new Intent(Don2Activity.this, Don1Activity.class);
            intent.putExtra("nomAssociation", nomAssociation);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
            finish();
        });

        btnProfil.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            Intent intent = new Intent(Don2Activity.this, ProfilActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }
}