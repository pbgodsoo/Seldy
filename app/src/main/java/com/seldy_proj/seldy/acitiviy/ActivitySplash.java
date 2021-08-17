package com.seldy_proj.seldy.acitiviy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.seldy_nawon.R;
import com.seldy_proj.seldy.util.PreferenceManager;
import com.seldy_proj.seldy.R;
import java.util.Random;

public class ActivitySplash extends AppCompatActivity {
    Animation anim;
    LinearLayout linearLayout;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mContext = this;
        linearLayout = (LinearLayout)findViewById(R.id.activity_splash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Random r = new Random();
                if(PreferenceManager.getString(mContext,"AutoLogin").equals("true")){
                    Intent intent = new Intent(ActivitySplash.this,ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }else{
                    int n = r.nextInt(100);
                    if(n%2==0){
                        Intent intent1 = new Intent(ActivitySplash.this, Activity_Splash_Random1.class);
                        startActivity(intent1);
                        finish();
                    }else{
                        Intent intent2 = new Intent(ActivitySplash.this, Activity_Splash_Random2.class);
                        startActivity(intent2);
                        finish();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        linearLayout.startAnimation(anim);
    }
}
