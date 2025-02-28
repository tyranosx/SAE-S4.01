package iut.dam.sae;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAssoAdapter extends ArrayAdapter<itemAsso> {

    private List<itemAsso> associationList;
    private Context context;

    public ItemAssoAdapter(List<itemAsso> associationList, Context context) {
        super(context, 0,associationList);
        this.associationList = associationList;
        this.context = context;
    }

    @NonNull

    public itemAssoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_asso, parent, false);
        return new itemAssoViewHolder(view);
    }


    public void onBindViewHolder(@NonNull itemAssoViewHolder holder, int position) {
        itemAsso association = associationList.get(position);
        holder.nomAssociation.setText(association.getNom());
        holder.descriptionAssociation.setText(association.getDescription());
        holder.imageAssociation.setImageResource(association.getImageResId());

        holder.btnDonner.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(association.getUrl()));
            context.startActivity(intent);
        });
    }


    public int getItemCount() {
        return associationList.size();
    }

    public static class itemAssoViewHolder extends RecyclerView.ViewHolder {
        TextView nomAssociation, descriptionAssociation;
        ImageView imageAssociation;
        Button btnDonner;

        public itemAssoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomAssociation = itemView.findViewById(R.id.tv_nom_association);
            descriptionAssociation = itemView.findViewById(R.id.tv_description_association);
            imageAssociation = itemView.findViewById(R.id.img_association);
            btnDonner = itemView.findViewById(R.id.btn_donner);
        }
    }
}
