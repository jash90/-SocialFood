package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.elvishew.xlog.XLog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodOrderAdapter;
import com.zimny.socialfood.model.FoodOrder;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShoppingBasketFragment extends Fragment {
    @BindView(R.id.clear)
    Button clear;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.group)
    Spinner group;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<FoodOrder> foodOrders;
    FoodOrderAdapter foodOrderAdapter;
    FirebaseAuth firebaseAuth;
    ArrayAdapter<Group> groupsAdapter;
    ArrayList<Group> groups;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping_basket, container, false);
        ButterKnife.bind(this, v);
        foodOrders = new ArrayList<>();
        groups = new ArrayList<>();
        groupsAdapter = new ArrayAdapter<Group>(getContext(), android.R.layout.simple_spinner_item, groups);
        group.setAdapter(groupsAdapter);
        foodOrderAdapter = new FoodOrderAdapter(foodOrders);
        recyclerView.setAdapter(foodOrderAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                    foodOrders.add(foodOrder);
                    foodOrderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                    for (Iterator<FoodOrder> iterator = foodOrders.iterator(); iterator.hasNext(); ) {
                        FoodOrder foodOrder1 = iterator.next();
                        if (foodOrder1.getUid() == foodOrder.getUid()) {
                            iterator.remove();
                        }
                    }
                    foodOrders.add(foodOrder);
                    foodOrderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                    foodOrders.remove(foodOrder);
                    foodOrderAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        databaseReference.child("groups").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    Group group = dataSnapshot.getValue(Group.class);
                    group.setUid(dataSnapshot.getKey());
                    groups.add(group);
                    groupsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar snackbar = Snackbar.make(view, "Shopping Basket is clear.", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar snackbar = Snackbar.make(view, "Error Shopping basket" + e.getLocalizedMessage(), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
                foodOrderAdapter.notifyDataSetChanged();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String orders = "";
                Double sumPrice = 0.0;
                for (FoodOrder foodOrder : foodOrders) {
                    orders += foodOrder.toString() + '\n';
                    sumPrice = sumPrice + (foodOrder.getPrice() * foodOrder.getCount());
                }
                orders += String.format("Sum : %.2f zł \n", sumPrice);
                new MaterialDialog.Builder(getContext())
                        .title("Order")
                        .content(orders)
                        .positiveText("Confirm")
                        .negativeText("No")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                                databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshots) {
                                        String orderKey = databaseReference.child("orders").push().getKey();
                                        Order order = new Order();
                                        ArrayList<FoodOrder> foodOrders = new ArrayList<FoodOrder>();
                                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                                            FoodOrder foodOrder = dataSnapshot.getValue(FoodOrder.class);
                                            foodOrders.add(foodOrder);
                                        }
                                        order.setUidGroup(((Group) group.getSelectedItem()).getUid());
                                        order.setUidUser(firebaseAuth.getCurrentUser().getUid());
                                        order.setDate(new SimpleDateFormat("dd.MM.yyyy HH:mm z").format(new Date().getTime()));
                                        order.setPaying(false);
                                        order.setFoodOrders(foodOrders);
                                        databaseReference.child("orders").child(orderKey).setValue(order);
                                        XLog.d(order.toString());
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                databaseReference.child("baskets").child(firebaseAuth.getCurrentUser().getUid()).child("foodOrders").removeValue();
                                foodOrderAdapter.notifyDataSetChanged();
                            }
                        })
                        .show();
            }
        });
        return v;
    }

}
