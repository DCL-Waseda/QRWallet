package com.example.gushimakota.qrwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import android.widget.TextView;

/**
 * Created by gushimakota on 16/06/10.
 */
public class MyListAdapter extends ArrayAdapter<ItemDataForMyList>{
    private LayoutInflater layoutInflater_;

    public MyListAdapter(Context context, int textViewResourceId, List<ItemDataForMyList> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        ItemDataForMyList item = (ItemDataForMyList) getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.customed_list, null);
        }

        // CustomDataのデータをViewの各Widgetにセットする
        TextView itemTextView;
        itemTextView = (TextView)convertView.findViewById(R.id.item);
        itemTextView.setText(item.getItemName());

        TextView priceTextView;
        priceTextView = (TextView)convertView.findViewById(R.id.price);
        priceTextView.setText(item.getItemPrice()+"P");

        return convertView;
    }

}
