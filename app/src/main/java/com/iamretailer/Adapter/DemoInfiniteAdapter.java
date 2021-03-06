package com.iamretailer.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.LoopingPagerAdapter;
import com.iamretailer.POJO.BannerBo;
import com.iamretailer.Product_list;
import com.iamretailer.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DemoInfiniteAdapter extends LoopingPagerAdapter<BannerBo> {

    private final Context context;
    private final ArrayList<BannerBo> itemList;

    public DemoInfiniteAdapter(Context context, ArrayList<BannerBo> itemList, boolean isInfinite) {
        super(context, itemList, isInfinite);
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    protected View getItemView(View convertView, final int listPosition, ViewPager container) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.image_view, null);
            container.addView(convertView);
        }
        ImageView imageView = convertView.findViewById(R.id.browsebackground);
        LinearLayout banner = convertView.findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Appconstatants.need_brand_product == 1) {
                    Intent intent = new Intent(context, Product_list.class);
                    Bundle best = new Bundle();
                    best.putString("view_all", "banners");
                    best.putString("head", "");
                    best.putString("banner_id", itemList.get(listPosition).getBanner_id());
                    best.putString("title", itemList.get(listPosition).getLink());
                    intent.putExtras(best);
                    context.startActivity(intent);
                }
            }
        });

        if (itemList.get(listPosition).getImage() != null && itemList.get(listPosition).getImage().length() > 0)
            Picasso.with(context).load(itemList.get(listPosition).getImage()).placeholder(R.mipmap.place_holder).into(imageView);
        else
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(imageView);

        return convertView;
    }

}
