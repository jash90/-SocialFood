package com.zimny.socialfood.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Food;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 22.08.2017.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder>{
    ArrayList<Food> foods = new ArrayList<>();

    public FoodAdapter(ArrayList<Food> foods) {
        this.foods = foods;
    }

    @Override
    public FoodAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_recyclerview,parent,false);
        ButterKnife.bind(this, itemView);
        return new FoodAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Food food = foods.get(position);
        holder.name.setText(food.getName());
        holder.cost.setText(String.valueOf(food.getCost())+" zł");
        holder.description.setText(food.getDescription());
        holder.nameRestaurant.setText(food.getRestaurant().getName());
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.foodImage)
        ImageView foodImage;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.cost)
        TextView cost;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.nameRestaurant)
        TextView nameRestaurant;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
