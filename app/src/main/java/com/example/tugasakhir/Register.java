package com.example.tugasakhir;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register extends AppCompatActivity {

//    object firebase Auth
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    EditText mEmail, mPassword,mKonfirmPass;
    TextView mLinkLogin;
    Button mBtnDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmail = findViewById(R.id.ptEmail);
        mPassword = findViewById(R.id.ptPassword);
        mKonfirmPass = findViewById(R.id.ptKonfirmPassword);
        mLinkLogin = findViewById(R.id.linkLogin);
        mBtnDaftar = findViewById(R.id.btnDaftar);

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

        mBtnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String konfirmPassword = mKonfirmPass.getText().toString().trim();

//                user input validation
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email tidak dapat kosong");
                    return;
                }
                if (TextUtils.isEmpty(password) | password.length() < 6) {
                    mPassword.setError("Password harus >= 6");
                    return;
                }
                if (TextUtils.isEmpty(konfirmPassword) | konfirmPassword.length() < 6) {
                    mKonfirmPass.setError("Password harus >= 6");
                    return;
                }
                if (!konfirmPassword.equals(password) ){
                    mKonfirmPass.setError("Konfirmasi password tidak sama");
                    return;
                }

//                Toast.makeText(Register.this, "going to create", Toast.LENGTH_SHORT).show();
//                register user to firebase
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
//                        check if register successfully perform
                        if (task.isSuccessful()){
//                            Toast.makeText(Register.this, "success create account", Toast.LENGTH_SHORT).show();
//                            redirect to home/main activity if email verified
//                            set intent to home/main activity
                            Intent main = new Intent(getApplicationContext(), MainActivity.class);
                            main.putExtra("email", user.getEmail());
                            startActivity(main);
                            finish();
                        }else{
//                            if fail Toast the message
                            Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

        mLinkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}