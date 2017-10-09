package com.zimny.socialfood.activity.details;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.FoodsFragment;
import com.zimny.socialfood.fragment.InfoFragment;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.Tag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;
import me.gujun.android.taggroup.TagGroup;

public class RestaurantDetailsActivity extends AppCompatActivity implements MaterialTabListener {
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.restaurantImageView)
    ImageView userImageView;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.tagGroup)
    TagGroup tagGroup;
    @BindView(R.id.materialTabsRestaurantDetails)
    TabLayout materialTabHost;
    @BindView(R.id.viewPagerRestaurantDetails)
    ViewPager viewPager;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    Restaurant restaurant;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String json = intent.getStringExtra("restaurant");
        if (json != null) {
            restaurant = new Gson().fromJson(json, Restaurant.class);
            address.setText(restaurant.getAddress().toString());
            phone.setText(String.valueOf(restaurant.getPhone()));
            collapsingToolbarLayout.setTitle(restaurant.getName());
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imageRef = storageReference.child(String.format("%s.png", restaurant.getUid()));
            Glide.with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .fitCenter()
                    .into(userImageView);

            materialTabHost.setBackgroundResource(R.color.colorPrimary);
            fragments = new ArrayList<>();
            fragments.add(new FoodsFragment());
            fragments.add(new InfoFragment());
            fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(fragmentAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(materialTabHost));
            materialTabHost.setupWithViewPager(viewPager);
            materialTabHost.getTabAt(0).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_restaurant_menu).color(Color.WHITE)).sizeDp(20));
            materialTabHost.getTabAt(1).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_info).color(Color.WHITE)).sizeDp(20));
            //  XLog.d(restaurant);
            if (restaurant.getTags() != null) {
                //    XLog.d(restaurant.getTags());
                tagGroup.setTags(Tag.getStringTags(restaurant.getTags()));
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
