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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.Timestamp;

import android.util.Log;
import android.widget.Toast;

import java.util.Date;

public class MonActiviteActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerViewDonsMensuels;
    private MonActiviteDonAdapter monActiviteDonAdapter;
    private List<MonActiviteDonItem> donMensuelList;
    private String prenomUtilisateur = "";
    private TextView montantTotalDons;
    private TextView nombreTotalDons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mon_activite);

        montantTotalDons = findViewById(R.id.montant_total_dons);
        nombreTotalDons = findViewById(R.id.nombre_total_dons);

        montantTotalDons.setText("Montant total des dons : 0€");
        nombreTotalDons.setText("Nombre total de dons : 0");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewDonsMensuels = findViewById(R.id.recyclerViewDonsMensuels);
        recyclerViewDonsMensuels.setLayoutManager(new LinearLayoutManager(this));

        donMensuelList = new ArrayList<>();
        monActiviteDonAdapter = new MonActiviteDonAdapter(donMensuelList);
        recyclerViewDonsMensuels.setAdapter(monActiviteDonAdapter);

        ImageButton btnRetour = findViewById(R.id.btn_retour);
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        TextView txtPrenomUtilisateur = findViewById(R.id.txt_prenom_utilisateur);

        // === ANIMATIONS ===
        Animation clickScale = AnimationUtils.loadAnimation(this, R.anim.click_scale);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideIn = AnimationUtils.loadAnimation(this, R.anim.slide_in_bottom);

        // Appliquer les animations aux éléments principaux
        findViewById(R.id.titre_mon_activite).startAnimation(slideIn);
        findViewById(R.id.txt_prenom_utilisateur).startAnimation(fadeIn);
        findViewById(R.id.montant_total_dons).startAnimation(fadeIn);
        findViewById(R.id.nombre_total_dons).startAnimation(fadeIn);

        // Appliquer une animation au RecyclerView → cascade
        recyclerViewDonsMensuels.setLayoutAnimation(
                AnimationUtils.loadLayoutAnimation(this, R.anim.layout_fade_in)
        );


        btnRetour.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

        btnProfil.setOnClickListener(v -> {
            v.startAnimation(clickScale);
            startActivity(new Intent(MonActiviteActivity.this, ProfilActivity.class));
        });

        // Récupérer le prénom depuis l'Intent
        prenomUtilisateur = getIntent().getStringExtra("prenom");

        if (prenomUtilisateur != null && !prenomUtilisateur.isEmpty()) {
            txtPrenomUtilisateur.setText("Prénom : " + prenomUtilisateur); // Affichage du prénom
            recupererDonsMensuels(prenomUtilisateur);
        } else {
            txtPrenomUtilisateur.setText("Prénom : Introuvable");
            Toast.makeText(this, "Prénom introuvable", Toast.LENGTH_SHORT).show();
        }
    }

    private void recupererDonsMensuels(String prenomUtilisateur) {
        db.collection("dons")
                .whereEqualTo("prenom", prenomUtilisateur)
                .whereEqualTo("category", "donMensuel") // Filtrer les dons mensuels
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donMensuelList.clear();
                    int totalMontant = 0;
                    int totalDons = 0; // Compteur du nombre total de dons

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Aucun don mensuel trouvé", Toast.LENGTH_SHORT).show();
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String association = document.getString("association");
                            int montant = document.getLong("montant").intValue();
                            Date date = document.getTimestamp("date").toDate();

                            MonActiviteDonItem don = new MonActiviteDonItem(association, montant, date);
                            donMensuelList.add(don);

                            totalMontant += montant;
                            totalDons++;
                            montantTotalDons.setText("Montant total des dons : " + totalMontant + "€");
                            nombreTotalDons.setText("Nombre total de dons : " + totalDons);
                            Log.d("DonMensuel", "Association: " + association + " | Montant: " + montant + "€ | Date: " + date);
                        }

                        monActiviteDonAdapter.notifyDataSetChanged();

                        // Affichage du total des montants et du nombre de dons
                        Toast.makeText(this, "Total des dons mensuels : " + totalDons + " dons | Montant total : " + totalMontant + "€", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Erreur lors de la récupération des dons mensuels", e));
    }
}
