package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Adapters.PizzaStructureAdapter;
import com.example.evgeniy.mypizzamegaapp.Dialogs.AddProductDialog;
import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class PizzaStructure extends AppCompatActivity {

    private Button btnBakc;
    private Button btnAdd;

    private Switch aSwitch;

    private TextView tvLogin;
    private ListView lvPizzaStruct;

    private PizzaStructureAdapter adapter;

    private Context context = this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pizza_structure);
        Intent intent = getIntent();
        final int pizzaId = intent.getIntExtra("pizzaId", 1);
        String login = SharedPreferencesHelper.getLogin(context, "");

        btnBakc = findViewById(R.id.btnBack);
        btnAdd = findViewById(R.id.btnAdd);

        aSwitch = findViewById(R.id.swEdit);

        tvLogin = findViewById(R.id.tvLogin);
        lvPizzaStruct = findViewById(R.id.lvPizzaStruct);

        btnBakc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddProductDialog dialog = new AddProductDialog(context, true, pizzaId);
                dialog.show();
                dialog.setOnClickListener(new AddProductDialog.onClickListener() {
                    @Override
                    public void onClickAdd(Product product) {
                        adapter.add(product);
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickBack() {

                    }
                });
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    btnAdd.setVisibility(View.VISIBLE);
                    adapter.isEdit = true;
                    adapter.notifyDataSetInvalidated();
                } else {
                    btnAdd.setVisibility(View.GONE);
                    adapter.isEdit = false;
                    adapter.notifyDataSetInvalidated();
                }
            }
        });

        if (login != null)
            tvLogin.setText(login);

        RequestHelper.apiGetPizzaStruct(SharedPreferencesHelper.getToken(this), pizzaId, new RequestHelper.ApiInterface.onCompleteGetProductList() {
            @Override
            public void onSuccess(final ArrayList<Product> products) {
                adapter = new PizzaStructureAdapter(context, products, new PizzaStructureAdapter.onItemRemove() {
                    @Override
                    public void onDelete(final int productId) {
                        RequestHelper.apiRemoveProductFromPizza(SharedPreferencesHelper.getToken(context), pizzaId, productId, new RequestHelper.ApiInterface.onComplete() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(context, "Продукт успешно удален 1", Toast.LENGTH_LONG).show();
                                adapter.removeProductByPos(productId);
                                adapter.notifyDataSetInvalidated();
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onFail(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                lvPizzaStruct.setAdapter(adapter);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
            }
        });

    }
}
