package com.iamretailer.krishnasupermarket.Adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.iamretailer.krishnasupermarket.POJO.CountryPO;
import com.iamretailer.krishnasupermarket.R;


import java.util.ArrayList;

public class StateAdapter extends ArrayAdapter<CountryPO> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final ArrayList<CountryPO> items;
    private final int mResource;

    public StateAdapter(Context context, int resource, ArrayList<CountryPO> objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent) {
        final View view = mInflater.inflate(mResource, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.txt);

        if (position==0)
        {
            textView.setTextColor(mContext.getResources().getColor(R.color.plceholder));
            textView.setPadding(0,0,0,0);
        }
        textView.setText(items.get(position).getCount_name());


        return view;
    }
}
