package com.example.evgeniy.mypizzamegaapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.example.evgeniy.mypizzamegaapp.Models.Product;
import com.example.evgeniy.mypizzamegaapp.R;

import java.util.ArrayList;

public class ProductListAdapter extends ArrayAdapter implements View.OnClickListener {

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
            swSelect.setOnClickListener(this);
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
    public void onClick(View v) {
        Switch aSwitch = (Switch) v;

        ItemInfo itemInfo = (ItemInfo) getItem((int) aSwitch.getTag());
        if (itemInfo.isSelected != aSwitch.isChecked()) {
            itemInfo.isSelected = aSwitch.isChecked();
        }
        if (callBack != null) {
            callBack.onSwitchChanged(itemInfo.product, aSwitch.isChecked());
        }

    }

    public ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> selected = new ArrayList<>();
        for (ItemInfo ii : itemInfos) {
            if (ii.isSelected) {
                selected.add(ii.product);
            }
        }
        return selected;
    }

    @Override
    public void add(@Nullable Object object) {
        super.add(object);
        Product product = (Product) object;
        ItemInfo ii = new ItemInfo();
        ii.product = product;
        ii.isSelected = false;
        itemInfos.add(ii);
    }

    @Override
    public Object getItem(int position) {
        return itemInfos.get(position);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return itemInfos.size();
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return itemInfos.get(position).product.id;
    }

    public interface onItemChecked {
        void onSwitchChanged(Product product, boolean isSelected);
    }
}
