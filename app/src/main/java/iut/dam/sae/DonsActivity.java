package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
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
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dons);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.recyclerViewAssociations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        associationList = new ArrayList<>();
        adapter = new ItemAssoAdapter(associationList, this);
        recyclerView.setAdapter(adapter);

        chargerAssociations();

        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(DonsActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void chargerAssociations() {
        db.collection("associations")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String nom = document.getString("nom");
                        String description = document.getString("description");
                        String url = document.getString("url");

                        associationList.add(new ItemAsso(nom, description, R.drawable.logo, url));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Erreur lors du chargement des associations", e);
                    Toast.makeText(DonsActivity.this, "Erreur lors du chargement des associations", Toast.LENGTH_SHORT).show();
                });
    }
}
