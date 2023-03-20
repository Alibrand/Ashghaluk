package com.ksacp2022.ashghaluk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        firebaseAuth=FirebaseAuth.getInstance();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                //check if a user is already logged in
                //then go to homepage
                if(firebaseAuth.getCurrentUser()!=null)
                {
                    if(firebaseAuth.getCurrentUser().getEmail().equals("admin@ashghaluk.com"))
                    {
                        Intent intent = new Intent(SplashActivity.this,AdminHomeActivity. class);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SplashActivity.this,HomeActivity. class);
                        startActivity(intent);
                    }

                    finish();
                }
                else{
                    startActivity(new Intent(SplashActivity.this, SignInActivity.class));
                    finish();
                }

            }
        }, 4000);
    }
}