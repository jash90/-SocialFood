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
import android.widget.Spinner;
import android.widget.Switch;

import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodOrderAdapter;
import com.zimny.socialfood.model.FoodOrder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminOrderAddFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.user)
    Spinner user;
    @BindView(R.id.date)
    Button selectDate;
    @BindView(R.id.group)
    Spinner group;
    @BindView(R.id.paying)
    Switch paying;
    @BindView(R.id.save)
    Button save;
    FoodOrderAdapter foodOrderAdapter;
    ArrayList<FoodOrder> foodOrders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_order_add, container, false);
        ButterKnife.bind(this, v);
        foodOrders = new ArrayList<>();
        foodOrders.add(new FoodOrder());
        foodOrderAdapter = new FoodOrderAdapter(foodOrders);
        recyclerView.setAdapter(foodOrderAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        return v;
    }

}
