package iut.dam.sae;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AssociationAdapter extends RecyclerView.Adapter<AssociationAdapter.ViewHolder> {

    private List<ItemAsso> associationList;
    private List<ItemAsso> associationListFull; // Liste complète pour la recherche
    private Context context;
    private OnItemClickListener onItemClickListener; // Ajout de l'interface ici

    public interface OnItemClickListener {
        void onItemClick(ItemAsso item);  // Méthode à implémenter dans l'activité
    }

    public AssociationAdapter(List<ItemAsso> associationList, Context context) {
        this.associationList = associationList;
        this.associationListFull = new ArrayList<>(associationList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_association, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAsso association = associationList.get(position);
        holder.tvNom.setText(association.getNom());
        holder.tvDescription.setText(association.getDescription());
        holder.ivLogo.setImageResource(R.drawable.logo);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(association);
            }
        });
    }

    @Override
    public int getItemCount() {
        return associationList.size();
    }

    // Méthode pour définir le gestionnaire de clics
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom, tvDescription;
        ImageView ivLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvNom);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivLogo = itemView.findViewById(R.id.ivLogo);
        }
    }

    // Ajout d'un filtre pour la recherche
    public void filtrerAssociations(String texte) {
        associationList.clear();
        if (texte.isEmpty()) {
            associationList.addAll(associationListFull);
        } else {
            String texteMinuscule = texte.toLowerCase();
            for (ItemAsso item : associationListFull) {
                if (item.getNom().toLowerCase().contains(texteMinuscule) ||
                        item.getDescription().toLowerCase().contains(texteMinuscule)) {
                    associationList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
