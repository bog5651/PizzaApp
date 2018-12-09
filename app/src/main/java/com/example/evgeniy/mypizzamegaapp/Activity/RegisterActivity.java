package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etLogin;
    private EditText etPassword;
    private EditText etFirstName;
    private EditText etSecondName;
    private Button btnRegister;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        Intent intent = getIntent();
        String login = intent.getStringExtra("login");
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        etFirstName = findViewById(R.id.etFirstName);
        etSecondName = findViewById(R.id.etSecondName);

        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHelper.apiRegister(etLogin.getText().toString().trim(),
                        etPassword.getText().toString().trim(),
                        etFirstName.getText().toString().trim(),
                        etSecondName.getText().toString().trim(),
                        new RequestHelper.ApiInterface.onComplete() {
                            @Override
                            public void onSuccess(String result) {
                                SharedPreferencesHelper.putToken(context, result);

                                Intent intent = new Intent(context, MainPizza.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFail(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        if (login != null) {
            etLogin.setText(login.trim());
        }
    }
}
