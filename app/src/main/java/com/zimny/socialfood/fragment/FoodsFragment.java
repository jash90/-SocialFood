package com.zimny.socialfood.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodsAdapter;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.Order;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FoodsFragment extends Fragment {
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    FoodsAdapter foodAdapter;
    ArrayList<Food> foods;
    User user;
    Restaurant restaurant;
    private IntentFilter intentFilter = new IntentFilter("foodsearch");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            foodAdapter.getFilter().filter(intent.getStringExtra("search"));

        }


    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);
        ButterKnife.bind(this, v);
        //XLog.d("open");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        foods = new ArrayList<>();
        foodAdapter = new FoodsAdapter(foods);
        recyclerView.setAdapter(foodAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(foodAdapter);
        if (getActivity().getIntent().getStringExtra("user") != null) {
            final String json = getActivity().getIntent().getStringExtra("user");
            if (json != null) {
                user = new Gson().fromJson(json, User.class);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("orders").orderByChild("uidUser").equalTo(user.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        final Order order = dataSnapshot.getValue(Order.class);
                        order.setUid(dataSnapshot.getKey());
                        databaseReference.child("orders").child(order.getUid()).child("foodOrders").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshotsFoodOrders) {
                                for (final DataSnapshot dataSnapshotsFoodOrder : dataSnapshotsFoodOrders.getChildren()) {
                                    final String uidFood = dataSnapshotsFoodOrder.child("uid").getValue(String.class);
                                    databaseReference.child("foods").orderByKey().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshots) {
                                            for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    if (dataSnapshot1.getKey() != null) {
                                                        if (uidFood.equals(dataSnapshot1.getKey())) {
                                                            final Food food = dataSnapshot1.getValue(Food.class);
                                                            food.setUid(dataSnapshot1.getKey());
                                                            food.setType(dataSnapshot.getKey());
                                                            databaseReference.child("restaurants").child(dataSnapshot1.child("restaurant").getValue(String.class)).addValueEventListener(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                                                                    restaurant.setUid(dataSnapshot.getKey());
                                                                    food.setRestaurant(restaurant);
                                                                    if (!foods.contains(food)) {
                                                                        foods.add(food);
                                                                        // XLog.d(foods);
                                                                        foodAdapter.notifyDataSetChanged();
                                                                    }
                                                                }

                                                                @Override
                                                                public void onCancelled(DatabaseError databaseError) {

                                                                }
                                                            });

                                                        }
                                                    }
                                                }
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
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else if (getActivity().getIntent().getStringExtra("restaurant") != null) {
            String json = getActivity().getIntent().getStringExtra("restaurant");
            restaurant = new Gson().fromJson(json, Restaurant.class);
            // XLog.d(user);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("foods").orderByKey().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            if (restaurant.getUid().equals(dataSnapshot1.child("restaurant").getValue(String.class))) {
                                final Food food = dataSnapshot1.getValue(Food.class);
                                food.setUid(dataSnapshot1.getKey());
                                food.setType(dataSnapshot.getKey());
                                food.setRestaurant(restaurant);
                                if (!foods.contains(food)) {
                                    foods.add(food);
                                    // XLog.d(foods);
                                    foodAdapter.notifyDataSetChanged();
                                }
                            }


                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        } else {
            if (firebaseAuth != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("foods").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshots) {
                        Food food = new Food();
                        food.setType(dataSnapshots.getKey());
                        for (final DataSnapshot dataSnapshot1 : dataSnapshots.getChildren()) {
                            //XLog.d(dataSnapshot1.getKey());
                            foods.clear();
                            databaseReference.child("foods").child(dataSnapshot1.getKey()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshots) {
                                    //XLog.d(dataSnapshots.getKey());
                                    for (final DataSnapshot dataSnapshot2 : dataSnapshots.getChildren()) {
                                        final Food food = dataSnapshot2.getValue(Food.class);
                                        food.setType(dataSnapshot1.getKey());
                                        food.setUid(dataSnapshot2.getKey());
                                        databaseReference.child("foods").child(dataSnapshot1.getKey()).child(dataSnapshot2.getKey()).child("restaurant").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                // XLog.d(dataSnapshot.getValue());
                                                databaseReference.child("restaurants").child((String) dataSnapshot.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                                                        food.setRestaurant(restaurant);
                                                        // XLog.d(food);
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

            } else {
                XLog.d("firebaseAuth not exits");
            }
        }


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}