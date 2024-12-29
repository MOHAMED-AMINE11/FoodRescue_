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

import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.Donations;

public class MydonationAdapter extends RecyclerView.Adapter<MydonationAdapter.ViewHolder> {

    private Context context;
    private List<Donations> itemList;
    private OnItemClickListener onItemClickListener;

    // Constructor with OnItemClickListener
    public MydonationAdapter(Context context, List<Donations> itemList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.itemList = itemList;
        this.onItemClickListener = onItemClickListener;
    }

    // Constructor without OnItemClickListener
    public MydonationAdapter(Context context, List<Donations> itemList) {
        this(context, itemList, null);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_donation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Donations item = itemList.get(position);

        holder.title.setText(item.getTitle() != null ? item.getTitle() : "Titre non disponible");

        holder.date.setText(item.getDate() != null ? item.getDate().toString() : "Date inconnue");
        Log.d("url",item.getFoodImagePreview());
      Glide.with(context)
               .load(item.getFoodImagePreview())
              .into(holder.image);
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

        TextView date;

        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.donation_date);
            image = itemView.findViewById(R.id.circleImage);

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Donations donation);

    }
}
