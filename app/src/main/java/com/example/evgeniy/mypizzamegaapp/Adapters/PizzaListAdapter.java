package com.example.evgeniy.mypizzamegaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.evgeniy.mypizzamegaapp.Models.Pizza;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class PizzaListAdapter extends ArrayAdapter {
    private ArrayList<Pizza> pizzas;
    private ArrayList<ItemInfo> itemInfos;
    private Context context;

    public static class ItemInfo {
        public View view;
        public Pizza pizza;

        public ItemInfo(View view, Pizza pizza) {
            this.view = view;
            this.pizza = pizza;
        }
    }

    public PizzaListAdapter(@NonNull Context context, ArrayList<Pizza> pizzas) {
        super(context, R.layout.pizza_list_item, pizzas);
        this.pizzas = pizzas;
        this.context = context;
        itemInfos = new ArrayList<>();
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
        itemInfos.add(new ItemInfo(rowView, pizzas.get(position)));
        return rowView;
    }

    public ItemInfo getItemInfo(int position) {
        return itemInfos.get(position);
    }


}