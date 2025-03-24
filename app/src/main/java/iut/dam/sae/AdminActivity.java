package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private DonAdapter donAdapter;
    private List<DonItem> donList;
    private String adminAssociation;
    private TextView titreAssociation;
    private TextView montantTotalDons;
    private TextView nombreTotalDons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ImageButton iconAdmin = findViewById(R.id.icon_admin);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        titreAssociation = findViewById(R.id.nom_association_admin);
        recyclerView = findViewById(R.id.recyclerViewDons);
        montantTotalDons = findViewById(R.id.montant_total_dons);
        nombreTotalDons = findViewById(R.id.nombre_total_dons);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donList = new ArrayList<>();
        donAdapter = new DonAdapter(donList);
        recyclerView.setAdapter(donAdapter);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && Boolean.TRUE.equals(documentSnapshot.getBoolean("isAdmin"))) {
                            adminAssociation = documentSnapshot.getString("association");
                            titreAssociation.setText(getString(R.string.dons_pour) + adminAssociation);
                            chargerEtCalculerTotalDons();
                        } else {
                            Toast.makeText(AdminActivity.this, "Accès refusé", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminActivity.this, LoginChoiceActivity.class));
                            finish();
                        }
                    });
        } else {
            Toast.makeText(this, "Utilisateur non connecté", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
        }

        btnRetour.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, LoginChoiceActivity.class));
            finish();
        });

        iconAdmin.setOnClickListener(v -> {
            startActivity(new Intent(AdminActivity.this, ProfilActivity.class));
        });
    }

    private void chargerEtCalculerTotalDons() {
        db.collection("dons").whereEqualTo("association", adminAssociation).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donList.clear();
                    int totalMontant = 0;
                    int totalDons = 0; // Compteur du nombre total de dons

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        int montant = document.getLong("montant").intValue();
                        String prenom = document.getString("prenom");
                        String date = document.getTimestamp("date").toDate().toString();
                        String category = document.getString("category"); // Récupération de la catégorie

                        donList.add(new DonItem(montant, document.getTimestamp("date").toDate(), prenom, category));
                        totalMontant += montant;
                        totalDons++; // Incrémentation du nombre total de dons
                    }

                    donAdapter.notifyDataSetChanged();
                    montantTotalDons.setText(totalMontant + " €");
                    nombreTotalDons.setText(totalDons + " dons"); // Affichage du nombre total de dons

                }).addOnFailureListener(e ->
                        Toast.makeText(AdminActivity.this, "Erreur lors du chargement des dons", Toast.LENGTH_SHORT).show()
                );
    }
}