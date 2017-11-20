package com.zimny.socialfood.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.pnikosis.materialishprogress.ProgressWheel;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;

import butterknife.BindView;
import butterknife.ButterKnife;


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
    @BindView(R.id.progressWheel)
    ProgressWheel progressWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        progressWheel.setVisibility(View.INVISIBLE);
        firebaseAuth = FirebaseAuth.getInstance();
        login.setText(sharedPreferences.getString("login",""));
        password.setText(sharedPreferences.getString("password",""));
        if (sharedPreferences.getBoolean("logout",false)){
            login(login.getText().toString(), password.getText().toString());
        }
        if (firebaseAuth.getCurrentUser()!=null){
            login(login.getText().toString(), password.getText().toString());
        }
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(login.getText().toString(), password.getText().toString());
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressWheel.isSpinning()){
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


    }

    public void login(final String username, final String password) {
        if (!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"Network is Disconnected.",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            progressWheel.setVisibility(View.VISIBLE);
            firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.signInWithEmailAndPassword(username, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(), String.format("You sign as %s .", authResult.getUser().getEmail()), Toast.LENGTH_SHORT).show();
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            final DatabaseReference databaseReference = firebaseDatabase.getReference();
                            databaseReference.child("users").child(authResult.getUser().getUid()).child("admin").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        Intent loginActivity = new Intent(LoginActivity.this, FlatMainActivity.class);
                                        sharedPreferencesEditor.putString("login", username);
                                        sharedPreferencesEditor.putString("password", password);
                                        sharedPreferencesEditor.putBoolean("logout", false);
                                        sharedPreferencesEditor.commit();
                                        loginActivity.putExtra("admin", true);
                                        startActivity(loginActivity);
                                        finish();
                                    } else {
                                        Intent loginActivity = new Intent(LoginActivity.this, FlatMainActivity.class);
                                        sharedPreferencesEditor.putString("login", username);
                                        sharedPreferencesEditor.putString("password", password);
                                        sharedPreferencesEditor.putBoolean("logout", false);
                                        sharedPreferencesEditor.commit();
                                        startActivity(loginActivity);
                                        finish();
                                    }
                                    progressWheel.setVisibility(View.INVISIBLE);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    progressWheel.setVisibility(View.INVISIBLE);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Check connection internet.", Toast.LENGTH_SHORT).show();
                            progressWheel.setVisibility(View.INVISIBLE);
                        }
                    });


        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "App : " + ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            progressWheel.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
