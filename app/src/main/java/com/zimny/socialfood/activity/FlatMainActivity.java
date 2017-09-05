package com.zimny.socialfood.activity;


import android.content.Intent;
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

import com.google.firebase.auth.FirebaseAuth;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
import com.zimny.socialfood.R;
import com.zimny.socialfood.fragment.AdminFragment;
import com.zimny.socialfood.fragment.MainFragment;
import com.zimny.socialfood.fragment.OrderAndHistoryOrderFragment;
import com.zimny.socialfood.fragment.SocialFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlatMainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bottomNavigationView)
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_flat);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        AdminOrderAddFragment fragment = new AdminOrderAddFragment();
//        ft.add(R.id.content, fragment);
//        ft.commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.food: {
                        FragmentManager fm =getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();
                        MainFragment fragment = new MainFragment();
                        ft.replace(R.id.content, fragment);
                        ft.commit();
                        break;
                    }
                    case R.id.orders: {
                        FragmentManager fm =getSupportFragmentManager();
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
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.food);
//        Intent intent = new Intent(FlatMainActivity.this, AdminActivity.class);
//        startActivity(intent);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                firebaseAuth.signOut();
                Intent intent = new Intent(FlatMainActivity.this, LoginActivity.class);
                intent.putExtra("logout", false);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
