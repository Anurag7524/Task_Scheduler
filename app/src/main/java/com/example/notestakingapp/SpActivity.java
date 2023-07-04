package com.example.notestakingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SpActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp);

        Handler handler=new Handler();

            handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent iHome=new Intent(SpActivity.this,MainActivity.class);
                startActivity(iHome);
                finish();
            }
        },3000);
    }
}