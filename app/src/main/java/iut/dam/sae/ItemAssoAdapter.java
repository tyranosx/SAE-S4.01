package iut.dam.sae;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAssoAdapter extends RecyclerView.Adapter<ItemAssoAdapter.ViewHolder> {

    private List<ItemAsso> associationList;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(ItemAsso item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.nom_association);
        }
    }
}
