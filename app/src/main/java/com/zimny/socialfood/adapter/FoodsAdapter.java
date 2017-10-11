package com.zimny.socialfood.adapter;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.details.FoodDetailsActivity;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.FoodOrder;
import com.zimny.socialfood.model.Tag;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> implements Filterable {
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Food> allfoods = new ArrayList<>();

    Filter foodFilter;

    public FoodsAdapter(ArrayList<Food> foods) {
        this.foods = foods;
        this.allfoods = foods;
    }

    @Override
    public FoodsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_circle_food, parent, false);
        return new FoodsAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.price.setText(String.valueOf(food.getPrice()) + " zł");

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(String.format("%s.png", food.getUid()));

        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .asBitmap()
                .placeholder(R.drawable.restaurant_menu)
                .signature(new StringSignature(food.getImageUpload()))
                .into(holder.foodImageCircle);

        holder.foodImageCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FoodDetailsActivity.class);
                intent.putExtra("food", new Gson().toJson(food));
                view.getContext().startActivity(intent);


            }
        });

        final ArrayList<Tag> tags = new ArrayList<>();
        final ArrayList<String> tagsString = new ArrayList<>();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        tagsString.add(food.getRestaurant().getName());
        tagsString.add(food.getName());
        tagsString.add(food.getType());
        holder.tagGroup.setTags(tagsString);
        databaseReference.child("foods").child(food.getType()).child(food.getUid()).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                if (dataSnapshots.exists() && dataSnapshots.hasChildren()) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        databaseReference.child("tags").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Tag tag = dataSnapshot.getValue(Tag.class);
                                tags.add(tag);
                                tagsString.add(tag.getName());
                                food.setTags(tags);
                                holder.tagGroup.setTags(tagsString);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String key = databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").getKey();
                databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").orderByChild("uid").equalTo(food.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {
                        if (dataSnapshots.getValue() != null) {
                            if (dataSnapshots.getChildren().iterator().hasNext()) {
                                DataSnapshot dataSnapshot = dataSnapshots.getChildren().iterator().next();
                                int count = ((Long) dataSnapshot.child("count").getValue()).intValue() + 1;
                                databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").child(dataSnapshot.getKey()).child("count").setValue(count);
                            }
                        } else {
                            String uidFoodOrder = databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").push().getKey();
                            FoodOrder foodOrder = new FoodOrder(food.getName(), food.getPrice(), food.getTags(), food.getDescription(), food.getUid(), food.getRestaurant(), food.getType(), food.getImageUpload(), 1);
                            databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").child(uidFoodOrder).setValue(foodOrder);


                        }
                        Snackbar snackbar = Snackbar.make(view, String.format("Adding 1 %s %.2f zł", food.getName(), food.getPrice()), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }


    @Override
    public int getItemCount() {
        return foods.size();
    }

    @Override
    public Filter getFilter() {
        if (foodFilter == null) {
            foodFilter = new FoodFilter();
        }
        return foodFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foodImageCircle)
        CircleImageView foodImageCircle;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.tagGroup)
        TagGroup tagGroup;
        @BindView(R.id.addButton)
        Button add;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class FoodFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            if (charSequence == null || charSequence.length() == 0) {
                results.values = allfoods;
                results.count = allfoods.size();
            } else {
                List<Food> resultList = new ArrayList<>();
                for (Food f : allfoods) {
                    if (f.getName().toUpperCase().startsWith(charSequence.toString().toUpperCase()))
                        resultList.add(f);
                }
                results.values = resultList;
                results.count = resultList.size();

            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.count != 0) {
                foods = (ArrayList<Food>) filterResults.values;
                notifyDataSetChanged();
            } else {
                foods = new ArrayList<>();
                notifyDataSetChanged();
            }
        }
    }
}
