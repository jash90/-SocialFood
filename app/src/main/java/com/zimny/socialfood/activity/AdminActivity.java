package com.zimny.socialfood.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.AdminFoodAddFragment;
import com.zimny.socialfood.fragment.AdminGroupAddFragment;
import com.zimny.socialfood.fragment.AdminRelationshipAddFragment;
import com.zimny.socialfood.fragment.AdminRestaurantAddFragment;
import com.zimny.socialfood.fragment.AdminTagAddFragment;
import com.zimny.socialfood.fragment.AdminUserAddFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;


public class AdminActivity extends AppCompatActivity implements MaterialTabListener {
    @BindView(R.id.materialHost)
    TabLayout materialTabHost;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        fragments = new ArrayList<>();
        fragments.add(new AdminFoodAddFragment());
        fragments.add(new AdminGroupAddFragment());
        fragments.add(new AdminRelationshipAddFragment());
        fragments.add(new AdminRestaurantAddFragment());
        fragments.add(new AdminTagAddFragment());
        fragments.add(new AdminUserAddFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(materialTabHost));
        materialTabHost.setupWithViewPager(viewPager);
        materialTabHost.getTabAt(0).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_restaurant_menu).color(Color.WHITE)));
        materialTabHost.getTabAt(1).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_group).color(Color.WHITE)));
        materialTabHost.getTabAt(2).setIcon((new IconicsDrawable(getBaseContext()).icon(FontAwesome.Icon.faw_handshake_o).color(Color.WHITE)));
        materialTabHost.getTabAt(3).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_restaurant).color(Color.WHITE)));
        materialTabHost.getTabAt(4).setIcon((new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_local_offer).color(Color.WHITE)));
        materialTabHost.getTabAt(5).setIcon(new IconicsDrawable(getBaseContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE));

    }

    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm,ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments=fragments;
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
