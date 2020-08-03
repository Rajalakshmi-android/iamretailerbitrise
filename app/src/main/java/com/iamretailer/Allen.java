package com.iamretailer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.all.All;
import com.iamretailer.Adapter.CommonAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.iamretailer.Common.RecyclerItemClickListener;
import com.iamretailer.POJO.BrandsPO;
import com.iamretailer.POJO.SingleOptionPO;
import com.logentries.android.AndroidLogger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.ProductsPO;

import stutzen.co.network.Connection;


public class Allen extends Language {
    private RecyclerView category;
    CommonAdapter adapter;
    ArrayList<ProductsPO> list;
    TextView product_count;
    LinearLayout cart_items;
    DBController db;
    public TextView cart_count;
    String id;
    Bundle bundle;
    FrameLayout prog_sec;
    LinearLayout menu;
    FrameLayout error_network;
    LinearLayout retry;
    LinearLayout sort;
    private TextView sort_name;
    private String sort_option = "";
    private String sort_order = "";
    private int cat_id;
    private int from;
    TextView no_items;
    FrameLayout loading;
    private boolean scrollValue;
    private boolean scrollNeed = true;
    private int start = 1, limit = 10;
    LinearLayout load_more;
    int val = 0;
    TextView header;
    FrameLayout fullayout;
    ArrayList<SingleOptionPO> optionPOS;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    AndroidLogger logger;
    private ArrayList<ProductsPO> fav_item;
    private ArrayList<ProductsPO> cart_item;
    private boolean loadin = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    GridLayoutManager mLayoutManager;
    LinearLayout filter;
    Dialog alertReviewDialog;
    LinearLayout  cancels,  filter_load,apply;
    FrameLayout cancel;
    EditText filter_edit;
    RecyclerView filter_list;

    TextView no_brands;
    Boolean value=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allen);
        CommonFunctions.updateAndroidSecurityProvider(this);
        db = new DBController(getApplicationContext());
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang = db.get_lang_code();
        Appconstatants.CUR = db.getCurCode();
        logger = AndroidLogger.getLogger(getApplicationContext(), Appconstatants.LOG_ID, false);
        category = (RecyclerView) findViewById(R.id.category_list);
        sort_name = (TextView) findViewById(R.id.sort_name);
        product_count = (TextView) findViewById(R.id.product_count);
        cart_items = (LinearLayout) findViewById(R.id.cart_items);
        cart_count = (TextView) findViewById(R.id.cart_count);
        menu = (LinearLayout) findViewById(R.id.menu);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        retry = (LinearLayout) findViewById(R.id.retry);
        sort = (LinearLayout) findViewById(R.id.sort);
        prog_sec = (FrameLayout) findViewById(R.id.prog_sec);
        no_items = (TextView) findViewById(R.id.no_items);
        loading = (FrameLayout) findViewById(R.id.loading);
        load_more = (LinearLayout) findViewById(R.id.load_more);
        header = (TextView) findViewById(R.id.header);
        fullayout = (FrameLayout) findViewById(R.id.fullayout);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar = (LinearLayout) findViewById(R.id.loading_bar);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        cat_id = Integer.parseInt(bundle.getString("id"));
        header.setText(bundle.getString("cat_name"));
        Log.i("tag", "cad_id..." + cat_id);
        sort_option = "date_added";
        sort_order = "DESC";
        sort_name.setText(R.string.news);


        ProductTask productTask = new ProductTask();
        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id +"&page=" + start + "&limit=" + limit);


        category.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    Log.d("Scroll_Check1", "adfadsdsa");
                    visibleItemCount = category.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                    Log.d("Scroll_Check2", "adfadsdsa");
                    Log.d("Scroll_Check2", visibleItemCount + "adfadsdsa" + totalItemCount + "  " + firstVisibleItem);

                    if (!loadin) {
                        if ((visibleItemCount + firstVisibleItem) >= (start - 1) * limit) {
                            loadin = true;
                            val = 1;
                            load_more.setVisibility(View.VISIBLE);
                            ProductTask productTask = new ProductTask();
                            productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id +"&page=" + start + "&limit=" + limit);
                        }
                    }
                }
            }


        });



        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(Allen.this);
                View sheetView = Allen.this.getLayoutInflater().inflate(R.layout.sortview, null);
                mBottomSheetDialog.setContentView(sheetView);
                LinearLayout htl, lth, newest, poupular, nameaz, nameza, modelaz, modelza;
                lth = (LinearLayout) sheetView.findViewById(R.id.low_to_high);
                htl = (LinearLayout) sheetView.findViewById(R.id.high_to_low);
                newest = (LinearLayout) sheetView.findViewById(R.id.newest);
                nameaz = (LinearLayout) sheetView.findViewById(R.id.nameaz);
                nameza = (LinearLayout) sheetView.findViewById(R.id.nameza);
                modelaz = (LinearLayout) sheetView.findViewById(R.id.modelaz);
                modelza = (LinearLayout) sheetView.findViewById(R.id.modelza);
                poupular = (LinearLayout) sheetView.findViewById(R.id.popular);
                mBottomSheetDialog.show();

                nameaz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "name";
                        sort_order = "ASC";
                        sort_name.setText(R.string.atoz);
                        val = 0;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit);

                        mBottomSheetDialog.dismiss();
                    }
                });

                nameza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "name";
                        sort_order = "DESC";
                        sort_name.setText(R.string.ztoa);
                        val = 0;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id +"&page=" + start + "&limit=" + limit);
                        mBottomSheetDialog.dismiss();
                    }
                });

                modelaz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "model";
                        sort_order = "ASC";
                        sort_name.setText(R.string.matoz);
                        val = 0;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit);

                        mBottomSheetDialog.dismiss();
                    }
                });

                modelza.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "model";
                        sort_order = "DESC";
                        val = 0;
                        start = 1;
                        sort_name.setText(R.string.mztoa);
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit);

                        mBottomSheetDialog.dismiss();
                    }
                });

                lth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        mBottomSheetDialog.dismiss();
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "price";
                        sort_order = "ASC";
                        val = 0;
                        start = 1;
                        sort_name.setText(R.string.lowtohigh);

                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit);
                    }
                });
                htl.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "price";
                        sort_order = "DESC";
                        val = 0;
                        start = 1;
                        sort_name.setText(R.string.hightolow);
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit);

                        mBottomSheetDialog.dismiss();


                    }
                });
                newest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        mBottomSheetDialog.dismiss();
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "date_added";
                        sort_order = "DESC";
                        sort_name.setText(R.string.news);
                        val = 0;
                        start = 1;
                        ProductTask productTask = new ProductTask();

                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id + "&page=" + start + "&limit=" + limit);


                    }
                });
                poupular.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        no_items.setVisibility(View.GONE);
                        mBottomSheetDialog.dismiss();
                        prog_sec.setVisibility(View.VISIBLE);
                        sort_option = "rating";
                        sort_order = "DESC";
                        sort_name.setText(R.string.popular);
                        val = 0;
                        start = 1;
                        ProductTask productTask = new ProductTask();
                        productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id +"&page=" + start + "&limit=" + limit);

                    }
                });

            }
        });

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error_network.setVisibility(View.GONE);
                sort.setVisibility(View.VISIBLE);
                loading.setVisibility(View.VISIBLE);
                no_items.setVisibility(View.GONE);
                ProductTask productTask = new ProductTask();
                productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id +"&page=" + start + "&limit=" + limit);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cart_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Allen.this, MyCart.class));
            }
        });


    }



    @Override
    public void onResume() {
        super.onResume();
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
        if (db.getLoginCount() > 0) {
            WISH_LIST wish_list = new WISH_LIST();
            wish_list.execute(Appconstatants.Wishlist_Get);
        }


    }


    private class ProductTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Product List api" + param[0]);
            Log.i("tag", "Allen_api--- " + param[0]);

            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Allen.this);
                logger.info("Product List resp" + response);
                Log.d("check_ses", Appconstatants.sessiondata + "");

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            load_more.setVisibility(View.GONE);
            scrollNeed = true;
            if (resp != null) {
                try {

                    if (val == 0) {
                        list = new ArrayList<>();
                        Log.d("check_ar_in", val + "");
                        Log.d("check_pagecount", start + "");
                    }

                    JSONObject json = new JSONObject(resp);

                    if (json.getInt("success") == 1) {

                        JSONArray arr = new JSONArray(json.getString("data"));

                        for (int h = 0; h < arr.length(); h++) {
                            JSONObject obj = arr.getJSONObject(h);
                            ProductsPO bo = new ProductsPO();
                            bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                            bo.setProduct_name(obj.isNull("name") ? "" : obj.getString("name"));
                            bo.setProd_original_rate(Double.parseDouble(obj.isNull("price") ? "" : obj.getString("price")));
                            bo.setProd_offer_rate(Double.parseDouble(obj.isNull("special") ? "" : obj.getString("special")));
                            bo.setProducturl(obj.isNull("image") ? "" : obj.getString("image"));
                            bo.setQty(obj.isNull("quantity") ? 0 : obj.getInt("quantity"));
                            bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                            bo.setWeight(obj.isNull("weight") ? "" : obj.getString("weight"));
                            bo.setManufact(obj.isNull("manufacturer") ? "" : obj.getString("manufacturer"));

                            bo.setP_rate(obj.isNull("rating") ? 0 : obj.getDouble("rating"));
                            bo.setWish_list(false);
                            bo.setCart_list(false);
                            optionPOS = new ArrayList<>();
                            if (obj.getJSONArray("options").length() > 0) {

                                JSONArray arr1 = obj.getJSONArray("options");
                                for (int k = 0; k < arr1.length(); k++) {
                                    JSONObject object = arr1.getJSONObject(k);
                                    SingleOptionPO po = new SingleOptionPO();
                                    po.setProduct_option_value_id(object.isNull("product_option_id") ? 0 : object.getInt("product_option_id"));
                                    optionPOS.add(po);

                                }
                                bo.setSingleOptionPOS(optionPOS);
                            } else {
                                bo.setSingleOptionPOS(optionPOS);
                            }
                            list.add(bo);
                        }
                        Log.d("check_list_size", list.size() + "");


                        if (list.size() != 0) {
                            if (val == 0) {
                                adapter = new CommonAdapter(Allen.this, list, 0, 4);
                                mLayoutManager = new GridLayoutManager(Allen.this, 2);
                                category.setLayoutManager(mLayoutManager);
                                category.setAdapter(adapter);
                            } else {
                                adapter.notifyDataSetChanged();
                                no_items.setVisibility(View.GONE);
                            }
                            product_count.setText(String.valueOf(list.size()));
                            category.setVisibility(View.VISIBLE);
                            no_items.setVisibility(View.GONE);
                            load_more.setVisibility(View.GONE);

                        } else {
                            product_count.setText(String.valueOf(list.size()));
                            no_items.setVisibility(View.VISIBLE);
                            category.setVisibility(View.GONE);

                        }

                        error_network.setVisibility(View.GONE);
                        load_more.setVisibility(View.GONE);
                        start = start + 1;
                        prog_sec.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        loading_bar.setVisibility(View.GONE);



                        CartTask cartTask = new CartTask();
                        cartTask.execute(Appconstatants.cart_api);

                    } else {
                        error_network.setVisibility(View.VISIBLE);
                        prog_sec.setVisibility(View.GONE);
                        loading.setVisibility(View.GONE);
                        errortxt1.setText(R.string.error_msg);
                        JSONArray array = json.getJSONArray("error");
                        errortxt2.setText(array.getString(0) + "");
                        Toast.makeText(Allen.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    prog_sec.setVisibility(View.GONE);
                    error_network.setVisibility(View.GONE);
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE).setActionTextColor(getResources().getColor(R.color.colorAccent))
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    ProductTask productTask = new ProductTask();
                                    productTask.execute(Appconstatants.PRODUCT_LIST + "&sort=" + sort_option + "&order=" + sort_order + "&category=" + cat_id);
                                }
                            })
                            .show();


                }
            } else {
                errortxt1.setText(R.string.no_con);
                errortxt2.setText(R.string.check_network);
                error_network.setVisibility(View.VISIBLE);
                prog_sec.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                category.setVisibility(View.GONE);

            }

        }
    }




    private class CartTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

        }

        protected String doInBackground(String... param) {

            logger.info("Cart api:" + param[0]);
            Log.i("Cart_api:", "" + param[0]);
            String response = null;
            try {
                Connection connection = new Connection();

                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Allen.this);
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
                    cart_item = new ArrayList<>();
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
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(jsonObject1.isNull("product_id") ? "" : jsonObject1.getString("product_id"));
                                qty = qty + (Integer.parseInt(jsonObject1.isNull("quantity") ? "" : jsonObject1.getString("quantity")));
                                cart_item.add(bo);
                            }


                            cart_count.setText(qty + "");
                            if (list.size() > 0 && list != null) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < cart_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(cart_item.get(h).getProduct_id())) {
                                            list.get(u).setCart_list(true);
                                            break;
                                        } else {
                                            list.get(u).setCart_list(false);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }

                        }

                    } else {
                        JSONArray array = json.getJSONArray("error");
                        Toast.makeText(Allen.this, array.getString(0) + "", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

        }
    }

    private class WISH_LIST extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Tag", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("WIsh list api" + param[0]);


            String response = null;
            try {
                Connection connection = new Connection();
                response = connection.connStringResponse(param[0], Appconstatants.sessiondata, Appconstatants.key1, Appconstatants.key, Appconstatants.value, Appconstatants.Lang, Appconstatants.CUR, Allen.this);
                logger.info("WIsh list api resp" + response);
                Log.d("wish_api", param[0]);
                Log.d("wish_res", response + "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("tag", "products_Hai--->  " + resp);
            if (resp != null) {
                try {
                    fav_item = new ArrayList<>();
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONArray array = json.getJSONArray("data");
                        Log.d("wish_res", "ddsadsa");

                        if (array.length() > 0) {
                            for (int h = 0; h < array.length(); h++) {
                                JSONObject obj = array.getJSONObject(h);
                                ProductsPO bo = new ProductsPO();
                                bo.setProduct_id(obj.isNull("product_id") ? "" : obj.getString("product_id"));
                                fav_item.add(bo);
                            }

                            if (list.size() > 0) {
                                for (int u = 0; u < list.size(); u++) {
                                    for (int h = 0; h < fav_item.size(); h++) {
                                        if (Integer.parseInt(list.get(u).getProduct_id()) == Integer.parseInt(fav_item.get(h).getProduct_id())) {
                                            list.get(u).setWish_list(true);
                                            break;
                                        } else {
                                            list.get(u).setWish_list(false);
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void cart_inc() {
        CartTask cartTask = new CartTask();
        cartTask.execute(Appconstatants.cart_api);
    }
}