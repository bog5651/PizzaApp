package com.example.evgeniy.mypizzamegaapp.Adapters.Spinners;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class ProductSpinnerAdapter extends ArrayAdapter {

    private ArrayList<Product> products;

    private Context context;

    public ProductSpinnerAdapter(@NonNull Context context, ArrayList<Product> products) {
        super(context, android.R.layout.simple_spinner_item, products);
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int pos, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.spinner_item, parent, false);
        }
        Product itemInfo = (Product) getItem(pos);
        if (rowView != null) {
            TextView tvName = rowView.findViewById(R.id.tvName);
            tvName.setText(String.valueOf(itemInfo.name));
        } else {
            rowView = new View(context);
        }
        return rowView;
    }


    @Nullable
    @Override
    public Object getItem(int position) {
        return products.get(position);
    }
}
