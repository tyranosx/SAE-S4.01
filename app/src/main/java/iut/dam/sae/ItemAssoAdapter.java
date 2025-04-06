package iut.dam.sae;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ItemAssoAdapter extends RecyclerView.Adapter<ItemAssoAdapter.ViewHolder> {

    private List<ItemAsso> associationList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ItemAssoAdapter(List<ItemAsso> associationList, Context context) {
        this.associationList = associationList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemAsso association);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAsso association = associationList.get(position);

        holder.tvNom.setText(association.getNom());
        holder.tvDescription.setText(association.getDescription());
        holder.ivLogo.setImageResource(R.drawable.logo);

        // Gestion du bouton "Donner"
        holder.btnDonner.setOnClickListener(v -> {
            Intent intent;
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

            if (currentUser != null) {
                // Utilisateur connecté -> Va vers Don1Activity
                intent = new Intent(context, Don1Activity.class);
            } else {
                // Utilisateur non connecté -> Va vers Don3Activity
                intent = new Intent(context, Don3Activity.class);

                // Vérification si le prénom provient de DemandePrenomActivity
                String prenom = ((DonsActivity) context).getIntent().getStringExtra("prenom");
                if (prenom != null) {
                    intent.putExtra("prenom", prenom); // Utiliser le prénom transmis
                } else {
                    intent.putExtra("prenom", "ANONYME"); // Valeur par défaut si aucun prénom n'est fourni
                }
            }

            intent.putExtra("nomAssociation", association.getNom()); // Envoie le nom de l'association
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return associationList.size();
    }

    public void setFilteredList(List<ItemAsso> filteredList) {
        this.associationList = filteredList;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvDescription;
        ImageView ivLogo;
        Button btnDonner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.nom_association);
            tvDescription = itemView.findViewById(R.id.tv_description_association);
            ivLogo = itemView.findViewById(R.id.img_association);
            btnDonner = itemView.findViewById(R.id.btn_donner);
        }
    }
}
