package com.zimny.socialfood.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.details.GroupDetailsActivity;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Tag;
import com.zimny.socialfood.model.User;
import com.zimny.socialfood.view.MultiCircleView;

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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Group group = groups.get(position);
        // final int[] mutualFriends = {0};
        holder.nameGroup.setText(group.getName());
        //holder.city.setText(group.getAddress().getCity());
        //holder.mutualFriends.setText(String.format("Mutual Friends : %d", mutualFriends[0]));
        final ArrayList<User> userArrayList = new ArrayList<>();
        //   final MutualFriendsAdapter mutualFriendsAdapter = new MutualFriendsAdapter(userArrayList);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final ArrayList<String> tagsString = new ArrayList<>();
        final ArrayList<Tag> tags = new ArrayList<>();
        tags.add(new Tag(group.getAddress().getCity()));
        tagsString.add(group.getAddress().getCity());
        holder.usersIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), GroupDetailsActivity.class);
                intent.putExtra("group", new Gson().toJson(group));
                view.getContext().startActivity(intent);
            }
        });
        databaseReference.child("groups").child(group.getUid()).child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {

                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    databaseReference.child("tags").child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Tag tag = dataSnapshot.getValue(Tag.class);
                            tags.add(tag);
                            tagsString.add(tag.getName());
                            group.setTags(tags);
                            holder.tagGroup.setTags(tagsString);
                            //   XLog.d("TAG  " + tag);
                            //   XLog.d("TAGS " + tags);
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

//        databaseReference.child("relationships").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshots) {
//                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
//                    final ArrayList<User> users = new ArrayList<User>();
//                    Relationship relationship = dataSnapshot.getValue(Relationship.class);
//                    if (relationship.getUidFriend1().equals(firebaseAuth.getCurrentUser().getUid()) || relationship.getUidFriend2().equals(firebaseAuth.getCurrentUser().getUid())) {
//                        if (relationship.getUidFriend1().equals(firebaseAuth.getCurrentUser().getUid())) {
//                            databaseReference.child("users").child(relationship.getUidFriend2()).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    User user = dataSnapshot.getValue(User.class);
//                                    user.setUid(dataSnapshot.getKey());
//                                    users.add(user);
//                                    userArrayList.add(user);
//                                    group.setUsers(users);
//                                    XLog.d("TAGS " + group);
//                                    mutualFriendsAdapter.notifyDataSetChanged();
//                                    XLog.d("TAGS " + users);
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//                        } else {
//                            databaseReference.child("users").child(relationship.getUidFriend1()).addValueEventListener(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(DataSnapshot dataSnapshot) {
//                                    User user = dataSnapshot.getValue(User.class);
//                                    user.setUid(dataSnapshot.getKey());
//                                    users.add(user);
//                                    userArrayList.add(user);
//                                    group.setUsers(users);
//                                    XLog.d("TAGS " + group);
//                                    mutualFriendsAdapter.notifyDataSetChanged();
//                                    XLog.d("TAGS " + users);
//                                }
//
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//
//                                }
//                            });
//
//                        }
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        final ArrayList<User> users = new ArrayList<>();
        databaseReference.child("groups").child(group.getUid()).child("users").orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //  XLog.d(position + " NO GROUP " + dataSnapshot);
                User user = new User();
                user.setUid(dataSnapshot.getKey());
                users.add(user);
                userArrayList.add(user);
                group.setUsers(users);
                //XLog.d(group);
                //    XLog.d("TAGS " + group);
                MultiCircleView.setupMultiCircleView(holder.usersIcon, users);
                //  XLog.d("TAGS " + users);

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


//        databaseReference.child("groups").child(group.getUid()).child("users").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                XLog.d("USER KEY "+dataSnapshot);
//                final Relationship relationship = new Relationship(dataSnapshot.getKey(), firebaseAuth.getCurrentUser().getUid(), false);
//                final Relationship relationship1 = new Relationship(firebaseAuth.getCurrentUser().getUid(), dataSnapshot.getKey(), false);
//                databaseReference.child("relationships").addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                          //  XLog.d(dataSnapshot1);
//                            Relationship relationship2 = dataSnapshot1.getValue(Relationship.class);
//                            if (relationship2.equals(relationship)) {
//                                XLog.d("REL 1"+relationship2);
//                            } else if (relationship2.equals(relationship1)) {
//                                XLog.d("REL 2"+relationship2);
//                            } else {
//                                //  XLog.d(relationship2);
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        //     XLog.d(group);
//        holder.recyclerView.setAdapter(mutualFriendsAdapter);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);
//        holder.recyclerView.setLayoutManager(layoutManager);
//        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
//        holder.recyclerView.setAdapter(mutualFriendsAdapter);

    }


    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.nameGroup)
        TextView nameGroup;
        //@BindView(R.id.city)
        //TextView city;
        @BindView(R.id.tagGroup)
        TagGroup tagGroup;
        @BindView(R.id.usersIcon)
        MultiCircleView usersIcon;
//        @BindView(R.id.recyclerView)
//        RecyclerView recyclerView;
//        @BindView(R.id.mutualFriends)
//        TextView mutualFriends;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
