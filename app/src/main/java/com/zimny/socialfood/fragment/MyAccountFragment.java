package com.zimny.socialfood.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.franmontiel.fullscreendialog.FullScreenDialogContent;
import com.franmontiel.fullscreendialog.FullScreenDialogController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.zimny.socialfood.R;
import com.zimny.socialfood.activity.LoginActivity;
import com.zimny.socialfood.activity.ProfileActivity;
import com.zimny.socialfood.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MyAccountFragment extends Fragment implements FullScreenDialogContent {
    @BindView(R.id.userIcon)
    CircleImageView userIcon;
    @BindView(R.id.logout)
    LinearLayout logout;
    @BindView(R.id.userAccount)
    LinearLayout myAccount;
    @BindView(R.id.settings)
    LinearLayout settings;
    @BindView(R.id.firstname)
    TextView firstname;
    @BindView(R.id.lastname)
    TextView lastname;
    User user;
    FullScreenDialogController fullScreenDialogController;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myaccount, container, false);
        ButterKnife.bind(this, v);
        sharedPreferences = getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUid(dataSnapshot.getKey());
                StorageReference imageRef = storageReference.child(String.format("%s.png", firebaseAuth.getCurrentUser().getUid()));
                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(imageRef)
                        .asBitmap()
                        .placeholder(R.drawable.person)
                        .signature(new StringSignature(user.getImageUpload()))
                        .into(userIcon);
                firstname.setText(user.getFirstname());
                lastname.setText(user.getLastname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        myAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                sharedPreferencesEditor.putBoolean("logout",true);
                startActivity(intent);
            }
        });


        return v;
    }


    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        fullScreenDialogController = dialogController;
    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialogController) {
        return false;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialogController) {
        return false;
    }
}
