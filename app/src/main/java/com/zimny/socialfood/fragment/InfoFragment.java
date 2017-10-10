package com.zimny.socialfood.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.Group;
import com.zimny.socialfood.model.Restaurant;
import com.zimny.socialfood.model.Tag;
import com.zimny.socialfood.model.User;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.gujun.android.taggroup.TagGroup;


public class InfoFragment extends Fragment {
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.cityLayout)
    LinearLayout cityLayout;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.mailLayout)
    LinearLayout mailLayout;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.phoneLayout)
    LinearLayout phoneLayout;
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.birthdayLayout)
    LinearLayout birthdayLayout;
    @BindView(R.id.tagGroup)
    TagGroup tagGroup;

    User user;
    Group group;
    Restaurant restaurant;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_info, container, false);
        ButterKnife.bind(this, v);
        if (getActivity().getIntent().getStringExtra("user") != null) {
            final String json = getActivity().getIntent().getStringExtra("user");
            if (json != null) {
                user = new Gson().fromJson(json, User.class);
                if (user.getAddress().getCity() != null) {
                    cityLayout.setVisibility(View.VISIBLE);
                    city.setText(user.getAddress().getCity());
                } else {
                    cityLayout.setVisibility(View.GONE);
                }
                if (user.getPhone() > 0) {
                    phoneLayout.setVisibility(View.VISIBLE);
                    phone.setText(String.valueOf(user.getPhone()));
                } else {
                    phoneLayout.setVisibility(View.GONE);
                }
                if (user.getUsername() != null) {
                    mailLayout.setVisibility(View.VISIBLE);
                    mail.setText(user.getUsername());
                } else {
                    mailLayout.setVisibility(View.GONE);
                }
                if (user.getBirthday() != null) {
                    birthdayLayout.setVisibility(View.VISIBLE);
                    birthday.setText(new SimpleDateFormat("dd.MM.yyyy").format(user.getBirthday()));
                } else {
                    birthdayLayout.setVisibility(View.GONE);
                }
            }
        } else if (getActivity().getIntent().getStringExtra("group") != null) {
            final String json = getActivity().getIntent().getStringExtra("group");
            if (json != null) {
                group = new Gson().fromJson(json, Group.class);
                if (group.getAddress().getCity() != null) {
                    cityLayout.setVisibility(View.VISIBLE);
                    city.setText(group.getAddress().getCity());
                } else {
                    cityLayout.setVisibility(View.GONE);
                }
                if (group.getPhone() > 0) {
                    phoneLayout.setVisibility(View.VISIBLE);
                    phone.setText(String.valueOf(group.getPhone()));
                } else {
                    phoneLayout.setVisibility(View.GONE);
                }
                if (group.getUidAdmin() != null) {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference databaseReference = firebaseDatabase.getReference();
                    databaseReference.child("friends").child(group.getUidAdmin()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            User user = dataSnapshot.getValue(User.class);
                            if (user.getUsername() != null) {
                                mailLayout.setVisibility(View.VISIBLE);
                                mail.setText(user.getUsername());
                            } else {
                                mailLayout.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    mailLayout.setVisibility(View.GONE);
                }
                birthdayLayout.setVisibility(View.GONE);
                if (group.getTags() != null) {
                    tagGroup.setTags(Tag.getStringTags(group.getTags()));
                }

            }
        } else if (getActivity().getIntent().getStringExtra("restaurant") != null) {
            final String json = getActivity().getIntent().getStringExtra("restaurant");
            if (json != null) {
                restaurant = new Gson().fromJson(json, Restaurant.class);
                if (restaurant.getAddress().getCity() != null) {
                    cityLayout.setVisibility(View.VISIBLE);
                    city.setText(restaurant.getAddress().toString());
                } else {
                    cityLayout.setVisibility(View.GONE);
                }
                if (restaurant.getPhone() > 0) {
                    phoneLayout.setVisibility(View.VISIBLE);
                    phone.setText(String.valueOf(restaurant.getPhone()));
                } else {
                    phoneLayout.setVisibility(View.GONE);
                }
                mailLayout.setVisibility(View.GONE);
                birthdayLayout.setVisibility(View.GONE);
                if (restaurant.getTags() != null) {
                    tagGroup.setTags(Tag.getStringTags(restaurant.getTags()));
                }

            }

        }

        return v;
    }


}
