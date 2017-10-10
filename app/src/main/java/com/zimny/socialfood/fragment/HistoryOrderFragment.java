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
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.OrderAdapter;
import com.zimny.socialfood.model.Order;

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
            databaseReference.child("orders").orderByChild("uidUser").equalTo(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Order order = dataSnapshot.getValue(Order.class);
                    order.setUid(dataSnapshot.getKey());
                    orders.add(order);
                    orderAdapter.notifyDataSetChanged();
                    XLog.d("ADD");
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Order order = dataSnapshot.getValue(Order.class);
                    order.setUid(dataSnapshot.getKey());
                    orders.remove(order);
                    XLog.d(orders.contains(order));
                    orderAdapter.notifyDataSetChanged();
                    XLog.d("REMOVE " + dataSnapshot);

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        return v;
    }


}
