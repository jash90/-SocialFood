package com.zimny.socialfood.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Food;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    ArrayList<Food> foods = new ArrayList<>();

    public FoodAdapter(ArrayList<Food> foods) {
        this.foods = foods;
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food, parent, false);
        return new FoodAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.price.setText(String.valueOf(food.getPrice()) + " zł");
        if (!(food.getDescription() == null)) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(food.getDescription());
        } else {
            holder.description.setVisibility(View.GONE);
        }
        //      holder.nameRestaurant.setText(food.getRestaurant().getName());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(String.format("%s.png", food.getName())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                holder.foodImage.setVisibility(View.VISIBLE);
                Picasso.with(holder.itemView.getContext())
                        .load(uri)
                        .into(holder.foodImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.foodImage.setVisibility(View.GONE);

            }
        });
        holder.tagGroup.setTags(food.getRestaurant().getName(), food.getType());

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.foodImage)
        ImageView foodImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.description)
        TextView description;
        //        @BindView(R.id.nameRestaurant)
//        TextView nameRestaurant;
        @BindView(R.id.tagGroup)
        TagGroup tagGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
