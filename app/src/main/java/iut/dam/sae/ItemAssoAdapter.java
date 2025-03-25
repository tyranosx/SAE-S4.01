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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemAssoAdapter extends RecyclerView.Adapter<ItemAssoAdapter.ViewHolder> {

    private static final int TYPE_CATEGORIE = 0;
    private static final int TYPE_ASSOCIATION = 1;

    private List<Object> itemsMixes;
    private List<Object> itemsMixesFiltres;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public ItemAssoAdapter(List<ItemAsso> associationList, Context context) {
        this.context = context;
        preparerItemsMixes(associationList);
    }

    private void preparerItemsMixes(List<ItemAsso> associations) {
        // Regrouper les associations par catégorie
        Map<String, List<ItemAsso>> associationsParCategorie = new HashMap<>();
        for (ItemAsso asso : associations) {
            associationsParCategorie
                    .computeIfAbsent(asso.getCategorie(), k -> new ArrayList<>())
                    .add(asso);
        }

        // Créer une liste mixte avec des catégories et des associations
        itemsMixes = new ArrayList<>();
        for (Map.Entry<String, List<ItemAsso>> entry : associationsParCategorie.entrySet()) {
            itemsMixes.add(entry.getKey()); // Ajouter le titre de la catégorie
            itemsMixes.addAll(entry.getValue()); // Ajouter les associations de la catégorie
        }

        itemsMixesFiltres = new ArrayList<>(itemsMixes);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = itemsMixesFiltres.get(position);
        return item instanceof String ? TYPE_CATEGORIE : TYPE_ASSOCIATION;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = itemsMixesFiltres.get(position);

        if (item instanceof String) {
            // Titre de catégorie
            holder.tvNom.setText((String) item);
            holder.tvDescription.setVisibility(View.GONE);
            holder.btnDonner.setVisibility(View.GONE);
            holder.ivLogo.setVisibility(View.GONE);
        } else if (item instanceof ItemAsso) {
            // Association
            ItemAsso association = (ItemAsso) item;

            holder.tvNom.setText(association.getNom());
            holder.tvDescription.setText(association.getDescription());
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.ivLogo.setVisibility(View.VISIBLE);
            holder.btnDonner.setVisibility(View.VISIBLE);

            holder.ivLogo.setImageResource(R.drawable.logo);  // Logo générique France Assos Santé

            // Gestion du bouton "Donner"
            holder.btnDonner.setOnClickListener(v -> {
                Intent intent;
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null) {
                    // Utilisateur connecté → Va vers Don1Activity
                    intent = new Intent(context, Don1Activity.class);
                } else {
                    // Utilisateur non connecté → Va vers Don3Activity
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
    }

    @Override
    public int getItemCount() {
        return itemsMixesFiltres.size();
    }

    // Méthode de filtrage
    public void filtrerAssociations(String query) {
        itemsMixesFiltres.clear();

        // Filtrer les associations
        List<Object> associationsFiltrees = itemsMixes.stream()
                .filter(item -> {
                    if (item instanceof ItemAsso) {
                        ItemAsso asso = (ItemAsso) item;
                        return asso.getNom().toLowerCase().contains(query.toLowerCase()) ||
                                asso.getDescription().toLowerCase().contains(query.toLowerCase());
                    }
                    return true; // Garder les titres de catégories
                })
                .collect(Collectors.toList());

        itemsMixesFiltres.addAll(associationsFiltrees);
        notifyDataSetChanged();
    }

    public void setFilteredList(List<ItemAsso> filteredList) {
        preparerItemsMixes(filteredList);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(ItemAsso association);
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