package com.iamretailer.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.POJO.SingleOptionPO;
import com.iamretailer.R;


public class ReviewAdapter extends ArrayAdapter<SingleOptionPO> {
    Context context;
    ArrayList<SingleOptionPO> items;
    int res;
    LayoutInflater mInflater;


    public ReviewAdapter(Context context, int resource, ArrayList<SingleOptionPO> items) {
        super(context, resource, items);
        this.context = context;
        this.res = resource;
        this.items = items;
        mInflater = LayoutInflater.from(context);
    }

    public int getViewTypeCount() {

        return getCount();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        FrameLayout alertView = null;
        holder = new ViewHolder();

        if (convertView == null) {
            convertView = mInflater.inflate(res, alertView, true);
            convertView.setTag(holder);
            alertView = (FrameLayout) convertView;
        } else {
            alertView = (FrameLayout) convertView;
        }
        holder.comts = (TextView) convertView.findViewById(R.id.comments);
        holder.rev_date = (TextView) convertView.findViewById(R.id.date);
        holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
        holder.user_first = (TextView) convertView.findViewById(R.id.user_first);
        holder.review_bg = (LinearLayout) convertView.findViewById(R.id.review_bg);
        holder.r1 = (ImageView) convertView.findViewById(R.id.r1);
        holder.r2 = (ImageView) convertView.findViewById(R.id.r2);
        holder.r3 = (ImageView) convertView.findViewById(R.id.r3);
        holder.r4 = (ImageView) convertView.findViewById(R.id.r4);
        holder.r5 = (ImageView) convertView.findViewById(R.id.r5);


        holder.comts.setText(items.get(position).getRev_text());
        holder.rev_date.setText(CommonFunctions.date_format(items.get(position).getRev_date()));
        holder.user_name.setText(items.get(position).getRev_author());
        holder.user_first.setText(String.valueOf(items.get(position).getRev_author().charAt(0)).toUpperCase());


        if (items.get(position).getRating() == 0) {
            holder.r1.setImageResource(R.mipmap.un_fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getRating() == 1) {

            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getRating() == 2) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getRating() == 3) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getRating() == 4) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getRating() == 5) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.fill);
            holder.r5.setImageResource(R.mipmap.fill);
        } else {
            holder.r1.setImageResource(R.mipmap.un_fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        }

        if (position % 2 == 1)
            holder.review_bg.setBackground(getContext().getResources().getDrawable(R.drawable.rev_bg));
        else
            holder.review_bg.setBackground(getContext().getResources().getDrawable(R.drawable.rev_bg));


        return alertView;
    }

    private static class ViewHolder {
        public TextView comts, user_name;
        TextView rev_date;
        ImageView r1, r2, r3, r4, r5;
        TextView user_first;
        LinearLayout review_bg;

    }
}