package com.zimny.socialfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.Tag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import me.grantland.widget.AutofitTextView;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by ideo7 on 30.08.2017.
 */

public class FoodDetailsActivity extends AppCompatActivity {
    @BindView(R.id.foodImage)
    CircleImageView foodImage;
    @BindView(R.id.nameFood)
    TextView nameFood;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.nameRestaurant)
    TextView nameRestaurant;
    @BindView(R.id.description)
    AutofitTextView description;
    @BindView(R.id.tagFood)
    TagGroup tagFood;
    @BindView(R.id.linearLayoutDescription)
    LinearLayout linearLayout;
    FirebaseStorage firebaseStorage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        final String uidFood = intent.getStringExtra("uid");
        final String typeFood = intent.getStringExtra("type");
        if (uidFood != null && typeFood != null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
            firebaseStorage = FirebaseStorage.getInstance();
            final StorageReference storageReference = firebaseStorage.getReference();
            if (firebaseAuth != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("foods").child(typeFood).child(uidFood).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Food food = dataSnapshot.getValue(Food.class);
                        food.setUid(dataSnapshot.getKey());
                        foodImage.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_restaurant_menu).sizeDp(10));
                        StorageReference imageRef = storageReference.child(String.format("%s.png", food.getUid()));
                        Glide.with(getApplicationContext())
                                .using(new FirebaseImageLoader())
                                .load(imageRef)
                                .error(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_restaurant_menu).sizeDp(150))
                                .into(foodImage);
                        if (food.getDescription() != null && !food.getDescription().isEmpty()) {
                            linearLayout.setVisibility(View.VISIBLE);
                            description.setText(food.getDescription());
                        } else {
                            linearLayout.setVisibility(View.GONE);
                        }
                        nameFood.setText(food.getName());
                        databaseReference.child("foods").child(typeFood).child(food.getUid()).child("restaurant").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String s = (String) dataSnapshot.getValue();
                                XLog.d(s);
                                databaseReference.child("restaurants").child(s).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                                        XLog.d(dataSnapshot);
                                        restaurant.setUid(dataSnapshot.getKey());
                                        nameRestaurant.setText(restaurant.getName());
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                final ArrayList<Tag> tags = new ArrayList<>();
                final ArrayList<String> tagsString = new ArrayList<>();
                databaseReference.child("foods").child(typeFood).child(uidFood).child("tags").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {
                        if (dataSnapshots.exists() && dataSnapshots.hasChildren()) {
                            for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                databaseReference.child("tags").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Tag tag = dataSnapshot.getValue(Tag.class);
                                        tagsString.add(tag.getName());
                                        tagFood.setVisibility(View.VISIBLE);
                                        tagFood.setTags(tagsString);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        } else {
                            tagFood.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

        }

        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
//        toolbar.setNavigationIcon(new
//
//                IconicsDrawable(this).
//                icon(GoogleMaterial.Icon.gmd_menu).
//                color(Color.WHITE).
//                sizeDp(16));
    }


}

