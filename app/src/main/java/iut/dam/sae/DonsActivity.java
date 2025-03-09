package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class DonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAssoAdapter adapter;
    private List<ItemAsso> associationList;
    private List<ItemAsso> filteredList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        associationList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new ItemAssoAdapter(filteredList, this);  // Utiliser la filteredList pour afficher les résultats filtrés
        recyclerView.setAdapter(adapter);

        chargerAssociations();

        // Bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(DonsActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });

        // Bouton Profil
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        btnProfil.setOnClickListener(v -> {
            startActivity(new Intent(DonsActivity.this, ProfilActivity.class));
        });

        // Bouton "On vous aide à choisir"
        Button btnAideChoix = findViewById(R.id.btn_aidechoix);
        btnAideChoix.setOnClickListener(v -> {
            startActivity(new Intent(DonsActivity.this, ConseilChoixActivity.class));
        });

        // Barre de recherche
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filtrerAssociations(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filtrerAssociations(newText);
                return true;
            }
        });
    }

    // Méthode pour charger les associations depuis Firestore
    private void chargerAssociations() {
        db.collection("associations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    associationList.clear();
                    filteredList.clear();  // Assurez-vous de vider la liste filtrée pour éviter les doublons
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String nom = document.getString("nom");
                        String description = document.getString("description");
                        String url = document.getString("url");

                        ItemAsso association = new ItemAsso(nom, description, R.drawable.logo, url);
                        associationList.add(association);
                        filteredList.add(association); // Ajouter à la liste filtrée aussi
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Erreur lors du chargement des associations", e);
                    Toast.makeText(DonsActivity.this, "Erreur lors du chargement des associations", Toast.LENGTH_SHORT).show();
                });
    }

    // Méthode pour filtrer les associations
    private void filtrerAssociations(String query) {
        if (query == null) query = ""; // Vérification pour éviter les valeurs nulles
        filteredList.clear();

        for (ItemAsso association : associationList) {
            if (association.getNom() != null &&
                    association.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(association);
            }
        }

        adapter.notifyDataSetChanged();
    }

}
