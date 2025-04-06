package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DonsAnnuelActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerViewDonsAnnuels;
    private MonActiviteDonAdapter monActiviteDonAdapter;
    private List<MonActiviteDonItem> donAnnuelList;
    private TextView txtNombreDonsAnnuels, txtMontantTotalAnnuels, txtPrenomUtilisateur;
    private String prenomUtilisateur = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons_annuel);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewDonsAnnuels = findViewById(R.id.recyclerViewDonsAnnuels);
        recyclerViewDonsAnnuels.setLayoutManager(new LinearLayoutManager(this));

        txtNombreDonsAnnuels = findViewById(R.id.txt_nombre_dons_annuels);
        txtMontantTotalAnnuels = findViewById(R.id.txt_montant_total_annuels);
        txtPrenomUtilisateur = findViewById(R.id.txt_prenom_utilisateur);

        donAnnuelList = new ArrayList<>();
        monActiviteDonAdapter = new MonActiviteDonAdapter(donAnnuelList);
        recyclerViewDonsAnnuels.setAdapter(monActiviteDonAdapter);

        ImageButton btnRetour = findViewById(R.id.btn_retour);
        ImageButton btnProfil = findViewById(R.id.btn_profil);

        // Animations
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        // Titre + Infos utilisateur
        findViewById(R.id.titre_mon_activite).startAnimation(fadeIn);
        findViewById(R.id.txt_prenom_utilisateur).startAnimation(fadeIn);
        findViewById(R.id.titre_dons_annuels).startAnimation(fadeIn);
        findViewById(R.id.txt_nombre_dons_annuels).startAnimation(fadeIn);
        findViewById(R.id.txt_montant_total_annuels).startAnimation(fadeIn);

        // RecyclerView (layout animation)
        recyclerViewDonsAnnuels.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_fade_in)
        );

        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        btnProfil.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            startActivity(new Intent(DonsAnnuelActivity.this, ProfilActivity.class));
        });

        // Récupérer le prénom depuis l'Intent
        prenomUtilisateur = getIntent().getStringExtra("prenom");

        if (prenomUtilisateur != null && !prenomUtilisateur.isEmpty()) {
            txtPrenomUtilisateur.setText("Prénom : " + prenomUtilisateur); // Affichage du prénom
            recupererDonsAnnuels(prenomUtilisateur);
        } else {
            txtPrenomUtilisateur.setText("Prénom : Introuvable");
            Toast.makeText(this, "Prénom introuvable", Toast.LENGTH_SHORT).show();
        }
    }

    private void recupererDonsAnnuels(String prenomUtilisateur) {
        db.collection("dons")
                .whereEqualTo("prenom", prenomUtilisateur)
                .whereEqualTo("category", "donAnnuel") // Filtrer les dons annuels
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donAnnuelList.clear();
                    int totalMontant = 0;
                    int totalDons = 0;

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Aucun don annuel trouvé", Toast.LENGTH_SHORT).show();
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String association = document.getString("association");
                            int montant = document.getLong("montant").intValue();
                            Date date = document.getTimestamp("date").toDate();

                            MonActiviteDonItem don = new MonActiviteDonItem(association, montant, date);
                            donAnnuelList.add(don);

                            totalMontant += montant;
                            totalDons++;
                        }

                        monActiviteDonAdapter.notifyDataSetChanged();

                        // Affichage du total des montants et du nombre de dons
                        txtNombreDonsAnnuels.setText("Nombre total de dons : " + totalDons);
                        txtMontantTotalAnnuels.setText("Montant total des dons : " + totalMontant + "€");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Erreur lors de la récupération des dons annuels", e));
    }
}