package abdellah.project.fooddonation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.imageview.ShapeableImageView;
import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.BoiteMail;
import java.util.List;

public class AdapterBoiteMail extends RecyclerView.Adapter<AdapterBoiteMail.BoiteMailViewHolder> {

    private List<BoiteMail> boiteMailList;
    private OnItemClickListener onItemClickListener;
   private Context context;
    public void setData(List<BoiteMail> boiteMails) {
        this.boiteMailList=boiteMails;
        notifyDataSetChanged();
    }

    // Interface pour gérer les clics sur les éléments de la liste
    public interface OnItemClickListener {
        void onConfirmClick(BoiteMail boiteMail);
        void onCancelClick(BoiteMail boiteMail);
    }

    // Constructeur
    public AdapterBoiteMail(List<BoiteMail> boiteMailList, OnItemClickListener onItemClickListener,Context context) {
        this.boiteMailList = boiteMailList;
        this.onItemClickListener = onItemClickListener;
        this.context=context;
    }

    @NonNull
    @Override
    public BoiteMailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate le layout de chaque élément de la liste
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_demand, parent, false);
        return new BoiteMailViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoiteMailViewHolder holder, int position) {
        BoiteMail boiteMail = boiteMailList.get(position);

        // Bind les données de la boîte mail dans la vue
        holder.beneficiaryName.setText(boiteMail.getBeneficiary().getNom());
        holder.donationDescription.setText(boiteMail.getDonation().getDescription());
        holder.date.setText("Date: " + boiteMail.getDonation().getDate());

        // Écouteurs de clic pour les boutons
        holder.confirmButton.setOnClickListener(v -> onItemClickListener.onConfirmClick(boiteMail));
        holder.cancelButton.setOnClickListener(v -> onItemClickListener.onCancelClick(boiteMail));
    }

    @Override
    public int getItemCount() {
        return boiteMailList.size();
    }

    // ViewHolder pour chaque élément de la liste
    public static class BoiteMailViewHolder extends RecyclerView.ViewHolder {

        TextView beneficiaryName;

        TextView donationDescription;
        TextView date;
        TextView message;
        Button confirmButton;
        Button cancelButton;
        ShapeableImageView imageView;

        public BoiteMailViewHolder(View itemView) {
            super(itemView);

            // Initialisation des vues
            beneficiaryName = itemView.findViewById(R.id.users);
            donationDescription = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.donation_date);
            confirmButton = itemView.findViewById(R.id.confirm_button);
            cancelButton = itemView.findViewById(R.id.cancel_button);
            imageView = itemView.findViewById(R.id.circleImage);
        }
    }
}
