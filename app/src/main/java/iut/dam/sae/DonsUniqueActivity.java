package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class DonsUniqueActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerViewDonsUniques;
    private MonActiviteDonAdapter monActiviteDonAdapter;
    private List<MonActiviteDonItem> donUniqueList;
    private String prenomUtilisateur = "";
    private TextView montantTotalDons;
    private TextView nombreTotalDons;
    private TextView txtPrenomUtilisateur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons_unique);

        // Initialisation des éléments UI
        montantTotalDons = findViewById(R.id.montant_total_dons);
        nombreTotalDons = findViewById(R.id.nombre_total_dons);
        txtPrenomUtilisateur = findViewById(R.id.txt_prenom_utilisateur);

        montantTotalDons.setText("Montant total des dons : 0€");
        nombreTotalDons.setText("Nombre total de dons : 0");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerViewDonsUniques = findViewById(R.id.recyclerViewDonsUniques);
        recyclerViewDonsUniques.setLayoutManager(new LinearLayoutManager(this));

        donUniqueList = new ArrayList<>();
        monActiviteDonAdapter = new MonActiviteDonAdapter(donUniqueList);
        recyclerViewDonsUniques.setAdapter(monActiviteDonAdapter);

        ImageButton btnRetour = findViewById(R.id.btn_retour);
        ImageButton btnProfil = findViewById(R.id.btn_profil);

        btnRetour.setOnClickListener(v -> finish());

        btnProfil.setOnClickListener(v -> {
            startActivity(new Intent(DonsUniqueActivity.this, ProfilActivity.class));
        });

        // Récupérer le prénom depuis l'Intent
        prenomUtilisateur = getIntent().getStringExtra("prenom");

        if (prenomUtilisateur != null && !prenomUtilisateur.isEmpty()) {
            txtPrenomUtilisateur.setText("Prénom : " + prenomUtilisateur); // Affichage du prénom
            recupererDonsUniques(prenomUtilisateur);
        } else {
            txtPrenomUtilisateur.setText("Prénom : Introuvable");
            Toast.makeText(this, "Prénom introuvable", Toast.LENGTH_SHORT).show();
        }
    }

    private void recupererDonsUniques(String prenomUtilisateur) {
        db.collection("dons")
                .whereEqualTo("prenom", prenomUtilisateur)
                .whereEqualTo("category", "donUnique") // Filtrer les dons uniques
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donUniqueList.clear();
                    int totalMontant = 0;
                    int totalDons = 0;

                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(this, "Aucun don unique trouvé", Toast.LENGTH_SHORT).show();
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String association = document.getString("association");
                            int montant = document.getLong("montant").intValue();
                            Date date = document.getTimestamp("date").toDate();

                            MonActiviteDonItem don = new MonActiviteDonItem(association, montant, date);
                            donUniqueList.add(don);

                            totalMontant += montant;
                            totalDons++;
                        }

                        monActiviteDonAdapter.notifyDataSetChanged();

                        // Mise à jour du total des montants et du nombre de dons
                        montantTotalDons.setText("Montant total des dons : " + totalMontant + "€");
                        nombreTotalDons.setText("Nombre total de dons : " + totalDons);
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Erreur lors de la récupération des dons uniques", e));
    }
}