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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InscriptionActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText inputEmail, inputPassword, inputDateNaissance;
    private Button btnSinscrire;
    private ImageButton btnRetour;

    private Calendar calendar;  // Pour gérer la date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        mAuth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.input_email);
        inputPassword = findViewById(R.id.input_password);
        inputDateNaissance = findViewById(R.id.input_date_naissance);  // Champ date de naissance
        btnSinscrire = findViewById(R.id.btn_inscription);
        btnRetour = findViewById(R.id.btn_retour);

        calendar = Calendar.getInstance();  // Initialisation du calendrier

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

            // Limite la date max à aujourd'hui (pas de date dans le futur)
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            // Affiche le calendrier
            datePickerDialog.show();
        });

        // Gestion de l'inscription
        btnSinscrire.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String dateNaissance = inputDateNaissance.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || dateNaissance.isEmpty()) {
                Toast.makeText(InscriptionActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Création de l'utilisateur avec Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(InscriptionActivity.this, "Inscription réussie !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(InscriptionActivity.this, LoginChoiceActivity.class));
                            finish();
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
