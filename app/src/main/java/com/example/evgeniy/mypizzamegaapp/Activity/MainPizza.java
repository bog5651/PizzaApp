package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.User;
import com.example.evgeniy.mypizzamegaapp.R;

public class MainPizza extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;

    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pizza_activity);

        tvLogin = findViewById(R.id.tvLogin);
        findViewById(R.id.btnLogOut).setOnClickListener(this);

        setUserInfo();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogOut:
                SharedPreferencesHelper.logout(context);
                finish();
                break;
        }
    }

    private void setUserInfo() {
        String token = SharedPreferencesHelper.getToken(this);
        if (token != null) {
            RequestHelper.apiGetUser(token, new RequestHelper.ApiInterface.onCompleteGetUser() {
                @Override
                public void onSuccess(User u) {
                    tvLogin.setText(u.login);
                }

                @Override
                public void onFail(String error) {
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    SharedPreferencesHelper.logout(context);
                    finish();
                }
            });
        } else {
            SharedPreferencesHelper.logout(context);
            finish();
        }
    }

}
