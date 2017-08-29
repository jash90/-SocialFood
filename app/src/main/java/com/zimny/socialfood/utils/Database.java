package com.zimny.socialfood.utils;

import com.elvishew.xlog.XLog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zimny.socialfood.model.Relationship;
import com.zimny.socialfood.model.User;

import java.util.ArrayList;

/**
 * Created by ideo7 on 29.08.2017.
 */

public class Database {
    public static ArrayList<User> getAllFriends(final String uidUser, final DatabaseReference databaseReference){
        final ArrayList<User> userArrayList = new ArrayList<>();
        databaseReference.child("relationships").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshots) {
                for (DataSnapshot dataSnapshot : dataSnapshots.getChildren()) {
                    final ArrayList<User> users = new ArrayList<User>();
                    Relationship relationship = dataSnapshot.getValue(Relationship.class);
                    if (relationship.getUidFriend1().equals(uidUser) || relationship.getUidFriend2().equals(uidUser)) {
                        if (relationship.getUidFriend1().equals(uidUser)) {
                            databaseReference.child("users").child(relationship.getUidFriend2()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    user.setUid(dataSnapshot.getKey());
                                    users.add(user);
                                    userArrayList.add(user);
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
        return userArrayList;
    }
    public ArrayList<User> getAllGroupUsers(String uidGroup){
        return null;
    }

}
