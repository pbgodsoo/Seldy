package com.seldy_proj.seldy.acitiviy;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.seldy_nawon.R;
import com.seldy_proj.seldy.util.PreferenceManager;
import com.seldy_proj.seldy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class ActivityLogin extends AppCompatActivity {
    TextView id, pw, register_btn;
    ImageView login_btn;
    Context mContext;
    CheckBox auto_login_check;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;


        mAuth = FirebaseAuth.getInstance();
        if(PreferenceManager.getString(mContext,"AutoLogin").equals("true")){
            String getId = PreferenceManager.getString(mContext,"Id");
            String getPw = PreferenceManager.getString(mContext,"Pw");
            mAuth.signInWithEmailAndPassword(getId,getPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(ActivityLogin.this,"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        id = findViewById(R.id.login_id);
        pw = findViewById(R.id.login_pw);

        register_btn = findViewById(R.id.register_btn);
        login_btn = findViewById(R.id.login_btn);
        auto_login_check = findViewById(R.id.auto_login_check);

        // 로그인 버튼
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getId = id.getText().toString();
                String getPw = pw.getText().toString();

                // 자동 로그인 체크했을 때
                if(auto_login_check.isChecked()==true){
                    mAuth.signInWithEmailAndPassword(getId,getPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                    PreferenceManager.setString(mContext, "AutoLogin", "true");
                                    PreferenceManager.setString(mContext, "Id", getId);
                                    PreferenceManager.setString(mContext, "Pw", getPw);
                                    Log.d("자동로그인 " , PreferenceManager.getString(mContext,"AutoLogin"));
                                    Log.d("아이디 ", PreferenceManager.getString(mContext, "Id"));
                                    Log.d("비밀번호 ", PreferenceManager.getString(mContext, "Pw"));
                                    Toast.makeText(ActivityLogin.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                            }else {
                                Toast.makeText(ActivityLogin.this, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }else{
                    // 이중 로그인 방지
                    if(mAuth.getCurrentUser()!=null){
                        mAuth.signOut();
                        mAuth.signInWithEmailAndPassword(getId, getPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ActivityLogin.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(ActivityLogin.this, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        mAuth.signInWithEmailAndPassword(getId, getPw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ActivityLogin.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Toast.makeText(ActivityLogin.this, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });
                    }
                }
            }
        });
        // 회원가입 버튼
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this,ActivityRegister.class);
                startActivity(intent);
            }
        });


    }

}
