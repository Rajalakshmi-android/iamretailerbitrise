package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.OrderAdapter;
import com.iamretailer.Adapter.ReturnlistAdapter;
import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.OrdersPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


import stutzen.co.network.Connection;


public class ReturnlistActivity extends Language {

    private ListView order;
    private ArrayList<OrdersPO> list;
    private FrameLayout error_network;
    private LinearLayout empty;
    private TextView cart_count;
    private FrameLayout loading;
    private FrameLayout fullayout;
    private TextView errortxt1;
    private TextView errortxt2;
    private TextView no_order;
    private AndroidLogger logger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        LinearLayout back = findViewById(R.id.menu);

        error_network = findViewById(R.id.error_network);
        DBController db = new DBController(ReturnlistActivity.this);
        LinearLayout retry = findViewById(R.id.retry);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cart_count = findViewById(R.id.cart_count);
        TextView header = findViewById(R.id.header);
        header.setText(R.string.returns_title);
        order = findViewById(R.id.order_list);
        loading = findViewById(R.id.loading);
        empty = findViewById(R.id.empty);
        LinearLayout shopnow = findViewById(R.id.shopnow);
        fullayout = findViewById(R.id.fullayout);
        empty.setVisibility(View.GONE);
        errortxt1 = findViewById(R.id.errortxt1);
        errortxt2 = findViewById(R.id.errortxt2);
        no_order = findViewById(R.id.no_order);
        no_order.setText(R.string.no_orders1);
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        Appconstatants.Lang = db.get_lang_code();
        if (Appconstatants.sessiondata == null)
            Appconstatants.sessiondata = db.getSession();
        Appconstatants.CUR = db.getCurCode();

        OrderTask orderTask = new OrderTask();
        orderTask.execute(Appconstatants.Return_List, Appconstatants.sessiondata);
        shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReturnlistActivity.this, MainActivity.class));
            }
        });


        order.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ReturnlistActivity.this, ReturnViewDetails.class);

                Bundle bundle = new Bundle();
                bundle.putString("id", list.get(i).getReturn_id());
                bundle.putString("status", list.get(i).getOrder_status());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                OrderTask orderTask = new OrderTask();
                orderTask.execute(Appconstatants.Return_List, Appconstatants.sessiondata);
            }
        });
        LinearLayout cart_items = findViewById(R.id.cart_items);
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReturnlistActivity.this, MyCart.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && data != null) {

            loading.setVisibility(View.VISIBLE);
            error_network.setVisibility(View.GONE);
            CartTask cartTask = new CartTask();
            cartTask.execute(Appconstatants.cart_api);
            OrderTask orderTask = new OrderTask();
            orderTask.execute(Appconstatants.Return_List, Appconstatants.sessiondata);
        }
    }

    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Cart api:" + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ReturnlistActivity.this);
                logger.info("Cart resp" + response);
                Log.d("Cart_list_resp", response);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Cart_list_resp", "CartResp--->  " + resp);

            if (resp != null) {

                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONArray) {
                            cart_count.setText(String.valueOf(0));

                        } else if (dd instanceof JSONObject) {

                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(String.valueOf(qty));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }


    private class OrderTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Order", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Order list api" + Appconstatants.Return_List);
            Log.d("Order_url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(Appconstatants.Return_List, Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, ReturnlistActivity.this);
                logger.info("Order list api resp" + response);
                Log.d("Order_resp", response);
                Log.d("url", Appconstatants.Lang);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Myorder", "order_Resp--->" + resp);

            if (resp != null) {

                try {
                    list = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Object dd = json.get("data");
                        if (dd instanceof JSONObject) {
                            error_network.setVisibility(View.GONE);
                            empty.setVisibility(View.VISIBLE);
                        } else if (dd instanceof JSONArray) {
                            JSONArray arr = new JSONArray(json.getString("data"));

                            if (arr.length() == 0) {

                                error_network.setVisibility(View.GONE);
                                empty.setVisibility(View.VISIBLE);
                                loading.setVisibility(View.GONE);
                            } else {

                                for (int h = 0; h < arr.length(); h++) {
                                    JSONObject obj = arr.getJSONObject(h);
                                    OrdersPO bo = new OrdersPO();
                                    bo.setOrder_id(obj.isNull("order_id") ? "" : obj.getString("order_id"));
                                    bo.setReturn_id(obj.isNull("return_id") ? "" : obj.getString("return_id"));
                                    bo.setOrder_name(obj.isNull("name") ? "" : obj.getString("name"));
                                    bo.setOrder_status(obj.isNull("status") ? "" : obj.getString("status"));
                                    bo.setOrder_date(obj.isNull("date_added") ? "" : obj.getString("date_added"));
                                    list.add(bo);
                                }
                                ReturnlistAdapter adapter = new ReturnlistAdapter(ReturnlistActivity.this, R.layout.return_list, list);
                                order.setAdapter(adapter);
                            }
                            loading.setVisibility(View.GONE);
                            error_network.setVisibility(View.GONE);
                        }
                    } else {
                        JSONArray array = json.getJSONArray("error");

                        if (array.getString(0).equalsIgnoreCase("User is not logged in")) {

                            loading.setVisibility(View.GONE);
                            Intent i = new Intent(ReturnlistActivity.this, Login.class);
                            i.putExtra("from", 7);
                            startActivityForResult(i, 7);
                            finish();
                            Toast.makeText(ReturnlistActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                        } else {
                            loading.setVisibility(View.GONE);
                            error_network.setVisibility(View.VISIBLE);
                            errortxt1.setText(R.string.error_msg);
                            String error = array.getString(0) + "";
                            errortxt2.setText(error);
                            Toast.makeText(ReturnlistActivity.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                        }
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    error_network.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading.setVisibility(View.VISIBLE);
                                    OrderTask orderTask = new OrderTask();
                                    orderTask.execute(Appconstatants.Return_List, Appconstatants.sessiondata);

                                }
                            })
                            .show();


                }

            } else {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);

    }
}
