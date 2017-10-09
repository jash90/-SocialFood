package com.zimny.socialfood.activity.details;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.FoodsFragment;
import com.zimny.socialfood.fragment.GroupsFragment;
import com.zimny.socialfood.fragment.InfoFragment;
import com.zimny.socialfood.fragment.RestaurantFragment;
import com.zimny.socialfood.fragment.UsersFragment;
import com.zimny.socialfood.model.User;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;

public class UserDetailsActivity extends AppCompatActivity implements MaterialTabListener {
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.userImageView)
    ImageView userImageView;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.age)
    TextView age;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.materialTabsUserDetails)
    TabLayout materialTabHost;
    @BindView(R.id.viewPagerUserDetails)
    ViewPager viewPager;
    @BindView(R.id.multiple_actions)
    FloatingActionButton floatingActionButton;
    Boolean invite = false;
    User user;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String json = intent.getStringExtra("user");
        if (json != null) {
            user = new Gson().fromJson(json, User.class);
            city.setText(user.getAddress().getCity());
            email.setText(user.getUsername());
            if (user.getBirthday() != null) {
                LocalDate birthday2 = LocalDate.fromDateFields(user.getBirthday());
                Period period = new Period(birthday2, LocalDate.now(), PeriodType.years());
                age.setVisibility(View.VISIBLE);
                age.setText(String.format("%d y.o.", period.getYears()));
            } else {
                age.setVisibility(View.GONE);
            }
            collapsingToolbarLayout.setTitle(String.format("%s %s", user.getFirstname(), user.getLastname()));
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imageRef = storageReference.child(String.format("%s.png", user.getUid()));
            Glide.with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(userImageView);

            if (invite) {
                floatingActionButton.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE).sizeDp(20));
            } else {
                floatingActionButton.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_person_add).color(Color.WHITE).sizeDp(20));
            }

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!invite) {
                        floatingActionButton.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE).sizeDp(20));
                        Snackbar snackbar = Snackbar.make(view, "Add user from relationship", Snackbar.LENGTH_SHORT);
                        //  snackbar.getView().setBackgroundResource(R.color.white);
                        snackbar.show();
                        invite = !invite;
                    } else {
                        floatingActionButton.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_person_add).color(Color.WHITE).sizeDp(20));
                        Snackbar snackbar = Snackbar.make(view, "Remove user from relationship", Snackbar.LENGTH_SHORT);
                        // snackbar.getView().setBackgroundResource(R.color.white);
                        snackbar.show();
                        invite = !invite;
                    }
                }
            });
            materialTabHost.setBackgroundResource(R.color.colorPrimary);
            fragments = new ArrayList<>();
            fragments.add(new FoodsFragment());
            fragments.add(new RestaurantFragment());
            fragments.add(new GroupsFragment());
            fragments.add(new UsersFragment());
            fragments.add(new InfoFragment());
            fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
            viewPager.setAdapter(fragmentAdapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(materialTabHost));
            materialTabHost.setupWithViewPager(viewPager);
            materialTabHost.getTabAt(0).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_restaurant_menu).color(Color.WHITE)).sizeDp(20));
            materialTabHost.getTabAt(1).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_restaurant).color(Color.WHITE)).sizeDp(20));
            materialTabHost.getTabAt(2).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_group).color(Color.WHITE)).sizeDp(20));
            materialTabHost.getTabAt(3).setIcon(new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE).sizeDp(20));
            materialTabHost.getTabAt(4).setIcon(new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_info).color(Color.WHITE).sizeDp(20));

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