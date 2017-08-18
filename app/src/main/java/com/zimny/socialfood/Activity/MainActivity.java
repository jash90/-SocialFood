package com.zimny.socialfood.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
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
import com.zimny.socialfood.Fragment.MyAccountFragment;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 18.08.2017.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    User user;
    DatabaseReference databaseReference;
    ProfileDrawerItem profileDrawerItem;
    Bitmap userProfile = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        final AccountHeader accountHeader = new AccountHeaderBuilder()
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
        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Favorites Restaurant").withIcon(GoogleMaterial.Icon.gmd_store),
                        new PrimaryDrawerItem().withName("Favorites Food").withIcon(GoogleMaterial.Icon.gmd_restaurant_menu),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Shopping cart").withIcon(GoogleMaterial.Icon.gmd_shopping_basket),
                        new PrimaryDrawerItem().withName("Orders").withIcon(FontAwesome.Icon.faw_list_alt),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Groups").withIcon(FontAwesome.Icon.faw_users),
                        new PrimaryDrawerItem().withName("Friends").withIcon(FontAwesome.Icon.faw_user_circle_o).withBadge("20"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return true;
                    }
                })
                .build();
        if (firebaseUser != null) {
            databaseReference.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (user.getFirstname() != null && user.getLastname() != null) {
                            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                            StorageReference storageRef = firebaseStorage.getReference();
                            StorageReference imageRef = storageRef.child(String.format("%s.png", firebaseUser.getEmail()));
                            final long ONE_MEGABYTE = 1024 * 1024;
                            imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    userProfile = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    profileDrawerItem = new ProfileDrawerItem().withName(user.getFirstname() + " " + user.getLastname()).withEmail(firebaseUser.getEmail()).withIcon(userProfile);
                                    accountHeader.addProfiles(profileDrawerItem);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    profileDrawerItem = new ProfileDrawerItem().withName(user.getFirstname() + " " + user.getLastname()).withEmail(firebaseUser.getEmail()).withIcon(R.drawable.ic_app);
                                    Toast.makeText(getBaseContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    accountHeader.addProfiles(profileDrawerItem);
                                }
                            });

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setNavigationIcon(new IconicsDrawable(this).icon(GoogleMaterial.Icon.gmd_menu).color(Color.WHITE).sizeDp(16));

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        MyAccountFragment myAccountFragment = new MyAccountFragment();
        ft.replace(R.id.content, myAccountFragment);
        ft.addToBackStack("fragment");
        ft.commit();

    }

}
