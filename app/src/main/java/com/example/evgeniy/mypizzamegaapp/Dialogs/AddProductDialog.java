package com.example.evgeniy.mypizzamegaapp.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Adapters.Spinners.ProductSpinnerAdapter;
import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class AddProductDialog extends Dialog {

    private EditText etName;
    private EditText etCost;
    private EditText etUnit;
    private EditText etCount;

    private RelativeLayout rlMain;
    private RelativeLayout rlProductList;

    private Spinner spinner;

    private Button btnAdd;
    private Button btnBack;

    private boolean addInPizza;
    private Integer pizzaId;

    private ProductSpinnerAdapter adapter;

    private onClickListener onClickListener;

    private Context context;

    public AddProductDialog(@NonNull Context context, boolean addInPizza, Integer pizzaId) {
        super(context);
        this.context = context;
        this.addInPizza = addInPizza;
        this.pizzaId = pizzaId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dg_add_product);
        setTitle("Добавить пиццу");
        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        etName = findViewById(R.id.etName);
        etCost = findViewById(R.id.etCost);
        etUnit = findViewById(R.id.etUnit);
        etCount = findViewById(R.id.etCount);

        rlMain = findViewById(R.id.rlMain);
        rlProductList = findViewById(R.id.rlProductList);

        spinner = findViewById(R.id.spinner);

        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

        if (!addInPizza) {
            rlProductList.setVisibility(View.GONE);
            rlMain.setVisibility(View.VISIBLE);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = etName.getText().toString().trim();
                    String cost = etCost.getText().toString().trim();
                    String unit = etUnit.getText().toString().trim();

                    final Product product;
                    if (!cost.equals("") && !name.equals("") && !unit.equals("")) {
                        product = new Product(name, Integer.parseInt(cost), unit);
                    } else
                        return;

                    RequestHelper.apiAddProduct(SharedPreferencesHelper.getToken(context), product, new RequestHelper.ApiInterface.onCompleteWithResult() {
                        @Override
                        public void onSuccess(String result) {
                            Toast.makeText(context, "Продукт успешно добавлен", Toast.LENGTH_LONG).show();
                            if (onClickListener != null)
                                onClickListener.onClickAdd(product);
                        }

                        @Override
                        public void onFail(String error) {
                            Toast.makeText(context, "Продукт успешно НЕ добавлен\n" + error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else {
            rlMain.setVisibility(View.GONE);
            rlProductList.setVisibility(View.VISIBLE);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Product product = (Product) adapter.getItem(spinner.getSelectedItemPosition());
                    product.count = Integer.parseInt(etCount.getText().toString().trim());
                    RequestHelper.apiAddProductInPizza(SharedPreferencesHelper.getToken(context), pizzaId, product.id, product.count, new RequestHelper.ApiInterface.onComplete() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(context, "Успешно добавлено", Toast.LENGTH_LONG).show();
                            onClickListener.onClickAdd(product);
                            dismiss();
                        }

                        @Override
                        public void onFail(String error) {
                            Toast.makeText(context, "Успешно не добавлено \n " + error, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }

        RequestHelper.apiGetProduct(SharedPreferencesHelper.getToken(context), null, new RequestHelper.ApiInterface.onCompleteGetProductList() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                adapter = new ProductSpinnerAdapter(context, products);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                dismiss();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null)
                    onClickListener.onClickBack();

            }
        });

    }

    public void setOnClickListener(AddProductDialog.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClickAdd(Product product);

        void onClickBack();
    }
}
