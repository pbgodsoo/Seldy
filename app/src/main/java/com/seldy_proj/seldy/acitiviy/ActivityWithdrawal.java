package com.seldy_proj.seldy.acitiviy;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.seldy_proj.seldy.R;

public class ActivityWithdrawal extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        ImageButton button = (ImageButton) findViewById(R.id.btn_back);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        TextView text = (TextView)findViewById(R.id.textView2);
        text.setMovementMethod(new ScrollingMovementMethod());

        //Withdrawal2로 이동
        Button imageButton = (Button) findViewById(R.id.btn_withdrawal2);

        imageButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityWithdrawal2.class);
                startActivity(intent);

            }
        });
    }
}