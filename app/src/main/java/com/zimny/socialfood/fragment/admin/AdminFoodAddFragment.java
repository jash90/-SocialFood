package com.zimny.socialfood.fragment.admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Food;
import com.zimny.socialfood.model.Restaurant;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminFoodAddFragment extends Fragment {
    @BindView(R.id.restaurant)
    Spinner restaurant;
    @BindView(R.id.typeEditText)
    MaterialEditText typeEdiText;
    @BindView(R.id.typeSpinner)
    Spinner typeSpinner;
    @BindView(R.id.typeEditTextRadio)
    RadioButton typeEditTextRadioButton;
    @BindView(R.id.typeSpinnerRadio)
    RadioButton typeSpinnerRadioButton;
    @BindView(R.id.nameFood)
    MaterialEditText nameFood;
    @BindView(R.id.price)
    MaterialEditText price;
    @BindView(R.id.description)
    MaterialEditText description;
    @BindView(R.id.save)
    Button save;
    ArrayAdapter<Restaurant> restaurantArrayAdapter;
    ArrayList<Restaurant> restaurantList;
    ArrayList<String> types;
    ArrayAdapter<String> typeArrayAdapter;
    FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_food_add, container, false);
        ButterKnife.bind(this, v);
        restaurantList = new ArrayList<>();
        types = new ArrayList<>();
        restaurantArrayAdapter = new ArrayAdapter<Restaurant>(getContext(), android.R.layout.simple_spinner_item, restaurantList);
        restaurantArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, types);
        typeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        restaurant.setAdapter(restaurantArrayAdapter);
        typeSpinner.setAdapter(typeArrayAdapter);
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.child("restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                restaurantList.clear();
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                    restaurant.setUid(dataSnapshot.getKey());
                    restaurantList.add(restaurant);
                    //  XLog.d(restaurant);

                }
                restaurantArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("foods").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                types.clear();
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    String type = dataSnapshot.getKey();
                    types.add(type);
                    //XLog.d(type, types);
                }
                typeArrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                if (typeEditTextRadioButton.isChecked()) {
                    if (!typeEdiText.getText().toString().isEmpty()) {

                        String uid = databaseReference.child("foods").child(typeEdiText.getText().toString()).push().getKey();
                        Food food = new Food();
                        food.setRestaurant(null);
                        food.setType(null);
                        food.setName(nameFood.getText().toString());
                        food.setDescription(description.getText().toString());
                        food.setPrice(Double.valueOf(price.getText().toString()));
                        databaseReference.child("foods").child(typeEdiText.getText().toString()).child(uid).setValue(food);
                        databaseReference.child("foods").child(typeEdiText.getText().toString()).child(uid).child(((Restaurant) restaurant.getSelectedItem()).getUid()).setValue(true);
                        Toast.makeText(getContext(), food.toString(), Toast.LENGTH_SHORT).show();
                    }
                } else if (typeSpinnerRadioButton.isChecked()) {
                    if (typeSpinner.getSelectedItemPosition() > -1) {

                        String uid = databaseReference.child("foods").child(typeEdiText.getText().toString()).push().getKey();
                        Food food = new Food();
                        food.setRestaurant(null);
                        food.setType(null);
                        food.setName(nameFood.getText().toString());
                        food.setDescription(description.getText().toString());
                        food.setPrice(Double.valueOf(price.getText().toString()));
                        databaseReference.child("foods").child((String) typeSpinner.getSelectedItem()).child(uid).setValue(food);
                        databaseReference.child("foods").child((String) typeSpinner.getSelectedItem()).child(uid).child(((Restaurant) restaurant.getSelectedItem()).getUid()).setValue(true);
                        Toast.makeText(getContext(), food.toString(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "Must choose type", Toast.LENGTH_SHORT).show();
                }
            }


        });
        return v;
    }

}
