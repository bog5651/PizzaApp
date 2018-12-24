package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Adapters.ProductListAdapter;
import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class AddPizzaActivity extends AppCompatActivity {

    private Button btnAddPizza;
    private ListView lvProducts;
    private TextView tvCost;
    private EditText etPizzaName;
    private int Cost = 1000;

    private ProductListAdapter productListAdapter;

    private Context context = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_pizza_activity);
        btnAddPizza = findViewById(R.id.btnAddPizza);
        etPizzaName = findViewById(R.id.etPizzaName);
        lvProducts = findViewById(R.id.lvProduct);
        tvCost = findViewById(R.id.tvCost);
        tvCost.setText(String.valueOf(Cost).trim());

        btnAddPizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestHelper.apiAddPizza(SharedPreferencesHelper.getToken(context), productListAdapter.getSelectedProducts(), etPizzaName.getText().toString().trim(), String.valueOf(Cost), new RequestHelper.ApiInterface.onCompleteWithResult() {
                    @Override
                    public void onSuccess(String result) {
                        finish();
                    }

                    @Override
                    public void onFail(String error) {
                        Toast.makeText(context, "Ошибка добавления пиццы " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        RequestHelper.apiGetProduct(SharedPreferencesHelper.getToken(context), null, new RequestHelper.ApiInterface.onCompleteGetProductList() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                productListAdapter = new ProductListAdapter(context, products, false);
                productListAdapter.setOnItemChecked(new ProductListAdapter.onItemChecked() {
                    @Override
                    public void onSwitchChanged(Product product, boolean isSelected) {
                        if (isSelected) {
                            Cost += product.cost;
                        } else {
                            Cost -= product.cost;
                        }
                        tvCost.setText(String.valueOf(Cost).trim() + "Руб");
                    }
                });
                lvProducts.setAdapter(productListAdapter);
            }

            @Override
            public void onFail(String error) {

            }
        });


    }
}
