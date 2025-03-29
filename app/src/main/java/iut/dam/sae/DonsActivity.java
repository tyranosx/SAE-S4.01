package iut.dam.sae;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;

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
    private FirebaseAuth mAuth;
    private ImageButton btnProfil;
    private LinearLayout loadingContainer;
    private ProgressBar progressBar;
    private TextView textLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SearchView searchView = findViewById(R.id.search_view);
        ImageView logo = findViewById(R.id.logo);
        Button btnAideChoix = findViewById(R.id.btn_aidechoix);
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnProfil = findViewById(R.id.btn_profil);

        loadingContainer = findViewById(R.id.loading_container);
        progressBar = findViewById(R.id.progress_dons);
        textLoading = findViewById(R.id.text_loading_dons);

        associationList = new ArrayList<>();
        filteredList = new ArrayList<>();

        adapter = new ItemAssoAdapter(filteredList, this);
        recyclerView.setAdapter(adapter);

        String prenom = getIntent().getStringExtra("prenom");

        // 🔥 Animations
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);

        logo.startAnimation(zoomIn);
        searchView.startAnimation(fadeIn);
        btnAideChoix.startAnimation(slideUp);
        recyclerView.startAnimation(fadeIn);

        showLoading(true);
        chargerAssociations();

        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(DonsActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.fade_out);
            finish();
        });

        if (mAuth.getCurrentUser() == null) {
            btnProfil.setVisibility(ImageButton.GONE);
        } else {
            btnProfil.setOnClickListener(v -> {
                Intent intent = new Intent(DonsActivity.this, ProfilActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            });
        }

        btnAideChoix.setOnClickListener(v -> {
            Intent intent = new Intent(DonsActivity.this, ConseilChoixActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.fade_out);
        });

        searchView.setIconifiedByDefault(false);
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        if (searchEditText != null) {
            searchEditText.setTextColor(Color.parseColor("#007980"));
            searchEditText.setHintTextColor(Color.parseColor("#7A7A7A"));
            searchEditText.setHint("Rechercher une association");
        }
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

        adapter.setOnItemClickListener(association -> {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            Intent intent;
            if (currentUser != null) {
                intent = new Intent(DonsActivity.this, Don1Activity.class);
            } else {
                intent = new Intent(DonsActivity.this, Don3Activity.class);
                intent.putExtra("prenom", (prenom != null) ? prenom : "ANONYME");
            }
            intent.putExtra("nomAssociation", association.getNom());
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });
    }

    private void chargerAssociations() {
        db.collection("associations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    associationList.clear();
                    filteredList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String nom = doc.getString("nom");
                        String description = doc.getString("description");
                        String url = doc.getString("url");
                        String category = doc.getString("category");

                        ItemAsso asso = new ItemAsso(nom, description, R.drawable.logo, url, category);
                        associationList.add(asso);
                        filteredList.add(asso);
                    }
                    adapter.notifyDataSetChanged();
                    showLoading(false);
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Erreur chargement associations", e);
                    Toast.makeText(this, "Erreur lors du chargement des associations", Toast.LENGTH_SHORT).show();
                    showLoading(false);
                });
    }

    private void showLoading(boolean isLoading) {
        loadingContainer.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
    }

    private void filtrerAssociations(String query) {
        if (query == null) query = "";
        filteredList.clear();

        for (ItemAsso asso : associationList) {
            if (asso.getNom() != null &&
                    asso.getNom().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(asso);
            }
        }

        adapter.notifyDataSetChanged();
        recyclerView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
    }
}