package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.evgeniy.mypizzamegaapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnUseToken).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin: {
                Intent toStart = new Intent(this, LoginActivity.class);
                toStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                startActivity(toStart);
                finish();
            }
            break;
            case R.id.btnUseToken: {
                Intent toStart = new Intent(this, MainPizza.class);
                startActivity(toStart);
            }
            break;
        }
    }
}
