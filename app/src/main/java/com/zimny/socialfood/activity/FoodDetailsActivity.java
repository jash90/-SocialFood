package com.zimny.socialfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.MainFragment;
import com.zimny.socialfood.fragment.MyAccountFragment;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.Tag;
import com.zimny.socialfood.model.User;

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
    User user;
    DatabaseReference databaseReference;
    ProfileDrawerItem profileDrawerItem;
    AccountHeader accountHeader;
    Bitmap userProfile = null;
    Drawer drawer;
    Boolean admin = false;
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
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        profileDrawerItem = new ProfileDrawerItem();
        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                //        .addProfiles(new ProfileDrawerItem().withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon(R.drawable.ic_app))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Glide.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Glide.clear(imageView);
            }
        });
        Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                XLog.d(position);
                switch (position) {
                    case 1:
                        MainFragment mainFragment = new MainFragment();
                        ft.replace(R.id.content, mainFragment);
                        ft.commit();
                        drawer.closeDrawer();
                        return true;
                    case 13:
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.putExtra("logout", false);
                        startActivity(intent);
                        return true;
                    case 12:
                        MyAccountFragment myAccountFragment = new MyAccountFragment();
                        ft.replace(R.id.content, myAccountFragment);
                        ft.commit();
                        drawer.closeDrawer();
                        return true;
                    default:
                        return false;


                }
            }


        };
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .withDrawerItems(builderUser())
//                .addDrawerItems(
//                        new PrimaryDrawerItem().withName("Main").withIcon(GoogleMaterial.Icon.gmd_home),
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem().withName("Favorites Restaurant").withIcon(GoogleMaterial.Icon.gmd_store),
//                        new PrimaryDrawerItem().withName("Favorites Food").withIcon(GoogleMaterial.Icon.gmd_restaurant_menu),
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem().withName("Shopping cart").withIcon(GoogleMaterial.Icon.gmd_shopping_basket),
//                        new PrimaryDrawerItem().withName("Orders").withIcon(FontAwesome.Icon.faw_list_alt),
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem().withName("Groups").withIcon(FontAwesome.Icon.faw_users),
//                        new PrimaryDrawerItem().withName("Friends").withIcon(FontAwesome.Icon.faw_user_circle_o).withBadge("20"),
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings),
//                        new PrimaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
//                )
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .build();
        if (firebaseUser != null) {
            databaseReference.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getFirstname() != null && user.getLastname() != null) {
                            profileDrawerItem.withName(String.format("%s %s", user.getFirstname(), user.getLastname()));
                        }
                        if (user.getUsername() != null) {
                            profileDrawerItem.withEmail(user.getUsername());
                        }
                        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                        StorageReference storageRef = firebaseStorage.getReference();
                        StorageReference imageRef = storageRef.child(String.format("%s.png", firebaseUser.getUid()));
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profileDrawerItem.withIcon(uri);
                                accountHeader.addProfiles(profileDrawerItem);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                profileDrawerItem.withIcon(R.drawable.ic_app);
                                accountHeader.addProfiles(profileDrawerItem);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), databaseError.getDetails(), Toast.LENGTH_SHORT).show();
                }
            });


        }
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setNavigationIcon(new

                IconicsDrawable(this).
                icon(GoogleMaterial.Icon.gmd_menu).
                color(Color.WHITE).
                sizeDp(16));
    }

    public ArrayList<IDrawerItem> builderUser() {
        ArrayList<IDrawerItem> list = new ArrayList<>();
        list.add(new PrimaryDrawerItem().withName("Main").withIcon(GoogleMaterial.Icon.gmd_home));
        list.add(new DividerDrawerItem());
        list.add(new PrimaryDrawerItem().withName("Favorites Restaurant").withIcon(GoogleMaterial.Icon.gmd_store));
        list.add(new PrimaryDrawerItem().withName("Favorites Food").withIcon(GoogleMaterial.Icon.gmd_restaurant_menu));
        list.add(new DividerDrawerItem());
        list.add(new PrimaryDrawerItem().withName("Shopping cart").withIcon(GoogleMaterial.Icon.gmd_shopping_basket));
        list.add(new PrimaryDrawerItem().withName("Orders").withIcon(FontAwesome.Icon.faw_list_alt));
        list.add(new DividerDrawerItem());
        list.add(new PrimaryDrawerItem().withName("Groups").withIcon(FontAwesome.Icon.faw_users));
        list.add(new PrimaryDrawerItem().withName("Friends").withIcon(FontAwesome.Icon.faw_user_circle_o).withBadge("20"));
        list.add(new DividerDrawerItem());
        list.add(new PrimaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings));
        list.add(new PrimaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app));
        return list;
    }
}

