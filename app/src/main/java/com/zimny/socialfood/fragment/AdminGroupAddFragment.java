package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Address;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Tag;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.apptik.widget.multiselectspinner.MultiSelectSpinner;


public class AdminGroupAddFragment extends Fragment {
    @BindView(R.id.nameGroup)
    MaterialEditText nameGroup;
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
    @BindView(R.id.tags)
    MultiSelectSpinner tagSpinner;
    @BindView(R.id.users)
    MultiSelectSpinner userSpinner;
    @BindView(R.id.save)
    Button save;
    ArrayList<User> users;
    ArrayList<Tag> tags;
    ArrayList<User> userArrayList;
    ArrayList<Tag> tagArrayList;
    ArrayAdapter<User> userArrayAdapter;
    ArrayAdapter<Tag> tagArrayAdapter;
    FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_admin_group_add, container, false);
        ButterKnife.bind(this, v);
        firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        userArrayList = new ArrayList<>();
        users = new ArrayList<>();
        tags = new ArrayList<>();
        tagArrayList = new ArrayList<>();
        userArrayAdapter = new ArrayAdapter<User>(getContext(), android.R.layout.simple_list_item_multiple_choice, userArrayList);
        tagArrayAdapter = new ArrayAdapter<Tag>(getContext(), android.R.layout.simple_list_item_multiple_choice, tagArrayList);

        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    user.setUid(dataSnapshot.getKey());
                    userArrayList.add(user);
                    userArrayAdapter.notifyDataSetChanged();
                }
                userSpinner.setListAdapter(userArrayAdapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    Tag tag = dataSnapshot.getValue(Tag.class);
                    tag.setUid(dataSnapshot.getKey());
                    tagArrayList.add(tag);
                    tagArrayAdapter.notifyDataSetChanged();
                }
                tagSpinner.setListAdapter(tagArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nameGroup.getText().toString().isEmpty()){
                    String uid =databaseReference.child("groups").push().getKey();
                    Group group = new Group();
                    Address address = new Address();
                    group.setName(nameGroup.getText().toString());
                    if (!city.getText().toString().isEmpty()){
                        address.setCity(city.getText().toString());
                    }
                    if (!street.getText().toString().isEmpty()){
                        address.setNameStreet(street.getText().toString());
                    }
                    if (!numberBuilding.getText().toString().isEmpty()){
                        address.setNumberBuilding(numberBuilding.getText().toString());
                    }
                    if (!numberHouse.getText().toString().isEmpty()){
                        address.setNumberHouse(numberHouse.getText().toString());
                    }
                    if (!postalCode.getText().toString().isEmpty()){
                        address.setPostalCode(postalCode.getText().toString());
                    }
                    group.setAddress(address);
                    databaseReference.child("groups").child(uid).setValue(group);


                    ArrayList<Tag> tags = new ArrayList<>();
                    for (int i=0;i<tagSpinner.getSelected().length;i++){
                        if (tagSpinner.getSelected()[i]){
                            databaseReference.child("groups").child(uid).child("tags").child(tagArrayList.get(i).getUid()).setValue(true);
                        }
                    }
                    ArrayList<User> users = new ArrayList<>();
                    for (int i=0;i<userSpinner.getSelected().length;i++){
                        if (userSpinner.getSelected()[i]){
                            databaseReference.child("groups").child(uid).child("users").child(userArrayList.get(i).getUid()).setValue(true);
                        }
                    }
                    
                    XLog.d(group);
                }else{
                    Toast.makeText(getContext(),"Group must have name",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }

}
