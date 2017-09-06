package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FriendsAdapter;
import com.zimny.socialfood.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FriendsFragment extends Fragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    ArrayList<User> users;
    FriendsAdapter friendsAdapter;
    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(friendsAdapter);
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("relationships").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {

                        final String friend1 = dataSnapshot.child("uidFriend1").getValue(String.class);
                        final String friend2 = dataSnapshot.child("uidFriend2").getValue(String.class);
                        // XLog.d(friend1+" "+friend2);
                        if (friend1.equals(firebaseAuth.getCurrentUser().getUid())) {
                            databaseReference.child("users").child(friend2).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    user.setUid(dataSnapshot.getKey());
                                    if (dataSnapshot.child("birthday").exists()) {
                                        String date = dataSnapshot.child("birthday").getValue(String.class);
                                        try {
                                            user.setBirthday(new SimpleDateFormat("dd.MM.yyyy").parse(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    users.add(user);
                                    friendsAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        if (friend2.equals(firebaseAuth.getCurrentUser().getUid())) {
                            databaseReference.child("users").child(friend1).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    user.setUid(dataSnapshot.getKey());
                                    if (dataSnapshot.child("birthday").exists()) {
                                        String date = dataSnapshot.child("birthday").getValue(String.class);
                                        try {
                                            user.setBirthday(new SimpleDateFormat("dd.MM.yyyy").parse(date));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    users.add(user);
                                    friendsAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        return v;
    }

}
