package com.app.wheelsonadminapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.data.db.AppRepository;
import com.app.wheelsonadminapp.ui.auth.SignUpActivity;
import com.app.wheelsonadminapp.ui.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
               gotoHomeOrLogin();
            }
        }, 10000);
    }

    private void gotoHomeOrLogin(){
        AppRepository appRepository = new AppRepository(this);
        if(appRepository.getUser()!=null && appRepository.getUser().getEmail()!=null){
            startActivity(new Intent(SplashActivity.this, HomeActivity.class));
        }else {
            startActivity(new Intent(SplashActivity.this, SignUpActivity.class));
        }
        finish();
    }
}