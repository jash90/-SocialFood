package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.GroupsAdapter;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Tag;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GroupsFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    GroupsAdapter groupsAdapter;
    ArrayList<Group> groups;
    ArrayList<Tag> tags;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_group, container, false);
        ButterKnife.bind(this, v);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        groups = new ArrayList<>();
        tags = new ArrayList<>();
        groupsAdapter = new GroupsAdapter(groups);
        recyclerView.setAdapter(groupsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (getActivity().getIntent().getStringExtra("user") == null) {
            if (firebaseAuth != null) {
                firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {
                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                            final Group group = dataSnapshot.getValue(Group.class);
                            group.setUid(dataSnapshot.getKey());
                            //XLog.d(group);

                            group.setUsers(new ArrayList<User>());
                            groups.add(group);
                            groupsAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        } else {
            final String json = getActivity().getIntent().getStringExtra("user");
            if (json != null) {
                user = new Gson().fromJson(json, User.class);
                firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshots) {
                        for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                            final Group group = dataSnapshot.getValue(Group.class);
                            group.setUid(dataSnapshot.getKey());
                            group.setUsers(new ArrayList<User>());
                            databaseReference.child("groups").child(dataSnapshot.getKey()).child("users").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshots) {
                                   for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                                       if (user.getUid().equals(dataSnapshot.getKey())){
                                           groups.add(group);
                                           groupsAdapter.notifyDataSetChanged();
                                       }
                                   }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        return v;

    }
}
