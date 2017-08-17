package com.zimny.socialfood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.model.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.zimny.socialfood.Utils.ToolBox.MyToast;


public class MyAccountFragment extends Fragment {


    @BindView(R.id.firstname)
    MaterialEditText firstname;
    @BindView(R.id.lastname)
    MaterialEditText lastname;
    @BindView(R.id.street)
    MaterialEditText street;
    @BindView(R.id.numberHome)
    MaterialEditText numberHome;
    @BindView(R.id.numberBuilding)
    MaterialEditText numberBuilding;
    @BindView(R.id.city)
    MaterialEditText city;
    @BindView(R.id.postalCode)
    MaterialEditText postalCode;
    @BindView(R.id.save)
    Button save;
    @BindView(R.id.image)
    CircleImageView image;
    @BindView(R.id.logout)
    Button logout;

    private User user;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    private int REQUEST_CODE_PICKER = 2000;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_myaccount, container, false);
        ButterKnife.bind(this, v);
        sharedPreferences = getActivity().getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    if (user.getFirstname() != null)
                        firstname.setText(user.getFirstname());
                    if (user.getLastname() != null)
                        lastname.setText(user.getLastname());
                    if (user.getAddress() != null) {
                        if (user.getAddress().getNameStreet() != null) {
                            street.setText(user.getAddress().getNameStreet());
                        }
                        if (user.getAddress().getNumberBuilding() != 0) {
                            numberBuilding.setText(Integer.toString(user.getAddress().getNumberBuilding()));
                        }
                        if (user.getAddress().getNumberHouse() != 0) {
                            numberHome.setText(Integer.toString(user.getAddress().getNumberHouse()));
                        }
                        if (user.getAddress().getCity() != null) {
                            city.setText(user.getAddress().getCity());
                        }
                        if (user.getAddress().getPostalCode() != null) {
                            postalCode.setText(user.getAddress().getPostalCode());
                        }

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MyAccountFragment.this.getActivity(), image);
                popupMenu.inflate(R.menu.menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ImagePicker.create(MyAccountFragment.this)
                                .folderMode(true)
                                .single()
                                .start(REQUEST_CODE_PICKER);
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                LoginFragment fragment = new LoginFragment();
                ft.replace(R.id.content, fragment);
                ft.commit();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setFirstname(firstname.getText().toString());
                user.setLastname(lastname.getText().toString());
                user.getAddress().setNameStreet(street.getText().toString());
                user.getAddress().setCity(city.getText().toString());
                user.getAddress().setPostalCode(postalCode.getText().toString());
                if (!numberBuilding.getText().toString().isEmpty()) {
                    user.getAddress().setNumberBuilding(Integer.parseInt(numberBuilding.getText().toString()));
                }
                if (!numberHome.getText().toString().isEmpty()) {
                    user.getAddress().setNumberHouse(Integer.parseInt(numberHome.getText().toString()));
                }
                databaseReference.child("users").child(firebaseUser.getUid()).setValue(user);
                MyToast("Dane zmienione", getContext());
            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
            List<Image> images = ImagePicker.getImages(data);
            image.setImageBitmap(BitmapFactory.decodeFile(images.get(0).getPath()));
        }
    }
}
