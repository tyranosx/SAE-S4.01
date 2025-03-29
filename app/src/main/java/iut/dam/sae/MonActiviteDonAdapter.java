package iut.dam.sae;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MonActiviteDonAdapter extends RecyclerView.Adapter<MonActiviteDonAdapter.DonViewHolder> {

    private List<MonActiviteDonItem> donList;

    public MonActiviteDonAdapter(List<MonActiviteDonItem> donList) {
        this.donList = donList;
    }

    @NonNull
    @Override
    public DonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_activite, parent, false);
        return new DonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonViewHolder holder, int position) {
        MonActiviteDonItem don = donList.get(position);

        holder.txtAssociation.setText(don.getAssociation());
        holder.txtMontant.setText(don.getMontant() + "€");

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRANCE);
        holder.txtDate.setText(dateFormat.format(don.getDate()));
    }

    @Override
    public int getItemCount() {
        return donList.size();
    }

    public static class DonViewHolder extends RecyclerView.ViewHolder {
        TextView txtAssociation, txtMontant, txtDate;

        public DonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAssociation = itemView.findViewById(R.id.txt_association);
            txtMontant = itemView.findViewById(R.id.txt_montant);
            txtDate = itemView.findViewById(R.id.txt_date);
        }
    }
}
