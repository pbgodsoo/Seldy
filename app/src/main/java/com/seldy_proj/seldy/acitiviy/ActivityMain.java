package com.seldy_proj.seldy.acitiviy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.seldy_nawon.R;
import com.seldy_proj.seldy.util.PreferenceManager;
import com.seldy_proj.seldy.R;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityMain extends AppCompatActivity {
    Button signout;
    FirebaseAuth mAuth;
    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        mAuth = FirebaseAuth.getInstance();

        signout = findViewById(R.id.signout);

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                PreferenceManager.removeKey(mContext, "AutoLogin");
                PreferenceManager.removeKey(mContext, "Id");
                PreferenceManager.removeKey(mContext,"Pw");

                Intent intent = new Intent(ActivityMain.this, ActivityLogin.class);
                startActivity(intent);
                finish();
            }
        });

        setContentView(R.layout.activity_main);

        Button imageButton = (Button) findViewById(R.id.btn_pro);
        Button imageButton2 = (Button) findViewById(R.id.btn_friends);
        Button imageButton3 = (Button) findViewById(R.id.btn_inquiry);
        Button imageButton4 = (Button) findViewById(R.id.btn_appver);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityProfile.class);
                startActivity(intent);

            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityFriends.class);
                startActivity(intent);

            }
        });

        imageButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityInquiry.class);
                startActivity(intent);
            }
        });

        imageButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAppver.class);
                startActivity(intent);
            }
        });

    }


}
