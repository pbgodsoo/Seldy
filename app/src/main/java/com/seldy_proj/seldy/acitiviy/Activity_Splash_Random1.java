package com.seldy_proj.seldy.acitiviy;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//import com.example.seldy_nawon.R;
import com.seldy_proj.seldy.R;
public class Activity_Splash_Random1 extends AppCompatActivity {
    LinearLayout linearLayout;
    Animation anim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_random1);

        linearLayout = (LinearLayout)findViewById(R.id.splash_random1);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = new Intent(Activity_Splash_Random1.this, ActivityLogin.class);
                overridePendingTransition(R.anim.anim_splash_out_top, R.anim.anim_splash_in_down);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearLayout.startAnimation(anim);
    }
}
