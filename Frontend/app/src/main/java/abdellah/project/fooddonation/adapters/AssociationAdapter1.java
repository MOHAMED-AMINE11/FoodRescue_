package abdellah.project.fooddonation.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.Association;

public class AssociationAdapter1 extends RecyclerView.Adapter<AssociationAdapter1.AssociationViewHolder> {

    private List<Association> associations;
    private OnAssociationClickListener listener;

    public interface OnAssociationClickListener {
        void onAssociationClick(Association association);
    }

    public AssociationAdapter1(OnAssociationClickListener listener) {
        this.associations = new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AssociationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_association, parent, false);
        return new AssociationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssociationViewHolder holder, int position) {
        Association association = associations.get(position);
        holder.bind(association);
    }

    @Override
    public int getItemCount() {
        return associations.size();
    }

    public void setAssociations(List<Association> associations) {
        this.associations = associations;
        notifyDataSetChanged();
    }

    class AssociationViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNom;
        private TextView tvDescription;
        private TextView tvContact;

        public AssociationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNom = itemView.findViewById(R.id.tvNomAssociation);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvContact = itemView.findViewById(R.id.tvContact);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onAssociationClick(associations.get(position));
                }
            });
        }

        public void bind(Association association) {
            tvNom.setText(association.getNom());
            tvDescription.setText(association.getDescription());
            tvContact.setText(String.format("%s - %s",
                    association.getTelephone(), association.getMail()));
        }
    }


}



