package com.zimny.socialfood.activity.details;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.InfoFragment;
import com.zimny.socialfood.fragment.UsersFragment;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Tag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;
import me.gujun.android.taggroup.TagGroup;

public class GroupDetailsActivity extends AppCompatActivity implements MaterialTabListener {
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

    Group group;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;
    boolean invite = true;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("group") != null) {
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseUser = firebaseAuth.getCurrentUser();
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
        materialTabHost.setBackgroundResource(R.color.colorPrimary);
        fragments = new ArrayList<>();
        fragments.add(new UsersFragment());
        fragments.add(new InfoFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(materialTabHost));
        materialTabHost.setupWithViewPager(viewPager);
        materialTabHost.getTabAt(0).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE)).sizeDp(20));
        materialTabHost.getTabAt(1).setIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_info).color(Color.WHITE).sizeDp(20));

//        FloatingActionButton addedOnce = new FloatingActionButton(this);
//        addedOnce.setSize(FloatingActionButton.SIZE_MINI);
//        addedOnce.setTitle("Added once");
        //  floatingActionMenu.hideMenuButton(false);
        //   floatingActionButton.addButton(addedOnce);
        if (group.getAdmin() != null) {
            if (group.getAdmin().equals(firebaseAuth.getCurrentUser().getUid())) {
                deleteGroup.setVisibility(View.VISIBLE);
                changeImage.setVisibility(View.VISIBLE);
            } else {
                deleteGroup.setVisibility(View.GONE);
                changeImage.setVisibility(View.GONE);
            }
        } else {
            deleteGroup.setVisibility(View.GONE);
            changeImage.setVisibility(View.GONE);
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
                    //          floatingActionButton.setBackgroundResource(R.drawable.group_add_white);

                    //floatingActionButton.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_group_add).color(Color.WHITE).sizeDp(25));
                } else {
                    //       floatingActionButton.setBackgroundResource(R.drawable.group);
                    // floatingActionButton.setImageResource(R.drawable.minus);
//                    floatingActionButton.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_group).color(Color.WHITE).sizeDp(45));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (invite) {
//                    new MaterialDialog.Builder(view.getContext())
//                            .title("Groups")
//                            .content(String.format("Do you want join to %s ?", group.getName()))
//                            .positiveText("Send")
//                            .negativeText("Cancel")
//                            .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    floatingActionButton.setBackgroundResource(R.drawable.group);
//                                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Added to %s", group.getName()), Snackbar.LENGTH_SHORT);
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                                    databaseReference.child("groups").child(group.getUid()).child("users").child(firebaseAuth.getCurrentUser().getUid()).setValue(true);
//                                    snackbar.show();
//                                    invite = !invite;
//                                }
//                            })
//                            .show();
//                } else {
//                    new MaterialDialog.Builder(view.getContext())
//                            .title("Groups")
//                            .content(String.format("Do you want remove from %s ?", group.getName()))
//                            .positiveText("Remove")
//                            .negativeText("Cancel")
//                            .onPositive(new MaterialDialog.SingleButtonCallback() {
//                                @Override
//                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                    floatingActionButton.setBackgroundResource(R.drawable.group_add_white);
//                                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), String.format("Your remove from %s", group.getName()), Snackbar.LENGTH_SHORT);
//                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//                                    databaseReference.child("groups").child(group.getUid()).child("users").child(firebaseAuth.getCurrentUser().getUid()).removeValue();
//                                    snackbar.show();
//                                    invite = !invite;
//                                }
//                            })
//                            .show();
//
//
//                }
//            }
//        });

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
