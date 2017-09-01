package com.zimny.socialfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.elvishew.xlog.XLog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.FoodOrder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 01.09.2017.
 */

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.ViewHolder> {
    ArrayList<FoodOrder> foodOrders = new ArrayList<>();

    public FoodOrderAdapter(ArrayList<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    @Override
    public FoodOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_admin_order, parent, false);
        return new FoodOrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FoodOrderAdapter.ViewHolder holder, int position) {
        final ArrayList<String> foodString = new ArrayList<>();
        final ArrayList<Food> foods = new ArrayList<>();
        final ArrayAdapter<Food> arrayAdapter = new ArrayAdapter<Food>(holder.itemView.getContext(), android.R.layout.simple_spinner_item, foods);
        holder.foodOrder.setAdapter(arrayAdapter);
        if (position == 0) {
            holder.plusButton.setVisibility(View.VISIBLE);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("foods").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            Food food = dataSnapshot2.getValue(Food.class);
                            XLog.d(dataSnapshot2);
                            food.setUid(dataSnapshot2.getKey());
                            food.setType(dataSnapshot.getKey());
                            foods.add(food);
                            foodString.add(food.getName());
                            arrayAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            holder.plusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    foodOrders.add(new FoodOrder());
                    FoodOrderAdapter.super.notifyDataSetChanged();

                }
            });
        } else {
            holder.plusButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    4.0f
            );
            holder.countFood.setLayoutParams(param);
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("foods").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                            Food food = dataSnapshot2.getValue(Food.class);
                            XLog.d(dataSnapshot2);
                            food.setUid(dataSnapshot2.getKey());
                            food.setType(dataSnapshot.getKey());
                            foods.add(food);
                            foodString.add(food.getName());
                            arrayAdapter.notifyDataSetChanged();
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
    public int getItemCount() {
        return foodOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foodOrder)
        Spinner foodOrder;
        @BindView(R.id.plusButton)
        Button plusButton;
        @BindView(R.id.countFood)
        MaterialEditText countFood;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
