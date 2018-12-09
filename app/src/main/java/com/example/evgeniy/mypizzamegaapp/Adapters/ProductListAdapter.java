package com.example.evgeniy.mypizzamegaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<ItemInfo> itemInfos;
    private ArrayList<Product> products;

    private Context context;

    private onItemChecked callBack;

    public class ItemInfo {
        public Product product;
        public boolean isSelected;
    }

    public ProductListAdapter(@NonNull Context context, ArrayList<Product> products) {
        super(context, R.layout.product_list_item, products);
        this.products = products;
        this.context = context;
        itemInfos = new ArrayList<>();
        for (Product p : products) {
            ItemInfo itemInfo = new ItemInfo();
            itemInfo.product = p;
            itemInfo.isSelected = false;
            itemInfos.add(itemInfo);
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;
        if (inflater != null) {
            rowView = inflater.inflate(R.layout.product_list_item, parent, false);
        }
        ItemInfo itemInfo = (ItemInfo) getItem(position);
        if (rowView != null) {
            TextView tvProductName = rowView.findViewById(R.id.ProductName);
            TextView tvProductCount = rowView.findViewById(R.id.ProductCost);
            Switch swSelect = rowView.findViewById(R.id.swSelect);
            tvProductName.setText(String.valueOf(itemInfo.product.name));
            tvProductCount.setText(String.valueOf(itemInfo.product.cost));
            swSelect.setOnCheckedChangeListener(this);
            swSelect.setTag(position);
            swSelect.setChecked(itemInfo.isSelected);
        } else {
            rowView = new View(context);
        }
        return rowView;
    }

    public void setOnItemChecked(onItemChecked callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (callBack != null) {
            ItemInfo itemInfo = (ItemInfo) getItem((int) buttonView.getTag());
            if (itemInfo.isSelected != isChecked) {
                itemInfo.isSelected = isChecked;
                Product checkedProduct = itemInfo.product;
                callBack.onSwitchChanged(checkedProduct, isChecked);
            }
        }
    }


        @Override
        public Object getItem ( int position){
            return itemInfos.get(position);
        }

        // кол-во элементов
        @Override
        public int getCount () {
            return itemInfos.size();
        }

        // id по позиции
        @Override
        public long getItemId ( int position){
            return itemInfos.get(position).product.id;
        }

        public interface onItemChecked {
            void onSwitchChanged(Product product, boolean isSelected);
        }
    }
