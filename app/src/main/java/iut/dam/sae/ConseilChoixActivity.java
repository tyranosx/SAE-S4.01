package iut.dam.sae;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConseilChoixActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategorizedAssociationAdapter adapter;
    private Map<String, List<ItemAsso>> categorizedData;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private List<ItemAsso> allAssociations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseil_choix);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        allAssociations = new ArrayList<>();
        categorizedData = new HashMap<>();
        adapter = new CategorizedAssociationAdapter(categorizedData, this);
        recyclerView.setAdapter(adapter);

        chargerAssociationsDepuisFirestore();

        // Bouton retour
        findViewById(R.id.btn_retour).setOnClickListener(v -> finish());

        // Bouton profil
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        if (mAuth.getCurrentUser() == null) {
            btnProfil.setVisibility(ImageButton.GONE);
        } else {
            btnProfil.setOnClickListener(v -> startActivity(new Intent(this, ProfilActivity.class)));
        }

        // SearchView
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconified(false);
        searchView.clearFocus(); // Pour éviter d’ouvrir le clavier au démarrage
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setFocusable(true);
        searchEditText.setFocusableInTouchMode(true);
        searchEditText.clearFocus();
        searchEditText.setTextColor(Color.parseColor("#007980"));
        searchEditText.setHintTextColor(Color.parseColor("#7A7A7A"));

        ImageView searchIcon = searchView.findViewById(androidx.appcompat.R.id.search_mag_icon);
        if (searchIcon != null) {
            searchIcon.setColorFilter(Color.parseColor("#007980"), PorterDuff.Mode.SRC_IN);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override public boolean onQueryTextChange(String newText) {
                adapter.filtrer(newText.trim().toLowerCase());
                return true;
            }
        });
    }

    private void chargerAssociationsDepuisFirestore() {
        db.collection("associations").get().addOnSuccessListener(result -> {
            allAssociations.clear();
            categorizedData.clear();

            for (QueryDocumentSnapshot doc : result) {
                String nom = doc.getString("nom");
                String description = doc.getString("description");
                String url = doc.getString("url");
                String category = doc.getString("category");

                if (nom != null && category != null) {
                    ItemAsso item = new ItemAsso(nom, description, R.drawable.logo, url, category);
                    allAssociations.add(item);

                    if (!categorizedData.containsKey(category)) {
                        categorizedData.put(category, new ArrayList<>());
                    }
                    categorizedData.get(category).add(item);
                }
            }

            adapter.setData(categorizedData);
        }).addOnFailureListener(e -> Toast.makeText(this, "Erreur de chargement des associations", Toast.LENGTH_SHORT).show());
    }
}