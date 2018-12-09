package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Adapters.PizzaStructureAdapter;
import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class PizzaStructure extends AppCompatActivity {

    private Button btnBakc;
    private TextView tvLogin;
    private ListView lvPizzaStruct;

    private PizzaStructureAdapter adapter;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_structure);
        Intent intent = getIntent();
        int pizzaId = intent.getIntExtra("pizzaId", 1);
        String login = SharedPreferencesHelper.getLogin(context, "");

        btnBakc = findViewById(R.id.btnBack);
        tvLogin = findViewById(R.id.tvLogin);
        lvPizzaStruct = findViewById(R.id.lvPizzaStruct);

        btnBakc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (login != null)
            tvLogin.setText(login);

        RequestHelper.apiGetPizzaStruct(SharedPreferencesHelper.getToken(this), pizzaId, new RequestHelper.ApiInterface.onCompleteGetPizzaStruct() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                lvPizzaStruct.setAdapter(new PizzaStructureAdapter(context, products));
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });

    }
}
