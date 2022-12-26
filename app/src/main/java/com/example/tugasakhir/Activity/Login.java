package com.example.tugasakhir.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tugasakhir.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

//    object firebase Auth
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    EditText mEmail,mPassword;
    TextView mLupaPass, mLinkDaftar;
    Button mMasuk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.ptEmailLogin);
        mPassword = findViewById(R.id.ptPasswordLogin);
        mLupaPass = findViewById(R.id.tvLupa);
        mLinkDaftar = findViewById(R.id.linkDaftar);
        mMasuk = findViewById(R.id.btnMasuk);

//        store instance object into var mAuth
        mAuth = FirebaseAuth.getInstance();

//        return firebase user if user already signin. but if not signin yet, return null value
        currentUser = mAuth.getCurrentUser();

//        Toast.makeText(Register.this, "check signed or not", Toast.LENGTH_SHORT).show();
        if (currentUser!=null){

//            redirect to home/main activity if email verified
//            set intent to home/main activity
            Intent main = new Intent(getApplicationContext(), MainActivity.class);
            main.putExtra("email", currentUser.getEmail());
            startActivity(main);
            finish();
        }

        mMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

//                user input validation
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email tidak dapat kosong");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password tidak dapat kosong");
                    return;
                }

//                authenticate user
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
//                        check if register successfully perform
                        if (task.isSuccessful()){
//                            redirect to home/main activity if email verified
//                            set intent to home/main activity
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            main.putExtra("email", user.getEmail());
                            startActivity(main);
                            finish();
                        }else{
//                            if fail Toast the message
                            Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        mLinkDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

    }
}