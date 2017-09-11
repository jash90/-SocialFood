package com.zimny.socialfood.activity.details;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodOrderAdapter;
import com.zimny.socialfood.model.FoodOrder;
import com.zimny.socialfood.model.Order;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.paying)
    ImageView paying;
    @BindView(R.id.group)
    TextView group;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Order order;
    FirebaseAuth firebaseAuth;
    FoodOrderAdapter foodOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, android.R.color.white));
        firebaseAuth = FirebaseAuth.getInstance();
        String json = getIntent().getStringExtra("order");
        if (json != null) {
            order = new Gson().fromJson(json, Order.class);
            //XLog.d("XXX " + order);
            date.setText(new SimpleDateFormat("dd.MM.yyyy HH:mm").format(order.getDate()));
            Double sumPrice = 0.0;
            //XLog.d(order.getUid());
            for (FoodOrder foodOrder : order.getFoodOrders()) {
                sumPrice = sumPrice + (foodOrder.getPrice() * foodOrder.getCount());
                // XLog.d(foodOrder.getPrice() + " * " + foodOrder.getCount() + " " + foodOrder.getPrice() * foodOrder.getCount());
            }
            price.setText(String.format("%.2f zł", sumPrice));
            if (order.isPaying()) {
                paying.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(FontAwesome.Icon.faw_money).color(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent)).sizeDp(40));

            } else {
                paying.setImageDrawable(new IconicsDrawable(getApplicationContext()).icon(FontAwesome.Icon.faw_money).color(Color.parseColor("#BDBDBD")).sizeDp(40));
            }
            if (firebaseAuth.getCurrentUser() != null) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("groups").child(order.getUidGroup()).child("name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String nameGroup = dataSnapshot.getValue(String.class);
                        group.setText(nameGroup);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            foodOrderAdapter = new FoodOrderAdapter(order.getFoodOrders());
            recyclerView.setAdapter(foodOrderAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
