package iut.dam.sae;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class DonAdapter extends RecyclerView.Adapter<DonAdapter.DonViewHolder> {

    private final List<DonItem> donList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    public DonAdapter(List<DonItem> donList) {
        this.donList = donList;
    }

    @NonNull
    @Override
    public DonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don, parent, false);
        return new DonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonViewHolder holder, int position) {
        DonItem donItem = donList.get(position);

        holder.prenomDon.setText("Prénom : " + donItem.getPrenom());
        holder.montantDon.setText("Montant : " + donItem.getMontant() + "€");
        holder.dateDon.setText("Date : " + dateFormat.format(donItem.getDate()));
        holder.categorieDon.setText("Catégorie : " + donItem.getCategory()); // Affichage de la catégorie
    }

    @Override
    public int getItemCount() {
        return donList.size();
    }

    public static class DonViewHolder extends RecyclerView.ViewHolder {
        TextView prenomDon, montantDon, dateDon, categorieDon;

        public DonViewHolder(@NonNull View itemView) {
            super(itemView);
            prenomDon = itemView.findViewById(R.id.prenom_don);
            montantDon = itemView.findViewById(R.id.montant_don);
            dateDon = itemView.findViewById(R.id.date_don);
            categorieDon = itemView.findViewById(R.id.categorie_don);
        }
    }
}