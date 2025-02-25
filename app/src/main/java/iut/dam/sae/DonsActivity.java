package iut.dam.sae;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;


public class DonsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ItemAssoAdapter adapter;
    private List<itemAsso> associationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons);

        // Initialiser RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAssociations);  // C'est ici que ça manquait !

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de la liste des associations
        associationList = new ArrayList<>();
        associationList.add(new itemAsso("AAAVAM", "Défense des victimes d’accidents médicamenteux", R.drawable.logo, "https://www.aaavam.eu/"));
        associationList.add(new itemAsso("Actions Traitements", "Aide aux usagers du système de santé", R.drawable.logo, "https://www.actions-traitements.org/"));
        associationList.add(new itemAsso("Addictions Alcool Vie Libre", "Aide aux personnes alcooliques", R.drawable.logo, "https://www.vielibre.org/"));
        associationList.add(new itemAsso("ADEPA", "Défense des personnes amputées", R.drawable.logo, "https://www.adepa.fr/"));
        associationList.add(new itemAsso("ADMD", "Droit de mourir dans la dignité", R.drawable.logo, "https://www.admd.net/"));
        associationList.add(new itemAsso("AIDES", "Lutte contre le SIDA", R.drawable.logo, "https://www.aides.org/"));

        // Associer l'Adapter au RecyclerView
        adapter = new ItemAssoAdapter(associationList, this);
        recyclerView.setAdapter(adapter);  // Cette ligne était également manquante !
    }
}

