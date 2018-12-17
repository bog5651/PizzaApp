package com.example.evgeniy.mypizzamegaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class PizzaStructureAdapter extends ArrayAdapter {

    private ArrayList<Product> products;

    private Context context;
    public boolean isEdit;

    private onItemRemove onItemRemove;

    public PizzaStructureAdapter(@NonNull Context context, ArrayList<Product> products, onItemRemove onItemRemove) {
        super(context, R.layout.pizza_structute_item, products);
        this.products = products;
        this.context = context;
        this.onItemRemove = onItemRemove;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.pizza_structute_item, parent, false);
        }
        if (rowView != null) {
            TextView tvProductName = rowView.findViewById(R.id.ProductName);
            TextView tvProductCount = rowView.findViewById(R.id.ProductCount);
            TextView tvProductUnit = rowView.findViewById(R.id.ProductUnit);
            ImageView iv = rowView.findViewById(R.id.ivDelete);
            iv.setTag(position);
            iv.setOnClickListener(listener);
            if(isEdit)
                iv.setVisibility(View.VISIBLE);
            else
                iv.setVisibility(View.GONE);
            Product product = products.get(position);
            tvProductName.setText(String.valueOf(product.name));
            tvProductCount.setText(String.valueOf(product.count));
            tvProductUnit.setText(String.valueOf(product.unit));
        } else {
            rowView = new View(context);
        }
        return rowView;
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (onItemRemove != null)
                onItemRemove.onDelete((int) v.getTag());
        }
    };

    public interface onItemRemove {
        void onDelete(int productId);
    }
}
