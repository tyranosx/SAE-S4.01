package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        donList = new ArrayList<>();
        donAdapter = new DonAdapter(donList);
        recyclerView.setAdapter(donAdapter);

        // Vérifier si l'utilisateur est un admin
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists() && Boolean.TRUE.equals(documentSnapshot.getBoolean("isAdmin"))) {
                            adminAssociation = documentSnapshot.getString("association");
                            titreAssociation.setText("Dons pour : " + adminAssociation);
                            chargerDons(adminAssociation);
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

        // Bouton retour
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });

        // Redirection vers profil admin
        iconAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this, ProfilActivity.class);
            startActivity(intent);
        });
    }

    // Charger les dons liés à l'association de l'admin
    private void chargerDons(String association) {
        db.collection("dons").whereEqualTo("association", association).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    donList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String montant = document.getString("montant");
                        String date = document.getString("date");
                        donList.add(new DonItem(montant, date));
                    }
                    donAdapter.notifyDataSetChanged();
                });
    }
}
