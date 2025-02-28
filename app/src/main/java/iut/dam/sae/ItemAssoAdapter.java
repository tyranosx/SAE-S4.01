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

public class ItemAssoAdapter extends RecyclerView.Adapter<ItemAssoAdapter.ItemAssoViewHolder> {

    private List<itemAsso> associationList;
    private Context context;

    public ItemAssoAdapter(List<itemAsso> associationList, Context context) {
        this.associationList = associationList;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemAssoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asso, parent, false);
        return new ItemAssoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAssoViewHolder holder, int position) {
        itemAsso association = associationList.get(position);
        holder.nomAssociation.setText(association.getNom());
        holder.descriptionAssociation.setText(association.getDescription());
        holder.imageAssociation.setImageResource(association.getImageResId());

        // Rediriger vers Don1Activity au lieu du site web
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

    public static class ItemAssoViewHolder extends RecyclerView.ViewHolder {
        TextView nomAssociation, descriptionAssociation;
        ImageView imageAssociation;
        Button btnDonner;

        public ItemAssoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomAssociation = itemView.findViewById(R.id.tv_nom_association);
            descriptionAssociation = itemView.findViewById(R.id.tv_description_association);
            imageAssociation = itemView.findViewById(R.id.img_association);
            btnDonner = itemView.findViewById(R.id.btn_donner);
        }
    }
}
