package iut.dam.sae;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAssoAdapter extends RecyclerView.Adapter<ItemAssoAdapter.ViewHolder> implements Filterable {

    private List<ItemAsso> associationList;
    private List<ItemAsso> associationListFull; // Liste complète pour la recherche
    private Context context;

    public ItemAssoAdapter(List<ItemAsso> associationList, Context context) {
        if (associationList == null) {
            associationList = new ArrayList<>();
        }
        this.associationList = associationList;
        this.associationListFull = new ArrayList<>(associationList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asso, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemAsso item = associationList.get(position);
        holder.nomAssociation.setText(item.getNom());
        holder.descriptionAssociation.setText(item.getDescription());
        holder.logoAssociation.setImageResource(item.getImageResId());

        // Écouteur d'événement sur le bouton "Donner"
        holder.btnDonner.setOnClickListener(v -> {
            Log.d("ItemAssoAdapter", "Bouton Donner cliqué pour : " + item.getNom());

            // Rediriger vers l'activité Dons1Activity
            Intent intent = new Intent(context, Don1Activity.class);
            intent.putExtra("association_nom", item.getNom()); // Optionnel : passer le nom de l'asso
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return associationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomAssociation, descriptionAssociation;
        ImageView logoAssociation;
        Button btnDonner; // Ajout du bouton "Donner"

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nomAssociation = itemView.findViewById(R.id.nom_association);
            descriptionAssociation = itemView.findViewById(R.id.tv_description_association);
            logoAssociation = itemView.findViewById(R.id.img_association);
            btnDonner = itemView.findViewById(R.id.btn_donner); // Initialisation du bouton "Donner"
        }
    }

    @Override
    public Filter getFilter() {
        return associationFilter;
    }

    private Filter associationFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ItemAsso> filteredList = new ArrayList<>();
            if (associationListFull == null) {
                associationListFull = new ArrayList<>();
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemAsso item : associationListFull) {
                    if (item.getNom().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                associationList = new ArrayList<>((List<ItemAsso>) results.values);
                notifyDataSetChanged();
            }
        }
    };
}
