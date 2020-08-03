package com.iamretailer.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.MyCart;
import com.iamretailer.POJO.ProductsPO;
import com.iamretailer.R;

import com.logentries.android.AndroidLogger;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class CartAdapter extends ArrayAdapter<ProductsPO> {

    Context context;
    ArrayList<ProductsPO> items;
    int res;
    LayoutInflater mInflater;
    double single_value;
    DBController db;
    MyCart myCart;
    String cur_left = "";
    String cur_right = "";
    AndroidLogger logger;
    ArrayList<String> qty_list;
    SpinnerAdapter1 spinnerAdapter1;
    AlertDialog alertReviewDialog;
    LayoutInflater inflater;
    public CartAdapter(Activity context, int resource, ArrayList<ProductsPO> items) {
        super(context, resource, items);
        mInflater = LayoutInflater.from(context);
        this.res = resource;
        this.context = context;
        this.items = items;
        single_value = 0;
        myCart = new MyCart();
        db = new DBController(getContext());
        cur_left = db.get_cur_Left();
        cur_right=db.get_cur_Right();
        Appconstatants.CUR=db.getCurCode();
        logger=AndroidLogger.getLogger(context,Appconstatants.LOG_ID,false);
        inflater =context.getLayoutInflater();


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
        final ViewHolder holder;
        LinearLayout alertView = null;
        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(res, alertView, true);
            convertView.setTag(holder);
            alertView = (LinearLayout) convertView;
        } else {
            alertView = (LinearLayout) convertView;
        }
        holder.cart_img = (ImageView) convertView.findViewById(R.id.cart_img);
        holder.cart_prod_name = (TextView) convertView.findViewById(R.id.cart_prod_name);
        holder.cart_prod_or_rate = (TextView) convertView.findViewById(R.id.tot);
        holder.cart_ins = (ImageView) convertView.findViewById(R.id.add);
        holder.cart_dec = (ImageView) convertView.findViewById(R.id.subtract);
        holder.cart_value = (TextView) convertView.findViewById(R.id.cart_value);
        holder.remove = (ImageView) convertView.findViewById(R.id.remove);
        holder.option_list = (TextView) convertView.findViewById(R.id.option_list);
        holder.out_of_stock=(TextView)convertView.findViewById(R.id.out_of_stock);
        holder.qty=(LinearLayout)convertView.findViewById(R.id.qty);
        holder.option=(LinearLayout)convertView.findViewById(R.id.option);
        holder.rupee_front=(TextView)convertView.findViewById(R.id.rupee_front);
        holder.rupee_back=(TextView)convertView.findViewById(R.id.rupee_back);
        holder.spin_qty=(Spinner)convertView.findViewById(R.id.spin_qty);
        holder.qty_count=(TextView)convertView.findViewById(R.id.qty_count);

        qty_list=new ArrayList<>();
        for (int i=1;i<=4;i++)
        {
            if (i!=4)
                qty_list.add("0"+i);
            else
                qty_list.add("More");
        }

        spinnerAdapter1=new SpinnerAdapter1(context,qty_list);
        holder.spin_qty.setAdapter(spinnerAdapter1);

        if (items.get(position).getCartvalue()<=3) {
            holder.spin_qty.setSelection(items.get(position).getCartvalue() - 1);
            holder.qty_count.setVisibility(View.GONE);
        }else
        {
            holder.spin_qty.setVisibility(View.GONE);
            holder.qty_count.setText(items.get(position).getCartvalue()+"");
        }

        if (items.get(position).getProducturl().isEmpty())
            Picasso.with(getContext()).load(R.mipmap.place_holder).into(holder.cart_img);
        else
            Picasso.with(getContext()).load(items.get(position).getProducturl()).into(holder.cart_img);

        if (items.get(position).isOut_of_stock()) {
            holder.out_of_stock.setVisibility(View.GONE);
            holder.qty.setVisibility(View.VISIBLE);
        }
        else {
            holder.out_of_stock.setVisibility(View.VISIBLE);
            holder.qty.setVisibility(View.GONE);
        }


        holder.cart_prod_name.setText(items.get(position).getProduct_name());
        holder.rupee_back.setText(cur_right);
        holder.rupee_front.setText(cur_left);
        holder.cart_prod_or_rate.setText(String.format("%.2f", items.get(position).getTotal()));
        String op = "";
        for (int j = 0; j < items.get(position).getOptionlist().size(); j++) {
            if (items.get(position).getOptionlist().size() - 1 == j)
                op = op + items.get(position).getOptionlist().get(j).getName() + ": " + items.get(position).getOptionlist().get(j).getValue();
            else
                op = op + items.get(position).getOptionlist().get(j).getName() + ": " + items.get(position).getOptionlist().get(j).getValue() + ", ";

        }

        StringBuilder sb2 = new StringBuilder();

        for (int h = 0; h < items.get(position).getOptionlist().size(); h++) {
            Log.d("option_list", items.get(position).getOptionlist().get(h).getValue() + "");
            sb2.append(items.get(position).getOptionlist().get(h).getValue());
            if (h != items.get(position).getOptionlist().size() - 1)
                sb2.append(",");
        }
        Log.d("option_list", String.valueOf(sb2));

        if (sb2.length()>0)
        {
            holder.option.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.option.setVisibility(View.GONE);
        }
        holder.option_list.setText(String.valueOf(sb2));
        holder.cart_value.setText(String.valueOf(items.get(position).getCartvalue()));
        holder.cart_ins.setTag(position);

        single_value = Double.parseDouble(String.valueOf(items.get(position).getProd_offer_rate()));

        holder.cart_ins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    CartUpdate cartUpdate = new CartUpdate(position);
                    cartUpdate.execute(items.get(position).getKey(), String.valueOf(items.get(position).getCartvalue() + 1));


            }
        });
        holder.cart_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    CartUpdate cartUpdate = new CartUpdate(position);
                    cartUpdate.execute(items.get(position).getKey(), String.valueOf(items.get(position).getCartvalue() - 1));

            }
        });



        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cartdel cartdel = new Cartdel(position);
                cartdel.execute(items.get(position).getKey(), 0 + "");
            }
        });





        holder.spin_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                Log.d("asdad","adfdadsd");
                if (i!=3) {

                    if (items.get(position).getCartvalue() - 1 != i) {
                        CartUpdate cartUpdate = new CartUpdate(position);
                        cartUpdate.execute(items.get(position).getKey(), qty_list.get(i));
                    }
                }
                else
                {
               //     quantity_change(position);
                    holder.spin_qty.setSelection(items.get(position).getCartvalue()-1);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        holder.qty_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // quantity_change(position);
            }
        });

        holder.cart_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity_change(position,items.get(position).getCartvalue()+"");

            }
        });


        return alertView;
    }

    private static class ViewHolder {
        public ImageView cart_img, cart_ins, cart_dec;
        public TextView cart_prod_name, cart_prod_or_rate,cart_value;
        TextView option_list;
        ImageView remove;
        TextView out_of_stock,rupee_front,rupee_back;
        LinearLayout qty,option;
        Spinner spin_qty;
        TextView qty_count;
    }


    public void quantity_change(final int pos,String qtys)
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context,R.style.CustomAlertDialog);
        View dialogView = inflater.inflate(R.layout.show_qty, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.create();
        alertReviewDialog=dialogBuilder.create();
        alertReviewDialog.setCancelable(false);
        LinearLayout cancel=(LinearLayout)dialogView.findViewById(R.id.cancel);
        final EditText qty=(EditText)dialogView.findViewById(R.id.qty);
        LinearLayout apply=(LinearLayout)dialogView.findViewById(R.id.apply);
        qty.setText(qtys);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertReviewDialog.dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qty.getText().length()>0 && Integer.parseInt(qty.getText().toString())>0) {
                    alertReviewDialog.dismiss();
                    CartUpdate cartUpdate = new CartUpdate(pos);
                    cartUpdate.execute(items.get(pos).getKey(), qty.getText().toString());
                }
                else
                {
                    qty.setError(context.getResources().getString(R.string.qty_e));
                }
            }
        });
        alertReviewDialog.show();


    }


    private class CartUpdate extends AsyncTask<String, Void, String> {
        private final int pos;
        private ProgressDialog pDialog;
        private String qty;


        public CartUpdate(int position) {
            pos = position;
        }

        @Override
        protected void onPreExecute() {

            Log.d("Login", "started");



            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getContext().getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {
            logger.info("Update cart api"+Appconstatants.cart_update_api);
            String response = null;
            qty = param[1];
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("key", param[0]);
                json.put("quantity", param[1]);


                Log.d("cart", json.toString());
                Log.d("session", Appconstatants.sessiondata);
                logger.info("Update cart api req"+json);
                response = connection.sendHttpPutjson1(Appconstatants.cart_update_api, json, Appconstatants.sessiondata,Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,getContext());
                logger.info("Update cart api res"+response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("cart", "Cart-->" + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        items.get(pos).setCartvalue(Integer.parseInt(qty));
                        notifyDataSetChanged();
                        ((MyCart) getContext()).CartCall();
                        Toast.makeText(getContext(),getContext().getResources().getString(R.string.items_update), Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray array=json.getJSONArray("error");
                        Toast.makeText(getContext(),array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class Cartdel extends AsyncTask<String, Void, String> {

        private int pos1;
        private ProgressDialog pDialog;
        private String qty;


        public Cartdel(int position) {
            pos1 = position;
        }


        @Override
        protected void onPreExecute() {

            Log.d("delete", "started");

            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage(getContext().getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... param) {

            logger.info("Delete cart api"+Appconstatants.cart_update_api);
            qty = param[1];

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("key", param[0]);
                json.put("key", param[0]);
                json.put("quantity", param[1]);


                Log.d("del_", json.toString());
                Log.d("del_session", Appconstatants.sessiondata);
                logger.info("Delete cart api"+json);
                response = connection.sendHttpPutjson1(Appconstatants.cart_update_api, json, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang, Appconstatants.CUR,getContext());
                logger.info("Delete cart api"+response);
                Log.d("del_s", Appconstatants.cart_update_api);
                Log.d("del_r", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            pDialog.dismiss();
            Log.i("cart", "Cart-->" + resp);
            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                         items.remove(pos1);
                        notifyDataSetChanged();
                        ((MyCart) getContext()).CartCall();
                        Toast.makeText(getContext(), R.string.remove, Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray array=json.getJSONArray("error");
                        Toast.makeText(getContext(),array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }

            } else {

                Toast.makeText(getContext(), R.string.error_net, Toast.LENGTH_SHORT).show();
            }
        }
    }
}