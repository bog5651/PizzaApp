package com.example.evgeniy.mypizzamegaapp.Adapters.Spinners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;

import com.example.evgeniy.mypizzamegaapp.Models.Product;

import java.util.ArrayList;

public class ProductSpinnerAdapter extends ArrayAdapter {

    private ArrayList<Product> products;

    private Context context;

    public ProductSpinnerAdapter(@NonNull Context context, ArrayList<Product> products) {
        super(context, android.R.layout.simple_spinner_item, products);
        this.products = products;
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return products.get(position);
    }
}
