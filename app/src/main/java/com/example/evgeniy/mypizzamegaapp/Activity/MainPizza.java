package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Pizza;
import com.example.evgeniy.mypizzamegaapp.Models.User;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class MainPizza extends AppCompatActivity implements View.OnClickListener {

    private TextView tvLogin;
    private ListView lvPizzas;

    private ArrayList<Pizza> pizzas;

    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_pizza_activity);

        tvLogin = findViewById(R.id.tvLogin);
        findViewById(R.id.btnLogOut).setOnClickListener(this);

        RequestHelper.apiGetPizza(SharedPreferencesHelper.getToken(this), null, new RequestHelper.ApiInterface.onCompleteGetPizza() {
            @Override
            public void onSuccess(ArrayList<Pizza> p) {
                pizzas = p;
                lvPizzas.setAdapter(new MyArrayAdapter(context, pizzas));

            }

            @Override
            public void onFail(String error) {
                pizzas = new ArrayList<>();
                Toast.makeText(context, "Ошибка загрузки списка пицц\n" + error, Toast.LENGTH_LONG).show();
            }
        });
        lvPizzas = findViewById(R.id.lvPizzas);


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

    private class MyArrayAdapter extends ArrayAdapter {
        private ArrayList<Pizza> pizzas;
        private Context context;

        public MyArrayAdapter(@NonNull Context context, ArrayList<Pizza> pizzas) {
            super(context, R.layout.pizza_list_item);
            this.pizzas = pizzas;
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = null;
            if (inflater != null) {
                rowView = inflater.inflate(R.layout.pizza_list_item, parent, false);
            }
            if (rowView != null) {
                TextView tvPizzaName = rowView.findViewById(R.id.PizzaName);
                TextView tvPizzaCost = rowView.findViewById(R.id.PizzaCost);
                TextView tvPizzaId = rowView.findViewById(R.id.PizzaId);
                Pizza pizza = pizzas.get(position);
                tvPizzaCost.setText(String.valueOf(pizza.PizzaCost));
                tvPizzaName.setText(String.valueOf(pizza.PizzaName));
                tvPizzaId.setText(String.valueOf(pizza.PizzaId));
            } else {
                rowView = new View(context);
            }
            return rowView;
        }

    }

    ;
}
