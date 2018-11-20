package com.example.evgeniy.mypizzamegaapp.Activity;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RequestHelper.ApiInterface.onComplete {

    private EditText etLogin;
    private EditText etPassword;

    private Button btnLogin;
    private String TAG = "LoginAct";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        RequestHelper.apiLogin(etLogin.getText().toString(), etPassword.getText().toString(), this);
    }

    @Override
    public void onSuccess(String result) {
        SharedPreferencesHelper.putToken(this, result);
        Intent toAct = new Intent(this, MainPizza.class);
        startActivity(toAct);
    }

    @Override
    public void onFail(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }
}
