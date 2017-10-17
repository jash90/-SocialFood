package com.zimny.socialfood.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.SearchUserAdapter;
import com.zimny.socialfood.model.Address;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.User;
import com.zimny.socialfood.model.UserRequest;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;


public class GroupAddFragment extends Fragment implements FullScreenDialogContent {


    @BindView(R.id.name)
    MaterialEditText name;
    @BindView(R.id.admin)
    MaterialEditText admin;
    @BindView(R.id.street)
    MaterialEditText street;
    @BindView(R.id.numberHouse)
    MaterialEditText numberHouse;
    @BindView(R.id.numberBuilding)
    MaterialEditText numberBuilding;
    @BindView(R.id.city)
    MaterialEditText city;
    @BindView(R.id.postalCode)
    MaterialEditText postalCode;
    @BindView(R.id.tagGroup)
    TagGroup tagGroup;
    @BindView(R.id.user)
    MaterialEditText searchUser;
    @BindView(R.id.groupUser)
    RecyclerView groupUser;
    ArrayList<UserRequest> groupUsers;
    ArrayList<UserRequest> allUsers;
    SearchUserAdapter adapter;
    User user;

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_new_group, container, false);
        ButterKnife.bind(this, v);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        groupUsers = new ArrayList<>();
        allUsers = new ArrayList<>();
        adapter = new SearchUserAdapter(groupUsers, allUsers);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        groupUser.setLayoutManager(layoutManager);
        groupUser.setItemAnimator(new DefaultItemAnimator());
        groupUser.setAdapter(adapter);
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());
                admin.setText(user.getFirstname() + " " + user.getLastname());
                street.setText(user.getAddress().getNameStreet());
                numberBuilding.setText(user.getAddress().getNumberBuilding());
                numberHouse.setText(user.getAddress().getNumberHouse());
                city.setText(user.getAddress().getCity());
                postalCode.setText(user.getAddress().getPostalCode());
                databaseReference.child("relationships").child("friends").child(user.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        databaseReference.child("users").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                user.setUid(dataSnapshot.getKey());
                                allUsers.add(new UserRequest(user, false));
                                groupUsers.add(new UserRequest(user, false));
                                adapter.notifyDataSetChanged();
                                XLog.d(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
//

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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter.getFilter().filter(editable.toString());
            }
        });
        return v;
    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        final String uidGroup = databaseReference.child("groups").push().getKey();
        Address address = new Address();
        if (!street.getText().toString().equals(null)) {
            address.setNameStreet(street.getText().toString());
        } else {
            return true;
        }
        if (!numberHouse.getText().toString().equals(null)) {
            address.setNumberHouse(numberHouse.getText().toString());
        } else {
            return true;
        }
        if (!numberBuilding.getText().toString().equals(null)) {
            address.setNumberBuilding(numberBuilding.getText().toString());
        }
        if (!city.getText().toString().equals(null)) {
            address.setCity(city.getText().toString());
        } else {
            return true;
        }
        if (!postalCode.getText().toString().equals(null)) {
            address.setPostalCode(postalCode.getText().toString());
        } else {
            return true;
        }
        Group group = new Group();
        group.setName(name.getText().toString());
        group.setAddress(address);
        group.setAdmin(user.getUid());
        databaseReference.child("groups").child(uidGroup).setValue(group);
        //databaseReference.child("groups").child(uidGroup).child("name").setValue(name.getText().toString());
        //databaseReference.child("groups").child(uidGroup).child("address").setValue(address);
        for (final String tag : tagGroup.getTags()) {
            databaseReference.child("tags").orderByChild("name").equalTo(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    if (dataSnapshots.exists()) {
                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                            databaseReference.child("groups").child(uidGroup).child("tags").child(dataSnapshot.getKey()).setValue(true);
                        }

                    } else {
                        final String uidTag = databaseReference.child("tags").push().getKey();
                        databaseReference.child("tags").child(uidTag).child("name").setValue(tag);
                        databaseReference.child("groups").child(uidGroup).child("tags").child(uidTag).setValue(true);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        for (UserRequest user : groupUsers) {
            if (user.isRequest()) {
                databaseReference.child("groups").child(uidGroup).child("users").child(user.getUid()).setValue(true);

            }
        }
        databaseReference.child("groups").child(uidGroup).child("users").child(user.getUid()).setValue(true);
        //databaseReference.child("groups").child(uidGroup).child("admin").child(user.getUid()).setValue(true);
        hideSoftKeyboard(getActivity());
        Toast.makeText(getContext(), "Add Group " + name.getText().toString(), Toast.LENGTH_SHORT).show();

        return false;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }
}
