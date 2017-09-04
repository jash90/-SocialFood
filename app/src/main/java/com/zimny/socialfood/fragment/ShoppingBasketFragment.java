package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodOrderAdapter;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.FoodOrder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingBasketFragment extends Fragment {
    @BindView(R.id.clear)
    Button clear;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<FoodOrder> foodOrders;
    FoodOrderAdapter foodOrderAdapter;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_basket, container, false);
        ButterKnife.bind(this, v);
        foodOrders = new ArrayList<>();
        foodOrderAdapter = new FoodOrderAdapter(foodOrders);
        recyclerView.setAdapter(foodOrderAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        final FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                        XLog.d(dataSnapshot.getKey());
                        XLog.d(dataSnapshots.getKey());
                        databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").child(dataSnapshot.getKey()).child("uidFood").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                final String uidFood = dataSnapshot.getValue(String.class);
                                XLog.d("SEARCH "+uidFood);
                                databaseReference.child("foods").orderByKey().addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshots, String s) {
                                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                            XLog.d("FOOD "+dataSnapshot.getKey());
                                            if (dataSnapshot.getKey()!= null && uidFood!=null) {
                                                if (uidFood.equals(dataSnapshot.getKey())) {
                                                    Food food = dataSnapshot.getValue(Food.class);
                                                    food.setUid(dataSnapshot.getKey());
                                                    FoodOrder foodOrder2 = new FoodOrder(food, foodOrder.getCount());
                                                    foodOrders.add(foodOrder2);
                                                    foodOrderAdapter.notifyDataSetChanged();
                                                    XLog.d(dataSnapshot);
                                                    XLog.d(foodOrder2);
                                                }

                                            }
                                        }
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

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
//                     foodOrders.add(foodOrder);
//                     foodOrderAdapter.notifyDataSetChanged();
//                     XLog.d(dataSnapshot);
//                     XLog.d(foodOrder);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return v;
    }

}
