package iut.dam.sae;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db; // Ajout de la base de données Firestore

    private EditText inputEmail, inputPassword, inputPrenom, inputNom, inputDateNaissance;
    private Button btnSinscrire;
    private ImageButton btnRetour;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();  // Initialisation de Firestore

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputPrenom = findViewById(R.id.input_prenom);  // Champ Prénom
        inputNom = findViewById(R.id.input_nom);        // Champ Nom
        inputDateNaissance = findViewById(R.id.input_date_naissance);

        btnSinscrire = findViewById(R.id.btn_inscription);
        btnRetour = findViewById(R.id.btn_retour);

        calendar = Calendar.getInstance();

        // Gestion du DatePicker pour la date de naissance
        inputDateNaissance.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    InscriptionActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
                        inputDateNaissance.setText(dateFormat.format(calendar.getTime()));
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });

        // Gestion de l'inscription
        btnSinscrire.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String prenom = inputPrenom.getText().toString().trim();
            String nom = inputNom.getText().toString().trim();
            String dateNaissance = inputDateNaissance.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || prenom.isEmpty() || nom.isEmpty() || dateNaissance.isEmpty()) {
                Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Enregistrer les informations supplémentaires dans Firestore
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("prenom", prenom);
                                userData.put("nom", nom);
                                userData.put("dateNaissance", dateNaissance);
                                userData.put("association", "NONE"); // Valeur par défaut
                                userData.put("isAdmin", false);     // Valeur par défaut

                                // Enregistrement dans Firestore avec l'UID de l'utilisateur comme ID du document
                                db.collection("users").document(user.getUid())
                                        .set(userData)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(InscriptionActivity.this, LoginChoiceActivity.class));
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(InscriptionActivity.this, "Erreur lors de l'ajout des données", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            Toast.makeText(InscriptionActivity.this, "Inscription échouée : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });

        // Gestion du bouton retour
        btnRetour.setOnClickListener(v -> {
            Intent intent = new Intent(InscriptionActivity.this, LoginChoiceActivity.class);
            startActivity(intent);
            finish();
        });
    }
}