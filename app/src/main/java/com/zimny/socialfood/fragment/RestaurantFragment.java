package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.RestaurantAdapter;
import com.zimny.socialfood.model.Order;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<Restaurant> restaurants;
    RestaurantAdapter restaurantAdapter;
    FirebaseAuth firebaseAuth;
    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_restaurant, container, false);
        ButterKnife.bind(this, v);
        restaurants = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(restaurants);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(restaurantAdapter);
        firebaseAuth = FirebaseAuth.getInstance();
        if (getActivity().getIntent().getStringExtra("user") == null) {
            if (firebaseAuth.getCurrentUser() != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("restaurants").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {
                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                            Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                            restaurant.setUid(dataSnapshot.getKey());
                            restaurants.add(restaurant);
                            restaurantAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else {
            String json = getActivity().getIntent().getStringExtra("user");
            // XLog.d(json);
            if (json != null) {
                user = new Gson().fromJson(json, User.class);
                // XLog.d(user);
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
                                    final String uidFood = dataSnapshotsFoodOrder.child("uidFood").getValue(String.class);
                                    databaseReference.child("foods").orderByKey().addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshots) {
                                            for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    if (dataSnapshot1.getKey().equals(uidFood)) {
                                                        String uidRestaurant = dataSnapshot1.child("restaurant").getValue(String.class);
                                                        databaseReference.child("restaurants").child(uidRestaurant).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                                                                restaurant.setUid(dataSnapshot.getKey());
                                                                if (!restaurants.contains(restaurant)) {
                                                                    restaurants.add(restaurant);
                                                                    restaurantAdapter.notifyDataSetChanged();
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

        }
        return v;
    }

}
