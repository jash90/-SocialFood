package com.zimny.socialfood;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zimny.socialfood.Utils.ToolBox.MyToast;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.login)
    MaterialEditText login;
    @BindView(R.id.password)
    MaterialEditText password;
    @BindView(R.id.signin)
    Button signIn;
    @BindView(R.id.signup)
    Button signup;
    @BindView(R.id.icon)
    ImageView icon;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(login.getText().toString(), password.getText().toString());
            }
        });
        if (login.getText().toString().isEmpty() && password.getText().toString().isEmpty()){
            login(login.getText().toString(), password.getText().toString());
        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                ft.replace(R.id.content,registerFragment);
                ft.addToBackStack("fragment");
                ft.commit();
            }
        });
        dialog = new ProgressDialog(getBaseContext());
        dialog.setMessage("Sign in...");
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(GoogleMaterial.Icon.gmd_home);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings);
       // final ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem().withName(user.getFirstname() + " " + user.getLastname()).withEmail(firebaseUser.getEmail()).withIcon(ContextCompat.getDrawable(getContext(), R.drawable.ic_app));
        final AccountHeader accountHeader = new AccountHeaderBuilder()
                .withHeaderBackground(R.color.primary)
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();
        final Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(accountHeader)
                .withSavedInstance(savedInstanceState)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Favorites Restaurant").withIcon(GoogleMaterial.Icon.gmd_store),
                        new PrimaryDrawerItem().withName("Favorites Food").withIcon(GoogleMaterial.Icon.gmd_restaurant_menu),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Shopping cart").withIcon(GoogleMaterial.Icon.gmd_shopping_basket),
                        new PrimaryDrawerItem().withName("Orders").withIcon(FontAwesome.Icon.faw_list_alt),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Groups").withIcon(FontAwesome.Icon.faw_users),
                        new PrimaryDrawerItem().withName("Friends").withIcon(FontAwesome.Icon.faw_user_circle_o).withBadge("20"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        return true;
                    }
                })
                .build();
    }

    public void login(final String username, String password) {
        try {
            dialog.show();
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(username,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialog.dismiss();
                            MyToast(String.format("You sign as %s .",authResult.getUser().getEmail()),getBaseContext());
                            FragmentManager fm = getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();
                            MyAccountFragment myAccountFragment = new MyAccountFragment();
                            ft.replace(R.id.content,myAccountFragment);
                            ft.commit();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            MyToast("Firebase : "+e.getLocalizedMessage(),getBaseContext());
                        }
                    });


        }
        catch (Exception ex){
            dialog.dismiss();
            MyToast("App : "+ex.getLocalizedMessage(),getBaseContext());
        }
    }


}
