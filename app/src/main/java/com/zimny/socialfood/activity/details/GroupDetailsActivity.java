package com.zimny.socialfood.activity.details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.HistoryOrderFragment;
import com.zimny.socialfood.fragment.InfoFragment;
import com.zimny.socialfood.fragment.UsersFragment;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Tag;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;
import me.gujun.android.taggroup.TagGroup;

public class GroupDetailsActivity extends AppCompatActivity implements MaterialTabListener {
    private static final int REQUEST_CODE_PICKER = 2000;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.groupImageView)
    ImageView groupImageView;
    @BindView(R.id.user)
    TextView user;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.tagGroup)
    TagGroup tagGroup;
    FirebaseStorage firebaseStorage;
    @BindView(R.id.materialTabsGroupDetails)
    TabLayout materialTabHost;
    @BindView(R.id.viewPagerGroupDetails)
    ViewPager viewPager;
    @BindView(R.id.menu_yellow)
    FloatingActionMenu floatingActionMenu;
    @BindView(R.id.leaveGroup)
    FloatingActionButton leaveGroup;
    @BindView(R.id.deleteGroup)
    FloatingActionButton deleteGroup;
    @BindView(R.id.changeImage)
    FloatingActionButton changeImage;
    @BindView(R.id.changeOrder)
    FloatingActionButton changeOrder;
    @BindView(R.id.joinGroup)
    FloatingActionButton joinGroup;
    Group group;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;
    boolean invite = true;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("group") != null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseStorage = FirebaseStorage.getInstance();
            final StorageReference storageReference = firebaseStorage.getReference();
            String json = getIntent().getStringExtra("group");
            if (json != null) {
                group = new Gson().fromJson(json, Group.class);
                XLog.d(group);
                collapsingToolbarLayout.setTitle(group.getName());
                if (tagGroup.getTags() != null) {
                    ArrayList<String> tagss = new ArrayList<>();
                    for (Tag tag : group.getTags()) {
                        tagss.add(tag.getName());
                    }
                    tagGroup.setTags(tagss);
                }
                if (firebaseAuth != null) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference();

                }
            }

        }
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageReference.child(String.format("%s.png", group.getUid()));
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .placeholder(R.drawable.group)
                .into(groupImageView);
        materialTabHost.setBackgroundResource(R.color.colorPrimary);
        fragments = new ArrayList<>();
        fragments.add(new UsersFragment());
        if (firebaseAuth.getCurrentUser().getUid().equals(group.getAdmin())) {
            fragments.add(new HistoryOrderFragment());
        }
        fragments.add(new InfoFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(materialTabHost));
        materialTabHost.setupWithViewPager(viewPager);
        materialTabHost.getTabAt(0).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE)).sizeDp(20));
        if (firebaseAuth.getCurrentUser().getUid().equals(group.getAdmin())) {
            materialTabHost.getTabAt(1).setIcon((new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_assignment).color(Color.WHITE).sizeDp(20)));
        }
        materialTabHost.getTabAt(materialTabHost.getTabCount()-1).setIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_info).color(Color.WHITE).sizeDp(20));
        floatingActionMenu.setIconAnimated(false);
        if (group.getAdmin() != null) {
            if (group.getAdmin().equals(firebaseAuth.getCurrentUser().getUid())) {
                deleteGroup.setVisibility(View.VISIBLE);
                changeImage.setVisibility(View.VISIBLE);
                changeOrder.setVisibility(View.VISIBLE);
            } else {
                deleteGroup.setVisibility(View.GONE);
                changeImage.setVisibility(View.GONE);
                changeOrder.setVisibility(View.GONE);
            }
        } else {
            deleteGroup.setVisibility(View.GONE);
            changeImage.setVisibility(View.GONE);
            changeOrder.setVisibility(View.GONE);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("groups").child(group.getUid()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    if (dataSnapshot.getKey().equals(firebaseAuth.getCurrentUser().getUid())) {
                        invite = false;
                    }
                    XLog.d("GG GROUP " + dataSnapshot);
                }
                if (invite) {
                    joinGroup.setVisibility(View.VISIBLE);
                    leaveGroup.setVisibility(View.GONE);
                } else {
                    joinGroup.setVisibility(View.GONE);
                    leaveGroup.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialDialog.Builder(view.getContext())
                        .title("Groups")
                        .content(String.format("Do you want join to %s ?", group.getName()))
                        .positiveText("Send")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                joinGroup.setVisibility(View.GONE);
                                leaveGroup.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Added to %s", group.getName()), Snackbar.LENGTH_SHORT);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference.child("groups").child(group.getUid()).child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(true);
                                snackbar.show();
                                invite = !invite;
                                floatingActionMenu.close(true);
                            }
                        })
                        .show();
            }
        });

        deleteGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (group.getAdmin() != null) {
                    if (group.getAdmin().equals(firebaseAuth.getCurrentUser().getUid())) {
                        new MaterialDialog.Builder(view.getContext())
                                .title("Groups")
                                .content(String.format("Do you want delete %s ?", group.getName()))
                                .positiveText("Delete")
                                .negativeText("Cancel")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        joinGroup.setVisibility(View.VISIBLE);
                                        leaveGroup.setVisibility(View.GONE);
                                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Your remove from %s", group.getName()), Snackbar.LENGTH_SHORT);
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("groups").child(group.getUid()).removeValue();
                                        snackbar.show();
                                        invite = !invite;
                                        floatingActionMenu.close(true);
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You not delete group, because you aren't admin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (group.getAdmin() != null) {
                    if (group.getAdmin().equals(firebaseAuth.getCurrentUser().getUid())) {


                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_PICKER);
                    }
                }
            }
        });
        changeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (group.getAdmin() != null) {
                    if (group.getAdmin().equals(firebaseAuth.getCurrentUser().getUid())) {
                        new MaterialDialog.Builder(view.getContext())
                                .title("Order")
                                .content(String.format("Do you want open orders "))
                                .positiveText("Open")
                                .negativeText("Cancel")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Your open orders"), Snackbar.LENGTH_SHORT);
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("orders").child(group.getUid()).removeValue();
                                        snackbar.show();
                                        floatingActionMenu.close(true);
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You not open orders, because you aren't admin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        leaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (group.getAdmin() != null) {
                    if (!group.getAdmin().equals(firebaseAuth.getCurrentUser().getUid())) {
                        new MaterialDialog.Builder(view.getContext())
                                .title("Groups")
                                .content(String.format("Do you want remove from %s ?", group.getName()))
                                .positiveText("Remove")
                                .negativeText("Cancel")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        joinGroup.setVisibility(View.VISIBLE);
                                        leaveGroup.setVisibility(View.GONE);
                                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Your remove from %s", group.getName()), Snackbar.LENGTH_SHORT);
                                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                        databaseReference.child("groups").child(group.getUid()).child("users").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                        snackbar.show();
                                        invite = !invite;
                                        floatingActionMenu.close(true);
                                    }
                                })
                                .show();
                    } else {
                        Toast.makeText(getApplicationContext(), "You not leave group, because you are admin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new MaterialDialog.Builder(view.getContext())
                            .title("Groups")
                            .content(String.format("Do you want remove from %s ?", group.getName()))
                            .positiveText("Remove")
                            .negativeText("Cancel")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    joinGroup.setVisibility(View.VISIBLE);
                                    leaveGroup.setVisibility(View.GONE);
                                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Your remove from %s", group.getName()), Snackbar.LENGTH_SHORT);
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference.child("groups").child(group.getUid()).child("users").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                                    snackbar.show();
                                    invite = !invite;
                                    floatingActionMenu.close(true);
                                }
                            })
                            .show();
                }

            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        floatingActionMenu.close(true);
        if (requestCode == REQUEST_CODE_PICKER && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            // try {
            Picasso.with(getApplicationContext())
                    .load(data.getData())
                    .into(groupImageView);

            try {
                Bitmap userProfile = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference mountainsRef = storageRef.child(String.format("%s.png", group.getUid()));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                userProfile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] dataImage = baos.toByteArray();
                UploadTask uploadTask = mountainsRef.putBytes(dataImage);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(getApplicationContext(), exception.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.setAction("changeImageProfile");
                        sendBroadcast(intent);
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }


    public class FragmentAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
