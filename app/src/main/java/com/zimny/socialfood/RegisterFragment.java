package com.zimny.socialfood;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.zimny.socialfood.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zimny.socialfood.Utils.ToolBox.MyToast;

/**
 * Created by ideo7 on 11.08.2017.
 */

public class RegisterFragment extends Fragment {

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
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register,container,false);
        ButterKnife.bind(this,v);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
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
                                MyToast(String.format("Create account %s .",authResult.getUser().getEmail()),getActivity());
                                progressDialog.dismiss();
                                FragmentManager fm = getFragmentManager();
                                FragmentTransaction ft = fm.beginTransaction();
                                LoginFragment loginFragment = new LoginFragment();
                                ft.replace(R.id.content,loginFragment);
                                ft.addToBackStack("fragment");
                                ft.commit();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                MyToast(e.getLocalizedMessage(),getActivity());
                                progressDialog.dismiss();
                            }
                        });

                }

            }
        });


        return v;
    }
}
