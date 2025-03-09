package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ConseilChoixActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssociationAdapter adapter;
    private List<ItemAsso> associationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseil_choix);

        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation des associations
        initialiserAssociations();

        // Initialisation de l'adapter
        adapter = new AssociationAdapter(associationList, this);

        // Gestion du clic pour ouvrir Don1Activity avec le nom de l'association
        adapter.setOnItemClickListener(item -> {
            Intent intent = new Intent(ConseilChoixActivity.this, Don1Activity.class);
            intent.putExtra("nomAssociation", item.getNom());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        // Gestion du bouton retour
        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());

        // Gestion du bouton Profil
        ImageButton btnProfil = findViewById(R.id.btn_profil);
        btnProfil.setOnClickListener(v -> {
            Intent intent = new Intent(ConseilChoixActivity.this, ProfilActivity.class);
            startActivity(intent);
        });

        // Barre de recherche
        SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filtrerAssociations(newText);
                return true;
            }
        });
    }

    private void initialiserAssociations() {
        associationList = new ArrayList<>();
        associationList.add(new ItemAsso("AAAVAM", "Défense des victimes d’accidents médicamenteux", R.drawable.logo, "https://www.aaavam.eu/"));
        associationList.add(new ItemAsso("Actions Traitements", "Aide aux usagers du système de santé", R.drawable.logo, "https://www.actions-traitements.org/"));
        associationList.add(new ItemAsso("AIDES", "Lutte contre le SIDA", R.drawable.logo, "https://www.aides.org/"));
        associationList.add(new ItemAsso("France Alzheimer", "Aide aux malades d'Alzheimer", R.drawable.logo, "https://www.francealzheimer.org/"));
    }
}
