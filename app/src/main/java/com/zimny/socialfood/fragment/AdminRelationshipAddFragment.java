package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Relationship;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdminRelationshipAddFragment extends Fragment {
    @BindView(R.id.friends1)
    Spinner friends1;
    @BindView(R.id.friends2)
    Spinner friends2;
    @BindView(R.id.iniviteFriends)
    Switch inviteFriends;
    @BindView(R.id.save)
    Button save;
    FirebaseAuth firebaseAuth;
    ArrayList<User> users1;
    ArrayList<User> users2;
    ArrayAdapter<User> userArrayAdapter1;
    ArrayAdapter<User> userArrayAdapter2;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_relationship_add, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        users1 = new ArrayList<>();
        users2 = new ArrayList<>();
        userArrayAdapter1 = new ArrayAdapter<User>(getContext(), android.R.layout.simple_spinner_item, users1);
        userArrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friends1.setAdapter(userArrayAdapter1);
        userArrayAdapter2 = new ArrayAdapter<User>(getContext(), android.R.layout.simple_spinner_item, users2);
        userArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        friends2.setAdapter(userArrayAdapter2);
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setUid(dataSnapshot.getKey());
                        users1.add(user);
                        users2.add(user);
                        userArrayAdapter1.notifyDataSetChanged();
                        userArrayAdapter2.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    final DatabaseReference databaseReference = firebaseDatabase.getReference();
                    String uid = databaseReference.child("relationships").push().getKey();
                    if (friends1.getSelectedItem() != null && friends2.getSelectedItem() != null) {
                        User f1 = (User) friends1.getSelectedItem();
                        User f2 = (User) friends2.getSelectedItem();

                        final Relationship relationship = new Relationship(f1.getUid(), f2.getUid(), inviteFriends.isChecked());
                        databaseReference.child("relationships").child(uid).setValue(relationship).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getContext(), relationship.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                }
            });


        }
        return v;
    }


}
