package com.zimny.socialfood.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.OrderDetails;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.FoodOrder;
import com.zimny.socialfood.model.Order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 05.09.2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    ArrayList<Order> orders = new ArrayList<>();
    FirebaseAuth firebaseAuth;

    public OrderAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order, parent, false);
        return new OrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Order order = orders.get(position);
        holder.date.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(order.getDate()));
        if (order.isPaying()) {
            holder.paying.setImageDrawable(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_money).color(ContextCompat.getColor(holder.itemView.getContext(),R.color.colorAccent)).sizeDp(40));

        } else {
            holder.paying.setImageDrawable(new IconicsDrawable(holder.itemView.getContext()).icon(FontAwesome.Icon.faw_money).color(Color.parseColor("#BDBDBD")).sizeDp(40));
        }
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
//            XLog.d("UID GROUP"+order.getUidGroup());
            databaseReference.child("groups").child(order.getUidGroup()).child("name").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String nameGroup = dataSnapshot.getValue(String.class);
                    holder.group.setText(nameGroup);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            final ArrayList<FoodOrder> foodOrders = new ArrayList<>();
            databaseReference.child("orders").child(order.getUid()).child("foodOrders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        final FoodOrder foodOrder = new FoodOrder();
                        foodOrder.setCount(((Long) dataSnapshot.child("count").getValue()).intValue());
                        final String uidFood = dataSnapshot.child("uidFood").getValue(String.class);
                        databaseReference.child("foods").orderByKey().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshots, String s) {
                                for (DataSnapshot dataSnapshot :dataSnapshots.getChildren()) {
                                    if (dataSnapshot.getKey().equals(uidFood)) {
                                        Food food = dataSnapshot.getValue(Food.class);
                                        food.setUid(dataSnapshot.getKey());
                                        food.setType(dataSnapshots.getKey());
                                        foodOrder.setFood(food);
                                        foodOrders.add(foodOrder);
                                        order.setFoodOrders(foodOrders);
                                        Double sumPrice = 0.0;
                                        for (FoodOrder foodOrder1 : foodOrders){
                                            sumPrice=sumPrice+(foodOrder1.getPrice()*foodOrder1.getCount());
                                        }
                                        holder.price.setText(String.format("%.2f zł",sumPrice));
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
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(holder.itemView.getContext(), orders.get(position).toString(),Toast.LENGTH_SHORT).show();
                String orderJson =new Gson().toJson(orders.get(position));
                Intent intent = new Intent(holder.itemView.getContext(), OrderDetails.class);
                intent.putExtra("json",orderJson);
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.paying)
        ImageView paying;
        @BindView(R.id.group)
        TextView group;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.layout)
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
