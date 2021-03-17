package com.iamretailer.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Allen;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.Deal_list;
import com.iamretailer.Login;
import com.iamretailer.MainActivity;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.ProductFullView;
import com.iamretailer.Product_list;
import com.iamretailer.R;
import com.iamretailer.SearchActivity;
import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ProductsPO> items;
    Context context;
    DBController dbController;
    String cur_left = "";
    String cur_right = "";
    AndroidLogger logger;
    int from;
    int cart;
    public CommonAdapter(Context ctx, ArrayList<ProductsPO> imageModelArrayList, int from, int cart) {
        inflater = LayoutInflater.from(ctx);
        this.items = imageModelArrayList;
        this.context = ctx;
        this.from = from;
        dbController = new DBController(context);
        cur_left = dbController.get_cur_Left();
        cur_right = dbController.get_cur_Right();
        Appconstatants.CUR = dbController.getCurCode();
        this.cart=cart;
        logger = AndroidLogger.getLogger(context, Appconstatants.LOG_ID, false);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = inflater.inflate(R.layout.featured_item2, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        if (from==1)
        {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.items_bg.getLayoutParams();
            if(position==0){
                holder.font_view.setVisibility(View.VISIBLE);
                params.width=(int)context.getResources().getDimension(R.dimen.dp150);
                params.height=LinearLayout.LayoutParams.WRAP_CONTENT;
                holder.font_view.setVisibility(View.VISIBLE);
                params.setMargins((int) context.getResources().getDimension(R.dimen.dp5), 0, 0, (int) context.getResources().getDimension(R.dimen.dp5));
            }else{
                params.width=(int)context.getResources().getDimension(R.dimen.dp150);
                params.height=LinearLayout.LayoutParams.WRAP_CONTENT;
                params.setMargins((int) context.getResources().getDimension(R.dimen.dp10), 0, 0, (int) context.getResources().getDimension(R.dimen.dp5));
            }
            params.setMarginEnd((int) context.getResources().getDimension(R.dimen.dp10));

            holder.items_bg.setLayoutParams(params);
        }else if (from == 3) {

            if(position==0){
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) context.getResources().getDimension(R.dimen.dp10), RecyclerView.LayoutParams.MATCH_PARENT);
                holder.font_view.setVisibility(View.VISIBLE);
                holder.font_view.setLayoutParams(lp);

            }else{
                holder.font_view.setVisibility(View.GONE);
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.items_bg.getLayoutParams();
            params.width=(int)context.getResources().getDimension(R.dimen.dp150);
            params.height=LinearLayout.LayoutParams.WRAP_CONTENT;
            params.setMarginEnd((int) context.getResources().getDimension(R.dimen.dp10));
            holder.items_bg.setLayoutParams(params);
        }
        else
        {
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
            lp.setMarginStart((int) context.getResources().getDimension(R.dimen.dp13));
            lp.setMargins(0,(int) context.getResources().getDimension(R.dimen.dp10),0,0);
            holder.product.setLayoutParams(lp);
        }
        holder.product_name.setText(items.get(position).getProduct_name());

        if (items.get(position).getProd_offer_rate() > 0) {
            holder.or_cur_right.setText(cur_right);
            holder.or_cur_left.setText(cur_left);
            holder.sp_cur_right.setText(cur_right);
            holder.sp_cur_left.setText(cur_left);
            holder.p_price.setText(String.format("%.2f", items.get(position).getProd_offer_rate()));
            holder.orginal.setText(String.format("%.2f", items.get(position).getProd_original_rate()));
        } else {
            holder.sp_cur_right.setText(cur_right);
            holder.sp_cur_left.setText(cur_left);
            holder.or_cur_right.setText("");
            holder.or_cur_left.setText("");
            holder.p_price.setText(String.format("%.2f", items.get(position).getProd_original_rate()));
            holder.orginal.setText("");
        }

        if (items.get(position).getProducturl() != null && items.get(position).getProducturl().length() != 0) {
            Picasso.with(context).load(items.get(position).getProducturl()).placeholder(R.mipmap.place_holder).into(holder.p_img);
        } else{
            Picasso.with(context).load(R.mipmap.place_holder).placeholder(R.mipmap.place_holder).into(holder.p_img);

        }

        if(items.get(position).getWeight()!=null && !items.get(position).getWeight().equalsIgnoreCase("0.00kg")){
            Log.i("tag","nlfjslkfjlfdjwlk11 --- "+items.get(position).getWeight());
            holder.grms.setVisibility(View.VISIBLE);
        }else {
            Log.i("tag","nlfjslkfjlfdjwlk22 --- "+items.get(position).getWeight());
            holder.grms.setVisibility(View.GONE);
        }

        holder.grms.setText(items.get(position).getWeight()+"");
        holder.manufact.setText(items.get(position).getManufact()+"");
        if(items.get(position).getManufact()!=null && items.get(position).getManufact().length()>0 )
            holder.manufact.setVisibility(View.VISIBLE);
        else
            holder.manufact.setVisibility(View.GONE);

        if (items.get(position).isWish_list()) {
            holder.like.setVisibility(View.VISIBLE);
            holder.un_like.setVisibility(View.GONE);
        } else {
            holder.un_like.setVisibility(View.VISIBLE);
            holder.like.setVisibility(View.GONE);
        }


        holder.items_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductFullView.class);
                Log.i("tag", "product_id" + items.get(position).getProduct_id());
                Bundle nn = new Bundle();
                nn.putString("productid", items.get(position).getProduct_id());
                intent.putExtras(nn);
                context.startActivity(intent);
            }
        });
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbController.getLoginCount() > 0) {
                    if (holder.un_like.getVisibility() == View.VISIBLE) {
                        items.get(position).setWish_list(true);
                        notifyDataSetChanged();
                        WishlistAddTask task = new WishlistAddTask(position);
                        task.execute(items.get(position).getProduct_id());
                    } else {
                        items.get(position).setWish_list(false);
                        notifyDataSetChanged();
                        DeleteContactTask task = new DeleteContactTask(position);
                        task.execute(items.get(position).getProduct_id());
                    }
                } else {
                    Intent intent = new Intent(context, Login.class);
                    Bundle login = new Bundle();
                    login.putInt("from", 4);
                    intent.putExtras(login);
                    context.startActivity(intent);
                }
            }
        });

        if (items.get(position).getP_rate()==0) {
            holder.rating_tag.setVisibility(View.GONE);
        }
        else {
            holder.rating_text.setText(items.get(position).getP_rate() + "");
            holder.rating_tag.setVisibility(View.VISIBLE);
        }


        if (items.get(position).getP_rate() == 0) {
            holder.r1.setImageResource(R.mipmap.un_fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 1) {

            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.un_fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 2) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.un_fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 3) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.un_fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 4) {
            holder.r1.setImageResource(R.mipmap.fill);
            holder.r2.setImageResource(R.mipmap.fill);
            holder.r3.setImageResource(R.mipmap.fill);
            holder.r4.setImageResource(R.mipmap.fill);
            holder.r5.setImageResource(R.mipmap.un_fill);
        } else if (items.get(position).getP_rate() == 5) {
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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView p_price, orginal,product_name;
        ImageView like, un_like,p_img;
        LinearLayout items_bg,fav,background;
        ImageView r1, r2, r3, r4, r5;
        TextView sp_cur_left,sp_cur_right,or_cur_left,or_cur_right,grms,manufact;
        TextView rating_text;
        FrameLayout rating_tag;
        LinearLayout font_view,product;

        public MyViewHolder(View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.prod_name);
            p_img = itemView.findViewById(R.id.prod_img);
            p_price = itemView.findViewById(R.id.prod_price);
            like = itemView.findViewById(R.id.like);
            un_like = itemView.findViewById(R.id.unlike);
            fav = itemView.findViewById(R.id.fav);
            product = itemView.findViewById(R.id.product);
            items_bg = itemView.findViewById(R.id.items_bg);
            background= itemView.findViewById(R.id.background);
            orginal= itemView.findViewById(R.id.orginal);
            r1 = itemView.findViewById(R.id.r1);
            r2 = itemView.findViewById(R.id.r2);
            r3 = itemView.findViewById(R.id.r3);
            r4 = itemView.findViewById(R.id.r4);
            r5 = itemView.findViewById(R.id.r5);
            sp_cur_left= itemView.findViewById(R.id.sp_cur_left);
            sp_cur_right= itemView.findViewById(R.id.sp_cur_right);
            or_cur_left= itemView.findViewById(R.id.or_cur_left);
            or_cur_right= itemView.findViewById(R.id.or_cur_right);
            grms = itemView.findViewById(R.id.grms);
            manufact = itemView.findViewById(R.id.prod_name1);
            rating_text= itemView.findViewById(R.id.rating_text);
            rating_tag= itemView.findViewById(R.id.rating_tag);
            font_view=itemView.findViewById(R.id.font_view);
        }
    }

    private class WishlistAddTask extends AsyncTask<String, Void, String> {
        int pos;

        public WishlistAddTask(int position) {
            pos = position;
        }

        @Override
        protected void onPreExecute() {
        }

        protected String doInBackground(String... param) {
            logger.info("Wish list add api :" + Appconstatants.WishList_Add + param[0]);
            String response = null;
            Connection connection = new Connection();
            try {
                response = connection.sendHttpPostjson(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, context);
                logger.info("Wish list add api resp :" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {

            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        try {
                            items.get(pos).setWish_list(true);
                            notifyDataSetChanged();
                            Toast.makeText(context, R.string.wish_add, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        try {
                            items.get(pos).setWish_list(false);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                        }
                        Toast.makeText(context, array.getString(0) + "", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                    try {
                        items.get(pos).setWish_list(false);
                        notifyDataSetChanged();
                    } catch (Exception ex) {

                    }
                }
            } else {
                try {
                    items.get(pos).setWish_list(false);
                    notifyDataSetChanged();
                } catch (Exception ex) {
                }
                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class DeleteContactTask extends AsyncTask<String, Void, String> {
        int pos;

        public DeleteContactTask(int position) {
            pos = position;
        }

        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... param) {
            logger.info("Delete wish list api" + Appconstatants.WishList_Add + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.sendHttpDelete(Appconstatants.WishList_Add + param[0], null, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, context);
                logger.info("Delete wish list api resp" + response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String res) {
            Log.i("resd", res + "");
            if (res != null) {
                try {
                    JSONObject json = new JSONObject(res);
                    if (json.getInt("success") == 1) {
                        try {
                            items.get(pos).setWish_list(false);
                            notifyDataSetChanged();
                            Toast.makeText(context, R.string.dele_wish, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");
                        try {
                            items.get(pos).setWish_list(true);
                            notifyDataSetChanged();
                            Toast.makeText(context, R.string.dele_wish, Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {

                        }
                        Toast.makeText(context, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    try {
                        items.get(pos).setWish_list(true);
                        notifyDataSetChanged();
                        Toast.makeText(context, R.string.dele_wish, Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {

                    }
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                try {
                    items.get(pos).setWish_list(true);
                    notifyDataSetChanged();
                    Toast.makeText(context, R.string.dele_wish, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                }
                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CartSaveTask extends AsyncTask<Object, Void, String> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(context);
            pDialog.setMessage(context.getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("Cart", "started");
        }

        protected String doInBackground(Object... param) {
            logger.info("Cart Save api" + Appconstatants.cart_api);
            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("product_id", param[0]);
                json.put("quantity", "1");
                JSONObject object = new JSONObject();
                json.put("option", object);
                Log.d("Cart_input", json.toString());
                Log.d("Cart_url_insert", Appconstatants.sessiondata + "");
                logger.info("Cart Save req" + json);
                response = connection.sendHttpPostjson(Appconstatants.cart_api, json, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, context);
                logger.info("Cart Save resp" + response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("Cart", "Cart_resp  " + resp);
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Toast.makeText(context, context.getResources().getString(R.string.cart_Add_text), Toast.LENGTH_SHORT).show();
                        if (cart==1)
                            ((MainActivity) context).cart_inc();
                        else if (cart==2)
                            ((Product_list) context).cart_inc();
                        else if (cart==3)
                            ((SearchActivity) context).cart_inc();
                        else if (cart==4)
                            ((Allen) context).cart_inc();
                        else if (cart==5)
                            ((Deal_list) context).cart_inc();
                        else if (cart==6)
                            ((ProductFullView) context).cart_inc();


                    } else {
                        JSONArray error = json.getJSONArray("error");
                        String error_msg = error.getString(0);
                        Toast.makeText(context, error_msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }
}


