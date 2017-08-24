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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.adapter.FoodAdapter;
import com.zimny.socialfood.adapter.GroupsAdapter;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Tag;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ideo7 on 24.08.2017.
 */

public class GroupsFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    GroupsAdapter groupsAdapter;
    ArrayList<Group> groups;
    ArrayList<Tag> tags;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.group_fragment,container,false);
        ButterKnife.bind(this,v);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        groups = new ArrayList<>();
        tags = new ArrayList<>();
        groupsAdapter = new GroupsAdapter(groups);
        recyclerView.setAdapter(groupsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (firebaseAuth != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();

            DatabaseReference testReference = databaseReference.child("test");
            testReference.child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        for(DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                            String idGrupy = groupSnapshot.getKey();

                            DataSnapshot tagiGrupy = groupSnapshot.child("tags");

                            if(tagiGrupy.exists()) {
                                for(DataSnapshot tagGrupy : tagiGrupy.getChildren()) {
                                    testReference.child("tags").child(tagGrupy.getKey())
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            final Query queryRestaurant = databaseReference.child("groups");
            queryRestaurant.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshots) {
                    for(DataSnapshot dataSnapshot : dataSnapshots.getChildren()){
                        final Group group = dataSnapshot.getValue(Group.class);
                        final ArrayList<Tag> list = new ArrayList<Tag>();
                        for (final Tag tag : group.getTags())
                        {
                            databaseReference.child("tags").child(tag.getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Tag tag2 = dataSnapshot.getValue(Tag.class);
                                    XLog.d(tag2);
                                    list.add(tag2);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                        group.setTags(list);
                        groups.add(group);
                        groupsAdapter.notifyDataSetChanged();
                        XLog.d(group);



                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    XLog.d(databaseError);
                }
            });
        }

        return v;
    }
}
