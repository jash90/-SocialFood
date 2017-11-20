package com.zimny.socialfood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;
import com.zimny.socialfood.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.login)
    MaterialEditText login;
    @BindView(R.id.password)
    MaterialEditText password;
    @BindView(R.id.confirmpassword)
    MaterialEditText confirmPassword;
    @BindView(R.id.firstname)
    MaterialEditText firstname;
    @BindView(R.id.lastname)
    MaterialEditText lastname;
    @BindView(R.id.city)
    MaterialEditText city;
    @BindView(R.id.register)
    Button register;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstname.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Firstname is require.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (lastname.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Lastname is require.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (city.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "City is require.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (login.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email/Login is require.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Password is require.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (confirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Repeat password is require.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm Password isn't the same.", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.createUserWithEmailAndPassword(login.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getApplicationContext(), String.format("Create account %s .", authResult.getUser().getEmail()), Toast.LENGTH_SHORT).show();
                                firebaseAuth.signInWithEmailAndPassword(login.getText().toString(), password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                                        User user = new User(authResult.getUser().getEmail(),firstname.getText().toString(),lastname.getText().toString(),city.getText().toString());
                                        databaseReference.child("users").child(authResult.getUser().getUid()).setValue(user);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                sharedPreferencesEditor.putString("login",authResult.getUser().getEmail());
                                sharedPreferencesEditor.putString("password",password.getText().toString());
                                sharedPreferencesEditor.putBoolean("logout",false);
                                sharedPreferencesEditor.commit();
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });

            }
        });

    }
}
