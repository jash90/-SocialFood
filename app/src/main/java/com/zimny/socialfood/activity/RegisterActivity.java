package com.zimny.socialfood.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zimny.socialfood.utils.ToolBox.MyToast;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.login)
    MaterialEditText login;
    @BindView(R.id.password)
    MaterialEditText password;
    @BindView(R.id.confirmpassword)
    MaterialEditText confirmPassword;
    @BindView(R.id.register)
    Button register;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering ...");
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())){
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(login.getText().toString(),password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    MyToast(String.format("Create account %s .",authResult.getUser().getEmail()),getBaseContext());
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    MyToast(e.getLocalizedMessage(),getBaseContext());
                                    progressDialog.dismiss();
                                }
                            });

                }

            }
        });

    }
}
