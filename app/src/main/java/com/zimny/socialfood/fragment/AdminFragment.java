package com.zimny.socialfood.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabListener;


public class AdminFragment extends Fragment implements MaterialTabListener {
    @BindView(R.id.materialHost)
    TabLayout materialTabHost;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    ArrayList<Fragment> fragments;
    FragmentAdapter fragmentAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin, container, false);
        ButterKnife.bind(this, v);
        fragments = new ArrayList<>();
        fragments.add(new AdminFoodAddFragment());
        fragments.add(new AdminGroupAddFragment());
        fragments.add(new AdminRelationshipAddFragment());
        fragments.add(new AdminRestaurantAddFragment());
        fragments.add(new AdminTagAddFragment());
        fragments.add(new AdminUserAddFragment());
        fragmentAdapter = new FragmentAdapter(getFragmentManager(), fragments);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(materialTabHost));
        materialTabHost.setupWithViewPager(viewPager);
        materialTabHost.getTabAt(0).setIcon((new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_restaurant_menu).color(Color.WHITE)));
        materialTabHost.getTabAt(1).setIcon((new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_group).color(Color.WHITE)));
        materialTabHost.getTabAt(2).setIcon((new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_handshake_o).color(Color.WHITE)));
        materialTabHost.getTabAt(3).setIcon((new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_restaurant).color(Color.WHITE)));
        materialTabHost.getTabAt(4).setIcon((new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_local_offer).color(Color.WHITE)));
        materialTabHost.getTabAt(5).setIcon(new IconicsDrawable(getContext()).icon(GoogleMaterial.Icon.gmd_person).color(Color.WHITE));


        return v;
    }

    public void setupViewPager() {


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
