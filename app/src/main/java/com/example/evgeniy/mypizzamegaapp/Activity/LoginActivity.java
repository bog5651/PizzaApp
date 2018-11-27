package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etPassword;

    private Button btnLogin;
    private Button btnRegister;
    private String TAG = "LoginAct";

    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHelper.apiLogin(etLogin.getText().toString(), etPassword.getText().toString(), new RequestHelper.ApiInterface.onComplete() {
                    @Override
                    public void onSuccess(String result) {
                        SharedPreferencesHelper.putToken(context, result);
                        Intent toAct = new Intent(context, MainPizza.class);
                        startActivity(toAct);
                    }

                    @Override
                    public void onFail(String error) {
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAct = new Intent(context, RegisterActivity.class);
                //TODO передавать логин и пароль если они уже введены
                startActivity(toAct);
            }
        });
    }


}
