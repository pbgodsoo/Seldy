package com.seldy_proj.seldy.acitiviy;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.seldy_proj.seldy.R;
import com.seldy_proj.seldy.util.Emailcheck;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class ActivityChangePass extends AppCompatActivity {

    TextView code_check, pw_text, pwc_text;
    EditText id, pw, pwc, email_check_code;
    ImageView email_icon, pw_icon, pwc_icon;
    Button change_btn, email_check_btn;
    String code = null;
    boolean idc = false, pw_c = false, pwc_c = false;
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
        email_check_code = findViewById(R.id.email_check_code);

        pw_text = findViewById(R.id.pw_check);
        pwc_text = findViewById(R.id.pwc_check);
        code_check = findViewById(R.id.code_check);

        // 아이콘들
        pw_icon = findViewById(R.id.pw_check_icon);
        pwc_icon = findViewById(R.id.pwc_check_icon);
        email_icon = findViewById(R.id.email_check_icon);

        change_btn = findViewById(R.id.change);
        email_check_btn = findViewById(R.id.email_check);

        change_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 가입 정보 가져오기
                String getUserId = id.getText().toString();
                String getUserPw = pw.getText().toString();
                String getUserPwc = pwc.getText().toString();
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
    }
}
