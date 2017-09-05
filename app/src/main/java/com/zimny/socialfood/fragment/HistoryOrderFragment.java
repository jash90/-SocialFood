package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.zimny.socialfood.adapter.OrderAdapter;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.FoodOrder;
import com.zimny.socialfood.model.Order;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HistoryOrderFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<Order> orders;
    OrderAdapter orderAdapter;
    FirebaseAuth firebaseAuth;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history_order, container, false);
        ButterKnife.bind(this, v);
        orders = new ArrayList<>();
        orderAdapter = new OrderAdapter(orders);
        recyclerView.setAdapter(orderAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            databaseReference.child("orders").orderByChild("uidUser").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                        Order order = dataSnapshot.getValue(Order.class);
                        order.setUid(dataSnapshot.getKey());
                        String s = dataSnapshot.child("date").getValue(String.class);
                        try {
                            order.setDate(new SimpleDateFormat("dd.MM.yyyy HH:mm").parse(s));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (order.getUidUser().equals(firebaseAuth.getCurrentUser().getUid())){
                            orders.add(order);
                            orderAdapter.notifyDataSetChanged();
                        }
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
