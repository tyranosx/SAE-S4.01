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
import java.util.List;

public class ItemAssoAdapter extends RecyclerView.Adapter<ItemAssoAdapter.ViewHolder> {

    private List<ItemAsso> associationList;
    private Context context;

    public ItemAssoAdapter(List<ItemAsso> associationList, Context context) {
        this.associationList = associationList;
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
        ItemAsso association = associationList.get(position);

        holder.tvNom.setText(association.getNom());
        holder.tvDescription.setText(association.getDescription());
        holder.ivLogo.setImageResource(R.drawable.logo);  // Logo générique France Assos Santé

        // Gestion du bouton "Donner" pour accéder à Don1Activity
        holder.btnDonner.setOnClickListener(v -> {
            Intent intent = new Intent(context, Don1Activity.class);
            intent.putExtra("nomAssociation", association.getNom());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return associationList.size();
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
            btnDonner = itemView.findViewById(R.id.btn_donner);  // Ajout du bouton "Donner"
        }
    }
}
