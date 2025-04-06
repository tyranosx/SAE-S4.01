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

public class CategorizedAssociationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CATEGORY = 0;
    private static final int TYPE_ASSOCIATION = 1;

    private List<Object> items; // Can be String (category) or ItemAsso
    private Context context;
    private Map<String, List<ItemAsso>> fullCategorizedData; // Pour reset les filtres

    private String prenom;

    public CategorizedAssociationAdapter(Map<String, List<ItemAsso>> categorizedData, Context context, String prenom) {
        this.context = context;
        this.fullCategorizedData = categorizedData;
        this.prenom = prenom;
        buildItemList(categorizedData);
    }

    private void buildItemList(Map<String, List<ItemAsso>> categorizedData) {
        items = new ArrayList<>();
        for (Map.Entry<String, List<ItemAsso>> entry : categorizedData.entrySet()) {
            items.add(entry.getKey());
            items.addAll(entry.getValue());
        }
        notifyDataSetChanged();
    }

    public void setData(Map<String, List<ItemAsso>> newCategorizedData) {
        this.fullCategorizedData = newCategorizedData;
        buildItemList(newCategorizedData);
    }

    public void filtrer(String query) {
        if (query.isEmpty()) {
            buildItemList(fullCategorizedData);
            return;
        }

        Map<String, List<ItemAsso>> filtered = new HashMap<>();
        for (Map.Entry<String, List<ItemAsso>> entry : fullCategorizedData.entrySet()) {
            List<ItemAsso> matched = new ArrayList<>();
            for (ItemAsso asso : entry.getValue()) {
                if (asso.getNom().toLowerCase().contains(query) ||
                        (asso.getDescription() != null && asso.getDescription().toLowerCase().contains(query))) {
                    matched.add(asso);
                }
            }
            if (!matched.isEmpty()) {
                filtered.put(entry.getKey(), matched);
            }
        }
        buildItemList(filtered);
    }

    @Override
    public int getItemViewType(int position) {
        return (items.get(position) instanceof String) ? TYPE_CATEGORY : TYPE_ASSOCIATION;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CATEGORY) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_category_header, parent, false);
            return new CategoryViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_asso, parent, false);
            return new AssociationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CATEGORY) {
            String category = (String) items.get(position);
            ((CategoryViewHolder) holder).categorie.setText(category);
        } else {
            ItemAsso item = (ItemAsso) items.get(position);
            AssociationViewHolder assocHolder = (AssociationViewHolder) holder;
            assocHolder.nom.setText(item.getNom());
            assocHolder.description.setText(item.getDescription());
            assocHolder.logo.setImageResource(item.getImageResId());
            assocHolder.btnDonner.setOnClickListener(v -> {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent;
                if (currentUser != null) {
                    intent = new Intent(context, Don1Activity.class);
                } else {
                    intent = new Intent(context, Don3Activity.class);
                    intent.putExtra("prenom", prenom != null && !prenom.isEmpty() ? prenom : "ANONYME");
                }
                intent.putExtra("nomAssociation", item.getNom());
                context.startActivity(intent);
            });

            // ✅ Animation de fade-in avec effet cascade
            holder.itemView.setAlpha(0f);
            holder.itemView.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .setStartDelay(position * 30) // ajustable si tu veux plus rapide ou plus lent
                    .start();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class AssociationViewHolder extends RecyclerView.ViewHolder {
        TextView nom, description;
        ImageView logo;
        Button btnDonner;

        public AssociationViewHolder(@NonNull View itemView) {
            super(itemView);
            nom = itemView.findViewById(R.id.nom_association);
            description = itemView.findViewById(R.id.tv_description_association);
            logo = itemView.findViewById(R.id.img_association);
            btnDonner = itemView.findViewById(R.id.btn_donner);
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categorie;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categorie = itemView.findViewById(R.id.tv_categorie);
        }
    }
}