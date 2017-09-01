package com.zimny.socialfood.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zimny.socialfood.utils.ToolBox.MyToast;


public class LoginActivity extends AppCompatActivity {

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
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(login.getText().toString(), password.getText().toString());
            }
        });
        final Intent intent = getIntent();
        Boolean logoutFlag = intent.getBooleanExtra("logout", true);
        String user = intent.getStringExtra("user");
        String pass = intent.getStringExtra("pass");
        if (user != null && pass != null) {
            login(user, pass);
        }
        if (firebaseUser != null) {
            MyToast(String.format("You sign as %s .", firebaseUser.getEmail()), getBaseContext());
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            final DatabaseReference databaseReference = firebaseDatabase.getReference();
            databaseReference.child("users").child(firebaseUser.getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Intent loginActivity = new Intent(LoginActivity.this, Main_Activity_v2.class);
                        loginActivity.putExtra("admin", true);
                        startActivity(loginActivity);
                        finish();
                    } else {
                        Intent loginActivity = new Intent(LoginActivity.this, Main_Activity_v2.class);
                        startActivity(loginActivity);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    public void login(final String username, String password) {
        try {

            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            MyToast(String.format("You sign as %s .", authResult.getUser().getEmail()), getBaseContext());
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            final DatabaseReference databaseReference = firebaseDatabase.getReference();
                            databaseReference.child("users").child(authResult.getUser().getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent loginActivity = new Intent(LoginActivity.this, Main_Activity_v2.class);
                                        loginActivity.putExtra("admin", true);
                                        startActivity(loginActivity);
                                        finish();
                                    } else {
                                        Intent loginActivity = new Intent(LoginActivity.this, Main_Activity_v2.class);
                                        startActivity(loginActivity);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            MyToast("Firebase : " + e.getLocalizedMessage(), getBaseContext());
                        }
                    });


        } catch (Exception ex) {
            MyToast("App : " + ex.getLocalizedMessage(), getBaseContext());
        }
    }


}
