package com.example.tugasakhir.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tugasakhir.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button mBtnLogout;
    TextView mTvHelloUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        set hello user text view home page by current user's email
        mTvHelloUser = findViewById(R.id.tvEmailUser);
        Intent inIntent= getIntent();
        Bundle bundle = inIntent.getExtras();
        if (bundle != null) {
            mTvHelloUser.setText((String) bundle.get("email"));
        }

//        logout activity
        mBtnLogout= findViewById(R.id.btnLogout);
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }
}