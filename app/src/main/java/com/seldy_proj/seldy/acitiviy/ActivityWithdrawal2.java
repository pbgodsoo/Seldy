package com.seldy_proj.seldy.acitiviy;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seldy_proj.seldy.R;
import com.seldy_proj.seldy.util.Emailcheck;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static android.content.ContentValues.TAG;

public class ActivityWithdrawal2 extends AppCompatActivity {

    TextView code_check;
    EditText id, email_check_code;
    ImageView email_icon;
    Button withdrawal_btn, email_check_btn;
    String code = null;
    boolean idc = false;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal2);

        //뒤로가기 버튼
        ImageButton button = (ImageButton) findViewById(R.id.btn_back);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        // 파이어베이스 접근 설정
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        id = findViewById(R.id.register_id);
        email_check_code = findViewById(R.id.email_check_code);

        code_check = findViewById(R.id.code_check);

        email_icon = findViewById(R.id.email_check_icon);

        withdrawal_btn = findViewById(R.id.withdrawal);
        email_check_btn = findViewById(R.id.email_check);



        //회원 탈퇴
        withdrawal_btn.setOnClickListener(new View.OnClickListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            @Override
            public void onClick(View v) {
                if (idc == true) {
                    user.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User account deleted.");
                                    }
                                }
                            });
                    Toast.makeText(ActivityWithdrawal2.this, "회원탈퇴 완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ActivityWithdrawal2.this, ActivityLogin.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ActivityWithdrawal2.this, "이메일을 인증해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });




        //이메일 인증
        email_check_btn.setOnClickListener(new View.OnClickListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            @Override
            public void onClick(View v) {
                //현재 로그인한 사용자 프로필 가져오기
                if (user != null) {
                    String email = user.getEmail().trim();
                    //Log.e("아무거나",email);
                    if (id.getText().toString().equals(email)){
                        //Toast.makeText(ActivityChangePass.this, "이메일 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
                        //이메일 인증코드
                        try {
                            Emailcheck emailsend = new Emailcheck("seldybase777@gmail.com", "!tpfel123");
                            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
                                @Override
                                protected Void doInBackground(Void... voids) {
                                    try {
                                        emailsend.sendMail("Seldy 인증코드 입니다.", emailsend.getEmailCode(), id.getText().toString());
                                    } catch (SendFailedException e) {
                                        Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                                    } catch (MessagingException e) {
                                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void unused) {
                                    super.onPostExecute(unused);
                                    Toast.makeText(getApplicationContext(), "이메일 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            };
                            task.execute();
                            code = emailsend.getEmailCode();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(ActivityWithdrawal2.this, "잘못된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        // 인증코드 확인
        email_check_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email_check_code.getText().toString().equals(code)) {
                    email_icon.setImageResource(R.drawable.success_icon);
                    code_check.setTextColor(Color.parseColor("#484848"));
                    code_check.setText("일치하는 코드입니다.");
                    idc = true;
                } else {
                    email_icon.setImageResource(R.drawable.fail_icon);
                    code_check.setTextColor(Color.parseColor("#FF7E7E"));
                    code_check.setText("일치하지 않는 코드입니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}