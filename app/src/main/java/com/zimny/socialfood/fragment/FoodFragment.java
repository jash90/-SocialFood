package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodAdapter;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.Restaurant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FoodFragment extends Fragment {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    FoodAdapter foodAdapter;
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            final Query queryRestaurant = databaseReference.child("restaurants");
            queryRestaurant.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                        restaurant.setUid(dataSnapshot.getKey());
                        XLog.d(restaurant);
                        restaurants.add(restaurant);
                    }
                    for (final Restaurant r : restaurants) {
                        Query queryFood = databaseReference.child("foods").child(r.getUid());
                        queryFood.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshots) {
                                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                    final String s = dataSnapshot.getKey();
                                    XLog.d(s);
                                    databaseReference.child("foods").child(r.getUid()).child(s).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshots) {
                                            for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                                Food f = dataSnapshot.getValue(Food.class);
                                                XLog.d(f);
                                                XLog.d(dataSnapshot);
                                                f.setRestaurant(r);
                                                f.setType(s);
                                                foods.add(f);
                                            }
                                            foodAdapter.notifyDataSetChanged();
                                        }


                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            foodAdapter = new FoodAdapter(foods);
            recyclerView.setAdapter(foodAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(foodAdapter);

        }
        return v;
    }
}