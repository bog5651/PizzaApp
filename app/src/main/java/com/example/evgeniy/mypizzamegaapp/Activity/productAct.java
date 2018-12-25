package com.example.evgeniy.mypizzamegaapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.evgeniy.mypizzamegaapp.Adapters.ProductListAdapter;
import com.example.evgeniy.mypizzamegaapp.Dialogs.AddProductDialog;
import com.example.evgeniy.mypizzamegaapp.Helpers.RequestHelper;
import com.example.evgeniy.mypizzamegaapp.Helpers.SharedPreferencesHelper;
import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class productAct extends AppCompatActivity {
    private static final String TAG = "productAct";

    private ListView lvList;

    private Switch aSwitch;
    private Button btnAdd;

    private ProductListAdapter adapter;

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_product_list);
        context = this;
        lvList = findViewById(R.id.lvList);

        aSwitch = findViewById(R.id.swEdit);
        btnAdd = findViewById(R.id.btnAdd);

        RequestHelper.apiGetProduct(SharedPreferencesHelper.getToken(this), null, new RequestHelper.ApiInterface.onCompleteGetProductList() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                adapter = new ProductListAdapter(context, products, true);
                adapter.setCallBackDel(new ProductListAdapter.onItemRemove() {
                    @Override
                    public void onDelete(final int position) {
                        Product product = adapter.getProduct(position);
                        RequestHelper.apiRemoveProduct(SharedPreferencesHelper.getToken(context), product.id, new RequestHelper.ApiInterface.onComplete() {
                            @Override
                            public void onSuccess() {
                                adapter.remove(position);
                                adapter.notifyDataSetChanged();
                                adapter.notifyDataSetInvalidated();
                            }

                            @Override
                            public void onFail(String error) {
                                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
                lvList.setAdapter(adapter);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(context, error, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onSuccess: " + error);
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.isEdit = isChecked;
                if (isChecked)
                    btnAdd.setVisibility(View.VISIBLE);
                else
                    btnAdd.setVisibility(View.GONE);
                adapter.notifyDataSetInvalidated();
                adapter.notifyDataSetChanged();
            }
        });

        btnAdd.setVisibility(View.GONE);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AddProductDialog dialog = new AddProductDialog(context, false, null);
                dialog.setOnClickListener(new AddProductDialog.onClickListener() {
                    @Override
                    public void onClickAdd(Product product) {
                        adapter.add(product);
                        adapter.notifyDataSetInvalidated();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickBack() {

                    }
                });
                dialog.show();
            }
        });
    }

}
