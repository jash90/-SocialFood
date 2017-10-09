package com.zimny.socialfood.activity.details;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    Group group;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;
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

//        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
//        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
//        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
//        toolbar.setNavigationIcon(new
//
//                IconicsDrawable(this).
//                icon(GoogleMaterial.Icon.gmd_menu).
//                color(Color.WHITE).
//                sizeDp(16));
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
