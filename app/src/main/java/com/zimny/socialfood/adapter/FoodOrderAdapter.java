package com.zimny.socialfood.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.FoodDetailsActivity;
import com.zimny.socialfood.model.FoodOrder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ideo7 on 04.09.2017.
 */

public class FoodOrderAdapter extends RecyclerView.Adapter<FoodOrderAdapter.ViewHolder> {
    ArrayList<FoodOrder> foodOrders = new ArrayList<>();

    public FoodOrderAdapter(ArrayList<FoodOrder> foodOrders) {
        this.foodOrders = foodOrders;
    }

    @Override
    public FoodOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shopping_basket, parent, false);
        return new FoodOrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FoodOrderAdapter.ViewHolder holder, final int position) {
        final FoodOrder foodOrder = foodOrders.get(position);
        holder.name.setText(foodOrder.getName());
        holder.countFood.setText(String.format("pcs %d",foodOrder.getCount()));
        holder.price.setText(String.format("%.2f zł",Double.valueOf(String.valueOf(foodOrder.getCount() * foodOrder.getPrice()))));
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child(String.format("%s.png", foodOrder.getUid()));
        Glide.with(holder.itemView.getContext())
                .using(new FirebaseImageLoader())
                .load(imageRef)
                .into(holder.foodImageCircle);
        holder.foodImageCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FoodDetailsActivity.class);
                intent.putExtra("uid", foodOrder.getUid());
                intent.putExtra("type", foodOrder.getType());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return foodOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.foodImageCircle)
        CircleImageView foodImageCircle;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.countFood)
        TextView countFood;
        @BindView(R.id.price)
        TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
