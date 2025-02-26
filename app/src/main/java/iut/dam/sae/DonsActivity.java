package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
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
    private List<itemAsso> associationList;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialisation Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Vérifier si l'utilisateur est connecté
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // L'utilisateur n'est pas connecté, redirection vers la connexion
            Toast.makeText(this, "Vous devez être connecté pour accéder aux dons.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DonsActivity.this, ConnexionActivity.class));
            finish();
            return; // Arrête l'exécution de onCreate ici
        }

        setContentView(R.layout.activity_dons);

        // Initialisation RecyclerView
        recyclerView = findViewById(R.id.recyclerViewAssociations);
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
        recyclerView.setAdapter(adapter);
    }
}
