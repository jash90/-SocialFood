package com.zimny.socialfood.adapter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elvishew.xlog.XLog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Relationship;
import com.zimny.socialfood.model.Tag;
import com.zimny.socialfood.model.User;
import com.zimny.socialfood.utils.Database;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by ideo7 on 22.08.2017.
 */

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    ArrayList<Group> groups = new ArrayList<>();


    public GroupsAdapter(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @Override
    public GroupsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View viewHolder = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_group, parent, false);
        return new GroupsAdapter.ViewHolder(viewHolder);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Group group = groups.get(position);
        holder.nameGroup.setText(group.getName());
        holder.city.setText(group.getAddress().getCity());
        final ArrayList<User> userArrayList = new ArrayList<>();
        final MutualFriendsAdapter mutualFriendsAdapter = new MutualFriendsAdapter(userArrayList);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        databaseReference.child("groups").child(group.getUid()).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                final ArrayList<String> tagsString = new ArrayList<>();
                final ArrayList<Tag> tags = new ArrayList<>();
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    databaseReference.child("tags").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Tag tag = dataSnapshot.getValue(Tag.class);
                            tags.add(tag);
                            tagsString.add(tag.getName());
                            holder.tagGroup.setTags(tagsString);
                            XLog.d("TAG  " + tag);
                            XLog.d("TAGS " + tags);
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

        databaseReference.child("relationships").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    final ArrayList<User> users = new ArrayList<User>();
                    Relationship relationship = dataSnapshot.getValue(Relationship.class);
                    if (relationship.getUidFriend1().equals(firebaseAuth.getCurrentUser().getUid()) || relationship.getUidFriend2().equals(firebaseAuth.getCurrentUser().getUid())) {
                        if (relationship.getUidFriend1().equals(firebaseAuth.getCurrentUser().getUid())) {
                            databaseReference.child("users").child(relationship.getUidFriend2()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    user.setUid(dataSnapshot.getKey());
                                    users.add(user);
                                    userArrayList.add(user);
                                    group.setUsers(users);
                                    XLog.d("TAGS " + group);
                                    mutualFriendsAdapter.notifyDataSetChanged();
                                    XLog.d("TAGS " + users);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            databaseReference.child("users").child(relationship.getUidFriend1()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    user.setUid(dataSnapshot.getKey());
                                    users.add(user);
                                    userArrayList.add(user);
                                    group.setUsers(users);
                                    XLog.d("TAGS " + group);
                                    mutualFriendsAdapter.notifyDataSetChanged();
                                    XLog.d("TAGS " + users);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ArrayList<User> userss =Database.getAllFriends(firebaseAuth.getCurrentUser().getUid(),databaseReference);
        XLog.d(userss);
//        databaseReference.child("groups").child(group.getUid()).child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                XLog.d(dataSnapshot);
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        final ArrayList<User> users = new ArrayList<User>();
//                        databaseReference.child("relationships").addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
//                                    XLog.d(dataSnapshot2);
//                                    final Relationship relationship = dataSnapshot2.getValue(Relationship.class);
//                                    XLog.d(relationship);
//                                    if (relationship.getUidFriend1() != null && relationship.getUidFriend2() != null) {
//                                        if (relationship.getUidFriend1().equals(firebaseAuth.getCurrentUser().getUid()) || relationship.getUidFriend2().equals(firebaseAuth.getCurrentUser().getUid())) {
//                                            if (relationship.getUidFriend1().equals(firebaseAuth.getCurrentUser().getUid())) {
//                                                XLog.d(relationship);
//                                                databaseReference.child("users").child(relationship.getUidFriend2()).addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        User user = dataSnapshot.getValue(User.class);
//                                                        user.setUid(dataSnapshot.getKey());
//                                                        users.add(user);
//                                                        userArrayList.add(user);
//                                                        group.setUsers(users);
//                                                        XLog.d("TAGS " + group);
//                                                        mutualFriendsAdapter.notifyDataSetChanged();
//                                                        XLog.d("TAGS " + users);
//
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                            } else  if (relationship.getUidFriend2().equals(firebaseAuth.getCurrentUser().getUid())){
//                                                XLog.d(relationship);
//                                                databaseReference.child("users").child(relationship.getUidFriend1()).addValueEventListener(new ValueEventListener() {
//                                                    @Override
//                                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                                        User user = dataSnapshot.getValue(User.class);
//                                                        user.setUid(dataSnapshot.getKey());
//                                                        users.add(user);
//                                                        userArrayList.add(user);
//                                                        group.setUsers(users);
//                                                        XLog.d("TAGS " + group);
//                                                        mutualFriendsAdapter.notifyDataSetChanged();
//                                                        XLog.d("TAGS " + users);
//
//                                                    }
//
//                                                    @Override
//                                                    public void onCancelled(DatabaseError databaseError) {
//
//                                                    }
//                                                });
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


        XLog.d(group);
        holder.recyclerView.setAdapter(mutualFriendsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mutualFriendsAdapter);

    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameGroup)
        TextView nameGroup;
        @BindView(R.id.city)
        TextView city;
        @BindView(R.id.tagGroup)
        TagGroup tagGroup;
        @BindView(R.id.recyclerView)
        RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
