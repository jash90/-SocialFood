package com.zimny.socialfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.UserFoodDetailsFragment;
import com.zimny.socialfood.fragment.UserRestaurantDetailsFragment;
import com.zimny.socialfood.model.User;

import net.yanzm.mth.MaterialTabHost;

import org.joda.time.LocalDate;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserDetailsActivity extends AppCompatActivity {
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
    MaterialTabHost materialTabHost;
    @BindView(R.id.viewPagerUserDetails)
    ViewPager viewPager;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    Boolean invite = true;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        if (json != null) {
            user = new Gson().fromJson(json, User.class);
            city.setText(user.getAddress().getCity());
            email.setText(user.getUsername());
            if (user.getBirthday() != null) {
                LocalDate birthday2 = LocalDate.fromDateFields(user.getBirthday());
                //XLog.d(birthday2);
                Period period = new Period(birthday2, LocalDate.now(), PeriodType.years());
                //XLog.d("BD " + period);
                age.setVisibility(View.VISIBLE);
                age.setText(String.format("%d y.o.", period.getYears()));
            } else {
                age.setVisibility(View.GONE);
            }
            collapsingToolbarLayout.setTitle(String.format("%s %s",user.getFirstname(),user.getLastname()));
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference();
            StorageReference imageRef = storageReference.child(String.format("%s.png", user.getUid()));
            Glide.with(getApplicationContext())
                    .using(new FirebaseImageLoader())
                    .load(imageRef)
                    .into(userImageView);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (invite) {
                        floatingActionButton.setImageResource(R.drawable.minus);
                        invite = !invite;
                    } else {
                        floatingActionButton.setImageResource(R.drawable.plus);
                        invite = !invite;
                    }
                }
            });
            ArrayList<Fragment> fragments = new ArrayList<>();
            UserFoodDetailsFragment userFoodDetailsFragment = new UserFoodDetailsFragment();
            UserRestaurantDetailsFragment userRestaurantDetailsFragment = new UserRestaurantDetailsFragment();
            fragments.add(userFoodDetailsFragment);
            fragments.add(userRestaurantDetailsFragment);
            SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), fragments);
            //materialTabHost.setBackgroundResource(R.color.white);
            for (int i = 0; i < pagerAdapter.getCount(); i++) {
                materialTabHost.addTab(pagerAdapter.getPageTitle(i));
            }


            viewPager.setAdapter(pagerAdapter);
            viewPager.addOnPageChangeListener(materialTabHost);
            materialTabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
                @Override
                public void onTabSelected(int position) {
                    viewPager.setCurrentItem(position);
                }
            });
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        ArrayList<Fragment> fragments = new ArrayList<>();

        public SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Food".toUpperCase(l);
                case 1:
                    return "Restaurant".toUpperCase(l);
            }
            return null;
        }
    }


}