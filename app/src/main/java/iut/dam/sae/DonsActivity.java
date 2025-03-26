package iut.dam.sae;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.widget.SearchView;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth mAuth; // Gestion de l'état de connexion
    private ImageButton btnProfil; // Déclaration du bouton profil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        associationList = new ArrayList<>();
        filteredList = new ArrayList<>();

        String prenom = getIntent().getStringExtra("prenom");

        adapter = new ItemAssoAdapter(filteredList, this);
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
        btnProfil = findViewById(R.id.btn_profil);
        if (mAuth.getCurrentUser() == null) {
            btnProfil.setVisibility(ImageButton.GONE);  // Masquer si l'utilisateur n'est pas connecté
        } else {
            btnProfil.setOnClickListener(v -> {
                startActivity(new Intent(DonsActivity.this, ProfilActivity.class));
            });
        }

        // Bouton "On vous aide à choisir"
        Button btnAideChoix = findViewById(R.id.btn_aidechoix);
        btnAideChoix.setOnClickListener(v -> {
            startActivity(new Intent(DonsActivity.this, ConseilChoixActivity.class));
        });

        // Barre de recherche
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);  // Garde le champ ouvert par défaut
        // Modifier la couleur du texte et du hint dans le SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setHint("Rechercher une association"); // Redéfinir le hint au cas où il aurait été effacé
        searchEditText.setHintTextColor(Color.parseColor("#7A7A7A")); // Couleur plus visible
        if (searchEditText != null) {
            searchEditText.setTextColor(Color.parseColor("#007980"));
            searchEditText.setHintTextColor(Color.parseColor("#7A7A7A"));
        }

        // Modifier la couleur de l'icône de la loupe (en option)
        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        if (searchIcon != null) {
            searchIcon.setColorFilter(Color.parseColor("#007980"), PorterDuff.Mode.SRC_IN);
        }
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

        // Gestion du clic sur une association
        adapter.setOnItemClickListener(association -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();

            Intent intent;
            if (currentUser != null) {
                // Utilisateur connecté → Rediriger vers Don1Activity
                intent = new Intent(DonsActivity.this, Don1Activity.class);
            } else {
                // Utilisateur non connecté → Rediriger vers Don3Activity
                intent = new Intent(DonsActivity.this, Don3Activity.class);

                // Vérifie si le prénom est déjà défini (anonyme ou prénom saisi)
                if (prenom != null) {
                    intent.putExtra("prenom", prenom);
                } else {
                    intent.putExtra("prenom", "ANONYME");
                }
            }

            intent.putExtra("nomAssociation", association.getNom());
            startActivity(intent);
        });
    }

    // Méthode pour charger les associations depuis Firestore
    private void chargerAssociations() {
        db.collection("associations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    associationList.clear();
                    filteredList.clear();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String nom = document.getString("nom");
                        String description = document.getString("description");
                        String url = document.getString("url");
                        String category = document.getString("category");

                        ItemAsso association = new ItemAsso(nom, description, R.drawable.logo, url, category);
                        associationList.add(association);
                        filteredList.add(association);
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
        if (query == null) query = "";
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
