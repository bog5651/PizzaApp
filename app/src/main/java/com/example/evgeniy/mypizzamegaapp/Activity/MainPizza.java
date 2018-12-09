package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Adapters.PizzaListAdapter;
import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Pizza;
import com.example.evgeniy.mypizzamegaapp.Models.User;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

import static android.widget.AdapterView.OnItemClickListener;
import static com.example.evgeniy.mypizzamegaapp.Adapters.PizzaListAdapter.ItemInfo;

public class MainPizza extends AppCompatActivity {

    private TextView tvLogin;
    private ListView lvPizzas;
    private Button btnLogout;
    private Button btnAddPizza;

    private PizzaListAdapter pizzaAdapter;

    private ArrayList<Pizza> pizzas;

    private Context context = this;

    private static final String TAG = "MainPizza";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pizza_activity);

        tvLogin = findViewById(R.id.tvLogin);
        (btnLogout = findViewById(R.id.btnLogOut)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHelper.apiLogout(SharedPreferencesHelper.getToken(context), new RequestHelper.ApiInterface.onComplete() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "Logout onSuccess: ");
                        SharedPreferencesHelper.logout(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        intent.putExtra("login", SharedPreferencesHelper.getLogin(context, ""));
                        startActivity(intent);
                    }

                    @Override
                    public void onFail(String error) {
                        Log.d(TAG, "Logout onSuccess: ");
                        SharedPreferencesHelper.logout(context);
                        Intent intent = new Intent(context, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        intent.putExtra("login", SharedPreferencesHelper.getLogin(context, ""));
                        startActivity(intent);
                    }
                });
            }
        });

        btnAddPizza = findViewById(R.id.btnAddPizza);
        btnAddPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddPizzaActivity.class);
                startActivity(intent);
            }
        });

        RequestHelper.apiGetPizza(SharedPreferencesHelper.getToken(this), null, new RequestHelper.ApiInterface.onCompleteGetPizza() {
            @Override
            public void onSuccess(ArrayList<Pizza> p) {
                pizzas = p;
                pizzaAdapter = new PizzaListAdapter(context, pizzas);
                lvPizzas.setAdapter(pizzaAdapter);
            }

            @Override
            public void onFail(String error) {
                pizzas = new ArrayList<>();
                Toast.makeText(context, "Ошибка загрузки списка пицц\n" + error, Toast.LENGTH_LONG).show();
            }
        });
        lvPizzas = findViewById(R.id.lvPizzas);
        lvPizzas.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemInfo itemInfo = pizzaAdapter.getItemInfo(position);
                Intent intent = new Intent(context, PizzaStructure.class);
                intent.putExtra("pizzaId", itemInfo.pizza.PizzaId);
                startActivity(intent);
            }
        });

        setUserInfo();
    }

    private void setUserInfo() {
        String token = SharedPreferencesHelper.getToken(this);
        if (token != null) {
            RequestHelper.apiGetUser(token, new RequestHelper.ApiInterface.onCompleteGetUser() {
                @Override
                public void onSuccess(User u) {
                    SharedPreferencesHelper.putLogin(context, u.login);
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
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("login", SharedPreferencesHelper.getLogin(context, ""));
            startActivity(intent);
        }
    }
}
