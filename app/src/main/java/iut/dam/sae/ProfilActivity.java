package iut.dam.sae;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfilActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button btnQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mAuth = FirebaseAuth.getInstance();

        // Récupération des boutons
        Button btnConsulterActivite = findViewById(R.id.btn_consulteractivite);
        Button btnConsulterDonsAnnuels = findViewById(R.id.btn_consulter_dons_annuels);
        Button btnConsulterDonsUniques = findViewById(R.id.btn_consulter_dons_uniques);
        Button btnFaireDon = findViewById(R.id.btn_faire_un_don);
        Button btnDeconnexion = findViewById(R.id.btn_deconnexion);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnQrCode = findViewById(R.id.btn_qr_code);
        ImageView logo = findViewById(R.id.logo);

        // ✨ Animations
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        logo.startAnimation(zoomIn);
        btnConsulterActivite.startAnimation(slideUp);
        btnConsulterDonsAnnuels.startAnimation(slideUp);
        btnConsulterDonsUniques.startAnimation(slideUp);
        btnFaireDon.startAnimation(slideUp);
        btnQrCode.startAnimation(slideUp);
        btnDeconnexion.startAnimation(slideUp);

        // Redirection avec prénom
        String prenom = getIntent().getStringExtra("prenom");

        View.OnClickListener redirectWithPrenom = view -> {
            view.startAnimation(clickScale);
            Class<?> target = null;
            if (view == btnConsulterActivite) target = MonActiviteActivity.class;
            else if (view == btnConsulterDonsAnnuels) target = DonsAnnuelActivity.class;
            else if (view == btnConsulterDonsUniques) target = DonsUniqueActivity.class;

            if (target != null) {
                if (prenom != null && !prenom.isEmpty()) {
                    Intent intent = new Intent(this, target);
                    intent.putExtra("prenom", prenom);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Prénom introuvable, retour à l'accueil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginChoiceActivity.class));
                }
            }
        };

        btnConsulterActivite.setOnClickListener(redirectWithPrenom);
        btnConsulterDonsAnnuels.setOnClickListener(redirectWithPrenom);
        btnConsulterDonsUniques.setOnClickListener(redirectWithPrenom);

        btnFaireDon.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            FirebaseUser user = mAuth.getCurrentUser();
            Intent intent = new Intent(this, DonsActivity.class);
            if (user != null && user.getDisplayName() != null) {
                intent.putExtra("prenom", user.getDisplayName());
            }
            startActivity(intent);
        });

        // 🚀 QR Code
        btnQrCode.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            Intent intent = new Intent(this, ProfileQrCodeActivity.class);
            startActivity(intent);
        });

        // 🔙 Retour
        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            startActivity(new Intent(this, LoginChoiceActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
            finish();
        });

        // 🔐 Déconnexion avec popup animée
        btnDeconnexion.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
            builder.setTitle("Déconnexion");
            builder.setMessage("Souhaitez-vous vraiment vous déconnecter ?");
            builder.setPositiveButton("Oui", (dialog, which) -> {
                mAuth.signOut();
                Intent intent = new Intent(this, LoginChoiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("Annuler", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }
}