package iut.dam.sae;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.auth.FirebaseAuth;

public class ConseilChoixActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AssociationAdapter adapter;
    private List<ItemAsso> associationList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conseil_choix);

        mAuth = FirebaseAuth.getInstance();

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
        if (mAuth.getCurrentUser() == null) {
            btnProfil.setVisibility(ImageButton.GONE);  // Masquer si l'utilisateur n'est pas connecté
        } else {
            btnProfil.setOnClickListener(v -> {
                startActivity(new Intent(ConseilChoixActivity.this, ProfilActivity.class));
            });
        }

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
