package abdellah.project.fooddonation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.BoiteMail;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.HelpViewHolder> {

    private List<BoiteMail> itemList;
    private OnHelpClickListener listener;

    public HelpAdapter(List<BoiteMail> itemList, OnHelpClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    public interface OnHelpClickListener {
        void onTakeKnowClick(BoiteMail boiteMail);
        void onCellItem(BoiteMail boiteMail);
    }

    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notife_recieve, parent, false);
        return new HelpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int position) {
        BoiteMail item = itemList.get(position);

        // Utiliser la date de la donation associée à l'élément
        Date donationDate = item.getDonation().getDate();  // Assurez-vous que BoiteMail possède cette date

        // Créer un format pour afficher la date et le jour
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");

        // Formater la date de la donation
        String formattedDate = sdf.format(donationDate);

        // Afficher la date formatée dans le TextView
        holder.dateTextView.setText(formattedDate);
        holder.descriptionTextView.setText(item.getDonation().getDescription());
        holder.nameTextView.setText(item.getDonor().getNom());

        holder.takeKnowButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTakeKnowClick(item);
            }
        });

        holder.cellButton.setOnClickListener(v -> {
            listener.onCellItem(item);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Méthode pour mettre à jour la liste d'éléments dans l'adaptateur
    public void updateList(List<BoiteMail> newBoiteMails) {
        this.itemList = newBoiteMails;
        notifyDataSetChanged(); // Rafraîchir l'adaptateur avec les nouvelles données
    }

    public static class HelpViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView descriptionTextView;
        TextView nameTextView;
        Button takeKnowButton;

       Button cellButton;

        public HelpViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            descriptionTextView = itemView.findViewById(R.id.description);
            nameTextView = itemView.findViewById(R.id.Name);
            takeKnowButton = itemView.findViewById(R.id.take_know_button);
            cellButton=itemView.findViewById(R.id.cell_button);
        }
    }
}
