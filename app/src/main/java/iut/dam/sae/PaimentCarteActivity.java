package iut.dam.sae;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class PaimentCarteActivity extends AppCompatActivity {

    private EditText etNumeroCarte, etDateExpiration, etCVV, etTitulaireCarte;
    private Button btnPayerCarte;
    private FirebaseFirestore db;
    private String prenomUtilisateur = "ANONYME"; // Valeur par défaut

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paiment_carte);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        final String montant = intent.getStringExtra("montant");
        final String association = intent.getStringExtra("nomAssociation");
        final String prenom = intent.getStringExtra("prenom"); // Récupération du prénom

        if (prenom != null && !prenom.equals("ANONYME")) {
            prenomUtilisateur = prenom; // Utilise le prénom transmis uniquement s'il est valide
        } else {
            prenomUtilisateur = "ANONYME"; // Par défaut à "ANONYME"
        }

        etNumeroCarte = findViewById(R.id.et_card_number);
        etDateExpiration = findViewById(R.id.et_expiry_date);
        etCVV = findViewById(R.id.et_cvv);
        etTitulaireCarte = findViewById(R.id.et_card_holder);
        btnPayerCarte = findViewById(R.id.btn_payer);

        ImageButton btnRetour = findViewById(R.id.btn_retour);
        btnRetour.setOnClickListener(v -> finish());

        // Récupérer le prénom de l'utilisateur connecté
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            prenomUtilisateur = documentSnapshot.getString("prenom");
                        }
                    })
                    .addOnFailureListener(e -> Log.e("FirestoreError", "Erreur lors de la récupération du prénom : " + e.getMessage()));
        }

        btnPayerCarte.setOnClickListener(v -> {
            // Validation des informations de paiement
            if (validatePaymentInformation()) {
                ajouterDocumentDon(Double.parseDouble(montant), association, prenomUtilisateur);

                Toast.makeText(this, "Paiement de " + montant + "€ effectué avec succès !", Toast.LENGTH_LONG).show();

                Intent retourIntent = new Intent(this, DonsActivity.class);
                retourIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(retourIntent);
                finish();
            }
        });
    }

    private boolean validatePaymentInformation() {
        boolean isValid = true;

        // Vérification du numéro de carte
        String numeroCarte = etNumeroCarte.getText().toString().trim();
        if (TextUtils.isEmpty(numeroCarte)) {
            etNumeroCarte.setError("Veuillez saisir le numéro de carte");
            isValid = false;
        }

        // Vérification de la date d'expiration
        String dateExpiration = etDateExpiration.getText().toString().trim();
        if (TextUtils.isEmpty(dateExpiration)) {
            etDateExpiration.setError("Veuillez saisir la date d'expiration");
            isValid = false;
        } else {
            // Validation du format MM/AA
            if (!isValidExpiryDate(dateExpiration)) {
                etDateExpiration.setError("Format de date invalide (MM/AA)");
                isValid = false;
            }
        }

        // Vérification du CVV
        String cvv = etCVV.getText().toString().trim();
        if (TextUtils.isEmpty(cvv)) {
            etCVV.setError("Veuillez saisir le CVV");
            isValid = false;
        } else if (cvv.length() < 3 || cvv.length() > 4) {
            etCVV.setError("CVV invalide (3-4 chiffres)");
            isValid = false;
        }

        // Vérification du titulaire de la carte
        if (TextUtils.isEmpty(etTitulaireCarte.getText().toString().trim())) {
            etTitulaireCarte.setError("Veuillez saisir le nom du titulaire");
            isValid = false;
        }

        // Afficher un message Toast global si des champs sont manquants
        if (!isValid) {
            Toast.makeText(this, "Veuillez vérifier vos informations de paiement", Toast.LENGTH_LONG).show();
        }

        return isValid;
    }

    private boolean isValidExpiryDate(String dateExpiration) {
        // Vérifier le format de la date
        if (!Pattern.matches("^(0[1-9]|1[0-2])/([0-9]{2})$", dateExpiration)) {
            return false;
        }

        try {
            // Formater la date pour la validation
            SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.FRANCE);
            sdf.setLenient(false);
            Date expiryDate = sdf.parse(dateExpiration);

            // Vérifier que la date n'est pas expirée
            Calendar cal = Calendar.getInstance();
            cal.setTime(expiryDate);
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

            return !cal.before(Calendar.getInstance());
        } catch (ParseException e) {
            return false;
        }
    }

    private void ajouterDocumentDon(double montant, String association, String prenom) {
        Map<String, Object> donData = new HashMap<>();
        donData.put("association", association);
        donData.put("date", new Timestamp(new Date()));
        donData.put("montant", montant);
        donData.put("prenom", prenom);

        db.collection("dons").add(donData)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firestore", "Don ajouté avec succès avec ID: " + documentReference.getId());
                    Toast.makeText(this, "Don ajouté avec succès !", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur lors de l'ajout du don: " + e.getMessage());
                    Toast.makeText(this, "Erreur lors de l'ajout du don", Toast.LENGTH_SHORT).show();
                });
    }
}