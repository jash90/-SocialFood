package com.zimny.socialfood.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.franmontiel.fullscreendialog.FullScreenDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsContextWrapper;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.MainFragment;
import com.zimny.socialfood.fragment.MyAccountFragment;
import com.zimny.socialfood.fragment.OrderAndHistoryOrderFragment;
import com.zimny.socialfood.fragment.SocialFragment;
import com.zimny.socialfood.fragment.admin.AdminFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatMainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    FullScreenDialogFragment fullScreenDialogFragment;
    int navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        if (!getIntent().getBooleanExtra("admin", false)) {
            bottomNavigationView.getMenu().removeItem(R.id.admin);
        }

        fullScreenDialogFragment = new FullScreenDialogFragment.Builder(FlatMainActivity.this)
                .setTitle("Settings")
                .setContent(MyAccountFragment.class, savedInstanceState)
                .build();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.food: {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        MainFragment fragment = new MainFragment();
                        ft.replace(R.id.content, fragment);
                        ft.commit();
                        break;
                    }
                    case R.id.orders: {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        OrderAndHistoryOrderFragment fragment = new OrderAndHistoryOrderFragment();
                        ft.replace(R.id.content, fragment);
                        ft.commit();
                        break;
                    }
                    case R.id.social: {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        SocialFragment fragment = new SocialFragment();
                        ft.replace(R.id.content, fragment);
                        ft.commit();
                        break;
                    }
                    case R.id.admin: {
                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        AdminFragment fragment = new AdminFragment();
                        ft.replace(R.id.content, fragment);
                        ft.commit();
                        break;
                    }
                }
                searchView.closeSearch();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.food);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//                Intent intent = new Intent();
//                intent.setAction("searchend");
//                sendBroadcast(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Intent intent = new Intent();
                switch (navigation) {
                    case 0:
                        intent.setAction("foodsearch");
                        break;
                    case 1:
                        intent.setAction("restaurantsearch");
                        break;
                    case 2:
                        intent.setAction("friendsearch");
                        break;
                    case 3:
                        intent.setAction("groupsearch");
                        break;
                }
                intent.putExtra("search", newText);
                sendBroadcast(intent);
                return false;
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.account:
                fullScreenDialogFragment.show(getSupportFragmentManager(), null);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_search).color(Color.WHITE).sizeDp(18));
        searchView.setMenuItem(item);
        searchView.setBackIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_arrow_back).color(Color.WHITE).sizeDp(18));
        searchView.setCloseIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_close).color(Color.WHITE).sizeDp(18));
        //searchView.set(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_account_circle).color(Color.WHITE).sizeDp(20));
        MenuItem item2 = menu.findItem(R.id.account);
        item2.setIcon(new IconicsDrawable(getApplicationContext()).icon(GoogleMaterial.Icon.gmd_account_circle).color(Color.WHITE).sizeDp(18));


        return super.onCreateOptionsMenu(menu);
    }

    public void setItemNavigation(int item) {
        this.navigation = item;
    }


}
