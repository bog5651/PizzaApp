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
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

public class AppProductDialog extends Dialog {

    private EditText etName;
    private EditText etCost;
    private EditText etUnit;

    private Button btnAdd;
    private Button btnBack;

    private onClickListener onClickListener;

    private Context context;

    public AppProductDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dg_add_product);
        setTitle("Добавить пиццу");
        Window window = getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        etName = findViewById(R.id.etName);
        etCost = findViewById(R.id.etCost);
        etUnit = findViewById(R.id.etUnit);

        btnAdd = findViewById(R.id.btnAdd);
        btnBack = findViewById(R.id.btnBack);

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
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (onClickListener != null)
                    onClickListener.onClickBack();

            }
        });

    }

    public void setOnClickListener(AppProductDialog.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface onClickListener {
        void onClickAdd(Product product);

        void onClickBack();
    }
}
