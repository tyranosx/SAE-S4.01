package iut.dam.sae;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DonAdapter extends RecyclerView.Adapter<DonAdapter.DonViewHolder> {

    private List<DonItem> donList;

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
        DonItem don = donList.get(position);
        holder.montantText.setText("Montant : " + don.getMontant() + " €");
        holder.dateText.setText("Date : " + don.getDate());
    }

    @Override
    public int getItemCount() {
        return donList.size();
    }

    public static class DonViewHolder extends RecyclerView.ViewHolder {
        TextView montantText, dateText;

        public DonViewHolder(@NonNull View itemView) {
            super(itemView);
            montantText = itemView.findViewById(R.id.montant_don);
            dateText = itemView.findViewById(R.id.date_don);
        }
    }
}
