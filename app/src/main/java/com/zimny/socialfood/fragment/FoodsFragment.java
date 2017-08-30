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
import com.zimny.socialfood.adapter.FoodsAdapter;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.Restaurant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FoodsFragment extends Fragment {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    FoodsAdapter foodAdapter;
    ArrayList<Food> foods = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        ButterKnife.bind(this, v);
        XLog.d("open");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseAuth != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            Query queryFood = databaseReference.child("foods");
            queryFood.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(final DataSnapshot dataSnapshots) {
                    Food food = new Food();
                    food.setType(dataSnapshots.getKey());
                    for (final DataSnapshot dataSnapshot1 : dataSnapshots.getChildren()) {
                        XLog.d(dataSnapshot1.getKey());
                        databaseReference.child("foods").child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshots) {
                                XLog.d(dataSnapshots.getKey());
                                for (final DataSnapshot dataSnapshot2 : dataSnapshots.getChildren()) {
                                    final Food food = dataSnapshot2.getValue(Food.class);
                                    food.setType(dataSnapshot1.getKey());
                                    food.setUid(dataSnapshot2.getKey());
                                    databaseReference.child("foods").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("restaurant").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            XLog.d(dataSnapshot.getValue());
                                            databaseReference.child("restaurants").child((String) dataSnapshot.getValue()).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                                                    food.setRestaurant(restaurant);
                                                    XLog.d(food);
                                                    foods.add(food);
                                                    foodAdapter.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

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
            foodAdapter = new FoodsAdapter(foods);
            recyclerView.setAdapter(foodAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(foodAdapter);

        }
        return v;
    }
}