package com.iamretailer;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.BrandzAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.DBController;
import com.iamretailer.Common.RecyclerItemClickListener;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.POJO.BrandsPO;

import stutzen.co.network.Connection;


public class Category extends Language {

    RecyclerView cat_list;
    ArrayList<BrandsPO> cate_list;
    BrandzAdapter adapter1;
    FrameLayout rootlay;
    TextView cart_count;
    LinearLayout cart_items;
    FrameLayout loading;
    TextView header;
    LinearLayout menu;
    FrameLayout error_network;
    LinearLayout retry;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    AndroidLogger logger;
    DBController dbController;
    private int width;
    private int height;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger= AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        dbController=new DBController(Category.this);
        Appconstatants.sessiondata=dbController.getSession();
        Appconstatants.Lang=dbController.get_lang_code();
        Appconstatants.CUR=dbController.getCurCode();
        cat_list = (RecyclerView) findViewById(R.id.cat_list);
        rootlay = (FrameLayout) findViewById(R.id.rootlay);
        cart_count = (TextView) findViewById(R.id.cart_count);
        cart_items = (LinearLayout) findViewById(R.id.cart_items);
        loading = (FrameLayout) findViewById(R.id.loading);
        retry=(LinearLayout)findViewById(R.id.retry);
        Cat_Task cat_task = new Cat_Task();
        cat_task.execute(Appconstatants.CAT_LIST);
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        header=(TextView)findViewById(R.id.header);
        menu=(LinearLayout)findViewById(R.id.menu);
        error_network=(FrameLayout)findViewById(R.id.error_network);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        header.setText(R.string.our_cat);
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = ((display.getHeight()));
        cat_list.addOnItemTouchListener(new RecyclerItemClickListener(Category.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent u = new Intent(Category.this, Allen.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", cate_list.get(position).getS_id());
                bundle.putString("cat_name",cate_list.get(position).getStore_name());
                u.putExtras(bundle);
                startActivity(u);

            }
        }));
        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Category.this, MyCart.class));
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartTask cartTask = new CartTask();
                cartTask.execute(Appconstatants.cart_api);
                Cat_Task cat_task = new Cat_Task();
                cat_task.execute(Appconstatants.CAT_LIST);
                error_network.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
            }
        });


    }

    private class Cat_Task extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.d("tag", "started");
        }
        protected String doInBackground(String... param) {
            logger.info("Category api"+param[0]);

            Log.d("url", param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Category.this);
                logger.info("Category resp"+response);
                Log.d("url response", response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "Hai--->" + resp);
            if (resp != null) {

                try {
                    cate_list = new ArrayList<BrandsPO>();

                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success")==1)
                    {
                        JSONArray arr = new JSONArray(json.getString("data"));
                        for (int h = 0; h < arr.length(); h++) {
                            JSONObject obj = arr.getJSONObject(h);
                            BrandsPO bo = new BrandsPO();
                            bo.setS_id(obj.isNull("category_id") ? "" : obj.getString("category_id"));
                            bo.setStore_name(obj.isNull("name") ? "" : obj.getString("name"));
                            bo.setBg_img_url(obj.isNull("image") ? "" : obj.getString("image"));
                            cate_list.add(bo);

                        }

                       adapter1 = new BrandzAdapter(Category.this, cate_list, 2,width,height);
                        cat_list.setAdapter(adapter1);
                        cat_list.setLayoutManager(new GridLayoutManager(Category.this,3));
                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);
                    }
                    else
                        {
                         loading.setVisibility(View.GONE);
                            error_network.setVisibility(View.VISIBLE);
                            errortxt1.setText(R.string.error_msg);
                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(Category.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(rootlay, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    Cat_Task cat_task = new Cat_Task();
                                    cat_task.execute(Appconstatants.CAT_LIST);

                                }
                            })
                            .show();

                }

            } else {
                loading.setVisibility(View.GONE);
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
            }
        }
    }

    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            Log.d("Cart_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Cart api"+param[0]);
            String response = null;
            try {
                Connection connection = new Connection();
                Log.d("Cart_list_url", param[0]);
                Log.d("Cart_url_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value,Appconstatants.Lang,Appconstatants.CUR,Category.this);
                logger.info("Cart resp"+response);
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
                            cart_count.setText(0 + "");

                        } else if (dd instanceof JSONObject) {

                            JSONObject jsonObject = (JSONObject) dd;
                            JSONArray array = new JSONArray(jsonObject.getString("products"));

                            int qty = 0;
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonObject1 = array.getJSONObject(i);
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                            }
                            cart_count.setText(qty + "");
                        }
                    }
                    else
                    {
                        JSONArray array=json.getJSONArray("error");
                        Toast.makeText(Category.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
