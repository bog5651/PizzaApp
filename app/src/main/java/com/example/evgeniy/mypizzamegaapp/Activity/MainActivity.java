package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.User;
import com.example.evgeniy.mypizzamegaapp.R;

public class MainActivity extends AppCompatActivity{

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreferencesHelper.hssToken(this)) {
            RequestHelper.apiGetUser(SharedPreferencesHelper.getToken(this), new RequestHelper.ApiInterface.onCompleteGetUser() {
                @Override
                public void onSuccess(User u) {
                    Intent toStart = new Intent(context, MainPizza.class);
                    toStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(toStart);
                    finish();
                }

                @Override
                public void onFail(String error) {
                    Toast.makeText(context, "Сессия устарела, войдите снова", Toast.LENGTH_LONG).show();
                    Intent toStart = new Intent(context, LoginActivity.class);
                    toStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                    startActivity(toStart);
                    finish();
                }
            });
        } else {
            Intent toStart = new Intent(context, LoginActivity.class);
            toStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
            startActivity(toStart);
            finish();
        }
    }
}
