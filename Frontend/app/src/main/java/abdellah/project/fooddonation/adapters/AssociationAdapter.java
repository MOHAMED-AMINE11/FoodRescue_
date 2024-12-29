package abdellah.project.fooddonation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import abdellah.project.fooddonation.R;
import abdellah.project.fooddonation.entity.Association;
import de.hdodenhof.circleimageview.CircleImageView;

public class AssociationAdapter extends RecyclerView.Adapter<AssociationAdapter.ViewHolder> {

    private Context context;
    private  OnItemClick onItemClick;
    private List<Association> itemList;

    // Constructor for the adapter
    public AssociationAdapter(Context context, List<Association> itemList,OnItemClick onItemClick) {
        this.context = context;
        this.itemList = itemList;
        this.onItemClick=onItemClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.association_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the current item
        Association item = itemList.get(position);

        holder.associationName.setText(item.getNom());
        //Glide.with(context)
            //    .load("") // Replace with your URL or local drawable
            //    .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            onItemClick.ItemClick(item);
        });// Assuming you have local drawable resources
    }


    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setAssociations(List<Association> associations) {
        this.itemList=associations;
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView associationName;
      CircleImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            associationName = itemView.findViewById(R.id.associationName);
            imageView=itemView.findViewById(R.id.circularImage);
        }
    }

    // Item class for the data model
    public static class Item {
        private String title;
        private String description;
        private int imageResId;

        public Item(String title, String description, int imageResId) {
            this.title = title;
            this.description = description;
            this.imageResId = imageResId;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public int getImageResId() {
            return imageResId;
        }
    }
    public interface  OnItemClick{
        public void ItemClick(Association association);
    }
}
