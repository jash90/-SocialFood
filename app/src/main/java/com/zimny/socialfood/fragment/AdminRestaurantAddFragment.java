package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Address;
import com.zimny.socialfood.model.Restaurant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminRestaurantAddFragment extends Fragment {
    @BindView(R.id.nameRestaurant)
    MaterialEditText nameRestaurant;
    @BindView(R.id.phone)
    MaterialEditText phone;
    @BindView(R.id.city)
    MaterialEditText city;
    @BindView(R.id.street)
    MaterialEditText street;
    @BindView(R.id.numberBuilding)
    MaterialEditText numberBuilding;
    @BindView(R.id.numberHouse)
    MaterialEditText numberHouse;
    @BindView(R.id.postalCode)
    MaterialEditText postalCode;
    @BindView(R.id.save)
    Button save;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_admin_restaurant_add, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    if (!nameRestaurant.getText().toString().isEmpty() && !phone.getText().toString().isEmpty()) {
                        String uid = databaseReference.child("restaurants").push().getKey();
                        final Restaurant restaurant = new Restaurant(nameRestaurant.getText().toString(), Integer.valueOf(phone.getText().toString()));
                        Address address = new Address();
                        if (!city.getText().toString().isEmpty()) {
                            address.setCity(city.getText().toString());
                        }
                        if (!street.getText().toString().isEmpty()) {
                            address.setNameStreet(street.getText().toString());
                        }
                        if (!numberBuilding.getText().toString().isEmpty()) {
                            address.setNumberBuilding(numberBuilding.getText().toString());
                        }
                        if (!numberHouse.getText().toString().isEmpty()) {
                            address.setNumberHouse(numberHouse.getText().toString());
                        }
                        if (!postalCode.getText().toString().isEmpty()) {
                            address.setPostalCode(postalCode.getText().toString());
                        }
                        restaurant.setAddress(address);
                        databaseReference.child("restaurants").child(uid).setValue(restaurant).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), restaurant.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        return v;
    }

}
