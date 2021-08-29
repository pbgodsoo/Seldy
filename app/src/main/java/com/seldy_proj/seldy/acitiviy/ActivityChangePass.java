package com.seldy_proj.seldy.acitiviy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seldy_proj.seldy.R;
import com.seldy_proj.seldy.util.Emailcheck;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

import static android.content.ContentValues.TAG;

public class ActivityChangePass extends AppCompatActivity {

    TextView codeCheck, pwText, pwcText;
    EditText id, pw, pwc, emailCheckCode;
    ImageView emailIcon, pwIcon, pwcIcon;
    Button changeBtn, emailCheckBtn;
    String code = null;
    boolean idc = false, pwC = false, pwcC = false;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);

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
        pw = findViewById(R.id.register_pw);
        pwc = findViewById(R.id.register_pwc);
        emailCheckCode = findViewById(R.id.email_check_code);

        pwText = findViewById(R.id.pw_check);
        pwcText = findViewById(R.id.pwc_check);
        codeCheck = findViewById(R.id.code_check);

        // 아이콘들
        pwIcon = findViewById(R.id.pw_check_icon);
        pwcIcon = findViewById(R.id.pwc_check_icon);
        emailIcon = findViewById(R.id.email_check_icon);

        changeBtn = findViewById(R.id.change);
        emailCheckBtn = findViewById(R.id.email_check);

        //비밀번호 변경
        changeBtn.setOnClickListener(new View.OnClickListener() {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String newPassword;
            @Override
            public void onClick(View v) {
                // 가입 정보 가져오기
                String getUserPw = pw.getText().toString();
                String getUserPwc = pwc.getText().toString();

                if (idc == true) {
                    if (getUserPw.equals("") != true && getUserPwc.equals("") != true) {
                        if (pwC != true || pwcC != true) {
                            Toast.makeText(ActivityChangePass.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            newPassword = pw.getText().toString().trim();
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User password updated.");

                                        Toast.makeText(ActivityChangePass.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        Intent intent = new Intent(ActivityChangePass.this, ActivityLogin.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    } else {
                        Toast.makeText(ActivityChangePass.this, "공백없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ActivityChangePass.this, "이메일을 인증해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        //이메일 인증
        emailCheckBtn.setOnClickListener(new View.OnClickListener() {
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
                        Toast.makeText(ActivityChangePass.this, "잘못된 이메일 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


        // 인증코드 확인
        emailCheckCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailCheckCode.getText().toString().equals(code)) {
                    emailIcon.setImageResource(R.drawable.success_icon);
                    codeCheck.setTextColor(Color.parseColor("#484848"));
                    codeCheck.setText("일치하는 코드입니다.");
                    idc = true;
                } else {
                    emailIcon.setImageResource(R.drawable.fail_icon);
                    codeCheck.setTextColor(Color.parseColor("#FF7E7E"));
                    codeCheck.setText("일치하지 않는 코드입니다.");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        // 비밀번호 길이 설정
        pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pw.getText().toString().matches("") != true) {
                    String change_pw = pw.getText().toString();
                    if (change_pw.length() < 8 || change_pw.length() > 16) {
                        pwText.setTextColor(Color.parseColor("#FF7E7E"));
                        pwText.setText("8~16자 이내, 숫자, 특수문자 포함해주세요.");
                        pwIcon.setImageResource(R.drawable.fail_icon);
                    } else {
                        // 비밀번호 유효성 검사식 : 숫자, 특수문자
                        String val_symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
                        Pattern pattern_symbol = Pattern.compile(val_symbol);
                        Matcher matcher_symbol = pattern_symbol.matcher(pw.getText().toString());

                        if (matcher_symbol.find()) {
                            pwText.setText("");
                            pwIcon.setImageResource(R.drawable.success_icon);
                            pwC = true;
                        } else {
                            pwText.setTextColor(Color.parseColor("#FF7E7E"));
                            pwText.setText("숫자,특수문자를 사용해주세요.");
                            pwIcon.setImageResource(R.drawable.fail_icon);
                        }
                    }
                } else {
                    pwText.setText("");
                    pwIcon.setImageResource(R.drawable.register_border_solid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 비밀번호 서로 일치하는지 확인
        pwc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pwc.getText().toString().matches("") != true) {
                    String pwc_change = pwc.getText().toString();
                    if (pwc_change.length() < 8 || pwc_change.length() > 16) {
                        pwcText.setTextColor(Color.parseColor("#FF7E7E"));
                        pwcText.setText("8~16자 이내로 만들어주세요.");
                        pwcIcon.setImageResource(R.drawable.fail_icon);
                    } else {
                        if (pw.getText().toString().equals(pwc.getText().toString())) {
                            pwcText.setTextColor(Color.parseColor("#484848"));
                            pwcText.setText("비밀번호가 일치합니다.");
                            pwcIcon.setImageResource(R.drawable.success_icon);
                            pwcC = true;
                        } else {
                            pwcText.setTextColor(Color.parseColor("#FF7E7E"));
                            pwcText.setText("비밀번호가 일치하지 않습니다.");
                            pwcIcon.setImageResource(R.drawable.fail_icon);
                        }
                    }
                } else {
                    pwcText.setText("");
                    pwcIcon.setImageResource(R.drawable.register_border_solid);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
