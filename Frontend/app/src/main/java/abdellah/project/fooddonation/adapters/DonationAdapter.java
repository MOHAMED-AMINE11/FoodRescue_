package abdellah.project.fooddonation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.Donations;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private Context context;
    private List<Donations> itemList;
    private OnItemClickListener onItemClickListener;

    // Constructor with OnItemClickListener
    public DonationAdapter(Context context, List<Donations> itemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    // Constructor without OnItemClickListener
    public DonationAdapter(Context context, List<Donations> itemList) {
        this(context, itemList, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donnation_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donations item = itemList.get(position);

        holder.title.setText(item.getTitle() != null ? item.getTitle() : "Titre non disponible");
        holder.description.setText(item.getDescription() != null ? item.getDescription() : "Description non disponible");
        holder.date.setText(item.getDate() != null ? item.getDate().toString() : "Date inconnue");
        Log.d("url",item.getFoodImagePreview());
        Glide.with(context)
                .load(item.getFoodImagePreview()).apply(new RequestOptions()
                .timeout(5000))
                .into(holder.image);

        holder.button.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onButtonClick(item);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        TextView date;
        Button button;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.nom);
            description = itemView.findViewById(R.id.itemDescription);
            date = itemView.findViewById(R.id.dateChip);
            image = itemView.findViewById(R.id.itemImage);
            button = itemView.findViewById(R.id.getButton);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Donations donation);
        void onButtonClick(Donations donation);
    }
}
