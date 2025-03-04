package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class DonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAssoAdapter adapter;
    private List<ItemAsso> associationList;
    private FirebaseAuth mAuth;
    private ImageButton btnRetour;
    private SearchView searchView; // Ajout de la barre de recherche

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons);

        ImageButton btnProfil = findViewById(R.id.btn_profil);
        searchView = findViewById(R.id.search_view); // Initialisation du SearchView

        // Initialiser Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // Vérifier si l'utilisateur est connecté
        if (user == null) {
            Toast.makeText(this, "Vous devez être connecté pour faire un don", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ConnexionActivity.class));
            finish();
            return;
        }

        // Initialisation du RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la liste des associations
        associationList = new ArrayList<>();
        associationList.add(new ItemAsso("AAAVAM", "Défense des victimes d’accidents médicamenteux", R.drawable.logo, "https://www.aaavam.eu/"));
        associationList.add(new ItemAsso("Actions Traitements", "Aide aux usagers du système de santé", R.drawable.logo, "https://www.actions-traitements.org/"));
        associationList.add(new ItemAsso("Addictions Alcool Vie Libre", "Aide aux personnes alcooliques", R.drawable.logo, "https://www.vielibre.org/"));
        associationList.add(new ItemAsso("ADEPA", "Défense des personnes amputées", R.drawable.logo, "https://www.adepa.fr/"));
        associationList.add(new ItemAsso("ADMD", "Droit de mourir dans la dignité", R.drawable.logo, "https://www.admd.net/"));
        associationList.add(new ItemAsso("AIDES", "Lutte contre le SIDA", R.drawable.logo, "https://www.aides.org/"));

        // Associer l'Adapter au RecyclerView
        adapter = new ItemAssoAdapter(associationList, this);
        recyclerView.setAdapter(adapter);
        // Gestion du bouton retour
        btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(DonsActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });

        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(DonsActivity.this, ProfilActivity.class);
            startActivity(intent);
        });

        // Ajout de la gestion du SearchView pour filtrer les associations
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

    }
}
