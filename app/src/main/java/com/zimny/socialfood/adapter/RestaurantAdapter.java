package com.zimny.socialfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.Tag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by ideo7 on 06.09.2017.
 */

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    ArrayList<Restaurant> restaurants = new ArrayList<>();

    public RestaurantAdapter(ArrayList<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public RestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restaurant, parent, false);
        return new RestaurantAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        holder.name.setText(restaurant.getName());
        if (restaurant.getAddress() != null) {
            if (restaurant.getAddress().getNumberBuilding().isEmpty()) {
                holder.address.setText(String.format("%s, %s %s", restaurant.getAddress().getCity(), restaurant.getAddress().getNameStreet(), restaurant.getAddress().getNumberHouse()));
            } else {
                holder.address.setText(String.format("%s, %s %s/%s", restaurant.getAddress().getCity(), restaurant.getAddress().getNameStreet(), restaurant.getAddress().getNumberHouse(), restaurant.getAddress().getNumberBuilding()));

            }
        }
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(String.format("%s.png", restaurant.getUid()));

        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .error(R.drawable.restaurant_store)
                .centerCrop()
                .into(holder.restaurantImageCircle);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final ArrayList<String> tagsString = new ArrayList<>();
        final ArrayList<Tag> tags = new ArrayList<>();
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("foods").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshots, String s) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        if (dataSnapshot.child("restaurant").getValue(String.class).equals(restaurant.getUid())) {
                            databaseReference.child("foods").child(dataSnapshots.getKey()).child(dataSnapshot.getKey()).child("tags").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshots) {
                                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren())
                                        databaseReference.child("tags").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                XLog.d(dataSnapshot);
                                                Tag tag = dataSnapshot.getValue(Tag.class);
                                                tag.setUid(dataSnapshot.getKey());
                                                if (!tagsString.contains(tag.getName())) {
                                                    tags.add(tag);
                                                    tagsString.add(tag.getName());
                                                    holder.tagGroup.setTags(tagsString);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.restaurantImageCircle)
        CircleImageView restaurantImageCircle;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.tagGroup)
        TagGroup tagGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
