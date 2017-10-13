package com.zimny.socialfood.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FriendsAndRequestAdapter;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.User;
import com.zimny.socialfood.model.UserRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UsersFragment extends Fragment {
    @BindView(R.id.userView)
    RecyclerView userView;
    ArrayList<UserRequest> users;
    FriendsAndRequestAdapter usersAdapter;
    FirebaseAuth firebaseAuth;
    ArrayList<UserRequest> alluserRequests;
    User user;
    Group group;
    private IntentFilter intentFilter = new IntentFilter("friendsearch");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            usersAdapter.getFilter().filter(intent.getStringExtra("search"));

        }


    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        alluserRequests = new ArrayList<>();
        usersAdapter = new FriendsAndRequestAdapter(users, alluserRequests);
        final RecyclerView.LayoutManager userlayoutManager = new LinearLayoutManager(getContext());
        userView.setLayoutManager(userlayoutManager);
        userView.setItemAnimator(new DefaultItemAnimator());
        userView.setAdapter(usersAdapter);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        if (getActivity().getIntent().getStringExtra("user") != null) {
            final String json = getActivity().getIntent().getStringExtra("user");
            if (json != null) {
                user = new Gson().fromJson(json, User.class);
                XLog.d("users");
                databaseReference.child("relationships").child("friends").child(user.getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        XLog.d(dataSnapshot);
                        databaseReference.child("users").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
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
                                XLog.d(user);
                                users.add(new UserRequest(user, true));
                                usersAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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
        } else if (getActivity().getIntent().getStringExtra("group") != null) {
            final String json = getActivity().getIntent().getStringExtra("group");
            XLog.d("GROUP " + json);
            if (json != null) {
                group = new Gson().fromJson(json, Group.class);
                databaseReference.child("groups").child(group.getUid()).child("users").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        databaseReference.child("users").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                user.setUid(dataSnapshot.getKey());
                                users.add(new UserRequest(user, true));
                                group.setUsers(users);
                                XLog.d(user);
                                usersAdapter.notifyDataSetChanged();
                                XLog.d(dataSnapshot);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        for (Iterator<UserRequest> iterator = users.iterator(); iterator.hasNext(); ) {
                            UserRequest userRequest = iterator.next();
                            if (userRequest.getUid().equals(dataSnapshot.getKey())) {
                                iterator.remove();
                            }
                        }
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } else {
            if (firebaseAuth.getCurrentUser() != null) {
                databaseReference.child("relationships").child("deliveryrequest").child(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        databaseReference.child("users").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
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
                                users.add(new UserRequest(user, false));
                               // XLog.d(user);
                                usersAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        for (Iterator<UserRequest> iterator = users.iterator(); iterator.hasNext(); ) {
                            UserRequest userRequest = iterator.next();
                            if (userRequest.getUid().equals(dataSnapshot.getKey()) && !userRequest.isRequest()) {
                                iterator.remove();
                            }
                        }
                        usersAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference.child("relationships").child("friends").child(firebaseAuth.getCurrentUser().getUid()).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        databaseReference.child("users").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
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
                                users.add(new UserRequest(user, true));
                                usersAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        for (Iterator<UserRequest> iterator = users.iterator(); iterator.hasNext(); ) {
                            UserRequest userRequest = iterator.next();
                            if (userRequest.getUid().equals(dataSnapshot.getKey())) {
                                iterator.remove();
                            }
                        }
                        usersAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                databaseReference.child("users").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
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
                        alluserRequests.add(new UserRequest(user, true));
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

        }

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }
}
