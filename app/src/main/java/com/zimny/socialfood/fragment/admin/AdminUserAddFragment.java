package com.zimny.socialfood.fragment.admin;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Address;
import com.zimny.socialfood.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AdminUserAddFragment extends Fragment {
    @BindView(R.id.login)
    MaterialEditText login;
    @BindView(R.id.password)
    MaterialEditText password;
    @BindView(R.id.firstname)
    MaterialEditText firstname;
    @BindView(R.id.lastname)
    MaterialEditText lastname;
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
        View v = inflater.inflate(R.layout.fragment_admin_user_add, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                if (!login.getText().toString().isEmpty() && !password.getText().toString().isEmpty())
                    //XLog.d(login.getText().toString() + "   " + password.getText().toString() + " " + "kgfksgjdfusgdif");
                    firebaseAuth.createUserWithEmailAndPassword(login.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getContext(), authResult.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            DatabaseReference databaseReference = firebaseDatabase.getReference();
                            String uid = authResult.getUser().getUid();
                            final User user1 = new User();
                            if (!firstname.getText().toString().isEmpty()) {
                                user1.setFirstname(firstname.getText().toString());
                            }
                            if (!lastname.getText().toString().isEmpty()) {
                                user1.setLastname(lastname.getText().toString());
                            }
                            if (!login.getText().toString().isEmpty()) {
                                user1.setUsername(login.getText().toString());
                            }
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
                            user1.setAddress(address);
                            databaseReference.child("users").child(authResult.getUser().getUid()).setValue(user1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext(), user1.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            //XLog.d(e);
                            // XLog.d(firebaseAuth.getCurrentUser().getEmail());
                        }
                    });

            }
        });

        return v;
    }


}
