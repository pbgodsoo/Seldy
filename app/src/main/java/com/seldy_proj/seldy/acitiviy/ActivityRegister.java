package com.seldy_proj.seldy.acitiviy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.seldy_nawon.R;
import com.seldy_proj.seldy.util.Emailcheck;
import com.seldy_proj.seldy.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;


// 추가해야할 것 비밀번호-대소문자 특수문자, 닉네임-특수문자
public class ActivityRegister extends AppCompatActivity {

    ImageView prev_btn;
    TextView code_check, niknamec_text, pw_text, pwc_text;
    EditText name, id, nikname, pw, pwc, email_check_code, tel;
    ImageView email_icon, niknamec_icon, pw_icon, pwc_icon;
    Button signUp, email_check_btn;
    String code = null;
    boolean idc = false, pw_c = false, pwc_c = false;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitDiskReads().permitDiskWrites().permitNetwork().build());

        // 파이어베이스 접근 설정
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        prev_btn = findViewById(R.id.prev_btn);
        name = findViewById(R.id.register_name);
        id = findViewById(R.id.register_id);
        nikname = findViewById(R.id.register_nickname);
        pw = findViewById(R.id.register_pw);
        pwc = findViewById(R.id.register_pwc);
        email_check_code = findViewById(R.id.email_check_code);

        niknamec_text = findViewById(R.id.nickname_check);
        pw_text = findViewById(R.id.pw_check);
        pwc_text = findViewById(R.id.pwc_check);
        code_check = findViewById(R.id.code_check);

        // 아이콘들
        niknamec_icon = findViewById(R.id.nikname_check_icon);
        pw_icon = findViewById(R.id.pw_check_icon);
        pwc_icon = findViewById(R.id.pwc_check_icon);
        email_icon = findViewById(R.id.email_check_icon);

        signUp = findViewById(R.id.signUp);
        email_check_btn = findViewById(R.id.email_check);

        // 이전 아이콘 클릭시 로그인 화면으로
        prev_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // signUp 버튼 누를 시
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 가입 정보 가져오기
                String getUserName = name.getText().toString();
                String getUserId = id.getText().toString();
                String getUserNikname = nikname.getText().toString();
                String getUserPw = pw.getText().toString();
                String getUserPwc = pwc.getText().toString();

                if (getUserName.equals("") != true && getUserId.equals("") != true && getUserNikname.equals("") != true && getUserPw.equals("") != true && getUserPwc.equals("") != true) {
                    if (idc != true) {
                        Toast.makeText(ActivityRegister.this, "아이디를 확인해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (pw_c != true || pwc_c != true) {
                            Toast.makeText(ActivityRegister.this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                        } else {
                            mAuth.createUserWithEmailAndPassword(getUserId, getUserPw).addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String userId = user.getEmail();
                                        String uid = user.getUid();
                                        String name = getUserName.trim();
                                        String nikname = getUserNikname.trim();

                                        HashMap<Object, String> saveProfile = new HashMap<>();
                                        saveProfile.put("uid", uid);
                                        saveProfile.put("id", userId);
                                        saveProfile.put("name", name);
                                        saveProfile.put("nikname", nikname);

                                        mDatabase.child("user").child(uid).setValue(saveProfile);

                                        //가입이 되었으면 로그인 페이지로
                                        Toast.makeText(ActivityRegister.this, "가입되었습니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(ActivityRegister.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            });
                        }
                    }

                } else {
                    Toast.makeText(ActivityRegister.this, "공백없이 입력해주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 이메일 인증 코드
        email_check_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Emailcheck emailsend = new Emailcheck("seldybase777@gmail.com", "!tpfel123");
                    emailsend.sendMail("Seldy 인증코드 입니다.", emailsend.getEmailCode(), id.getText().toString());
                    code = emailsend.getEmailCode();
                    Toast.makeText(getApplicationContext(), "이메일 인증코드를 발송하였습니다.", Toast.LENGTH_SHORT).show();
                } catch (SendFailedException e) {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (MessagingException e) {
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
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
                        pw_text.setTextColor(Color.parseColor("#FF7E7E"));
                        pw_text.setText("8~16자 이내, 숫자, 특수문자 포함해주세요.");
                        pw_icon.setImageResource(R.drawable.fail_icon);
                    } else {
                        // 비밀번호 유효성 검사식 : 숫자, 특수문자
                        String val_symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
                        Pattern pattern_symbol = Pattern.compile(val_symbol);
                        Matcher matcher_symbol = pattern_symbol.matcher(pw.getText().toString());

                        if (matcher_symbol.find()) {
                            pw_text.setText("");
                            pw_icon.setImageResource(R.drawable.success_icon);
                            pw_c = true;
                        } else {
                            pw_text.setTextColor(Color.parseColor("#FF7E7E"));
                            pw_text.setText("숫자,특수문자를 사용해주세요.");
                            pw_icon.setImageResource(R.drawable.fail_icon);
                        }
                    }
                } else {
                    pw_text.setText("");
                    pw_icon.setImageResource(R.drawable.register_border_solid);
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
                        pwc_text.setTextColor(Color.parseColor("#FF7E7E"));
                        pwc_text.setText("8~16자 이내로 만들어주세요.");
                        pwc_icon.setImageResource(R.drawable.fail_icon);
                    } else {
                        if (pw.getText().toString().equals(pwc.getText().toString())) {
                            pwc_text.setTextColor(Color.parseColor("#484848"));
                            pwc_text.setText("비밀번호가 일치합니다.");
                            pwc_icon.setImageResource(R.drawable.success_icon);
                            pwc_c = true;
                        } else {
                            pwc_text.setTextColor(Color.parseColor("#FF7E7E"));
                            pwc_text.setText("비밀번호가 일치하지 않습니다.");
                            pwc_icon.setImageResource(R.drawable.fail_icon);
                        }
                    }
                } else {
                    pwc_text.setText("");
                    pwc_icon.setImageResource(R.drawable.register_border_solid);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // 닉네임 동적 확인
        nikname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nikname.getText().toString().matches("") != true) {
                    String nikname_length = nikname.getText().toString();
                    if (nikname_length.length() < 4 || nikname_length.length() > 20) {
                        niknamec_text.setTextColor(Color.parseColor("#FF7E7E"));
                        niknamec_text.setText("4~20자 이내로 만들어주세요.");
                        niknamec_icon.setImageResource(R.drawable.fail_icon);

                    } else {
                        niknamec_text.setText("");
                        niknamec_icon.setImageResource(R.drawable.success_icon);
                    }
                } else {
                    niknamec_text.setText("");
                    niknamec_icon.setImageResource(R.drawable.register_border_solid);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}