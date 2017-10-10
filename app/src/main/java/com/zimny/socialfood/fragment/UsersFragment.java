package com.zimny.socialfood.fragment;

import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
import com.zimny.socialfood.adapter.RequestFriendsAdapter;
import com.zimny.socialfood.adapter.UsersAdapter;
import com.zimny.socialfood.model.FoodOrder;
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
    @BindView(R.id.button)
    Button button;
    ArrayList<UserRequest> users;
    FriendsAndRequestAdapter usersAdapter;
    FirebaseAuth firebaseAuth;
    RequestFriendsAdapter requestFriendsAdapter;
    User user;
    Group group;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friends, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        users = new ArrayList<>();
        usersAdapter = new FriendsAndRequestAdapter(users);
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
                                users.add(new UserRequest(user,true));
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
        }
        else if(getActivity().getIntent().getStringExtra("group")!=null) {
            final String json = getActivity().getIntent().getStringExtra("group");
            XLog.d("GROUP "+json);
            if (json != null) {
                group = new Gson().fromJson(json, Group.class);
                for (User user : group.getUsers()){
                   databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        user.setUid(dataSnapshot.getKey());
                        users.add(new UserRequest(user,true));
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
            //    users=group.getUsers();
             //   usersAdapter.notifyDataSetChanged();
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
                                users.add(new UserRequest(user,false));
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
                            if (userRequest.getUid() ==dataSnapshot.getKey() && !userRequest.isRequest()) {
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
                                users.add(new UserRequest(user,true));
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
                        XLog.d(dataSnapshot);
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
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseReference.child("users").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                           databaseReference.child("relationships").child("deliveryrequest").child(firebaseAuth.getCurrentUser().getUid()).child(dataSnapshot.getKey()).setValue(true);
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
            });
        return v;
    }

}
