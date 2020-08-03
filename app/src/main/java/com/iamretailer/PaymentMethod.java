package com.iamretailer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.iamretailer.Adapter.PaymentAdapter;
import com.iamretailer.Common.CommonFunctions;
import com.logentries.android.AndroidLogger;

import com.iamretailer.Common.Appconstatants;
import com.iamretailer.Common.DBController;
import com.iamretailer.POJO.TypePO;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import stutzen.co.network.Connection;

public class PaymentMethod extends Language {
    LinearLayout cont;
    FrameLayout error_network;
    LinearLayout retry;
    EditText comments;
    DBController db;
    Bundle bundle;
    String cus_id;

    private ArrayList<TypePO> paylist;
    private String fname;
    private String lname;
    private String company;
    private String addressone;
    private String addresstwo;
    private String city;
    private String pincode;
    private String country;
    private String mobile;
    private String state;
    LinearLayout back;
    TextView header;
    FrameLayout loading;
    LinearLayout cart_items;
    Typeface radio_text;
    FrameLayout fullayout;
    TextView errortxt1, errortxt2;
    LinearLayout loading_bar;
    String address_id;
    String shipping;
    AndroidLogger logger;
    private int from=0;
    private ListView payment_list;
    private PaymentAdapter payadapter;
    private String paycode="";
    private String paymentname="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        CommonFunctions.updateAndroidSecurityProvider(this);
        logger=AndroidLogger.getLogger(getApplicationContext(),Appconstatants.LOG_ID,false);
        db = new DBController(PaymentMethod.this);
        Appconstatants.sessiondata = db.getSession();
        Appconstatants.Lang=db.get_lang_code();
        Appconstatants.CUR=db.getCurCode();
        fname = getIntent().getExtras().getString("fname");
        from = getIntent().getExtras().getInt("from");
        lname = getIntent().getExtras().getString("lname");
        company = getIntent().getExtras().getString("company");
        addressone = getIntent().getExtras().getString("addressone");
        addresstwo = getIntent().getExtras().getString("addresstwo");
        city = getIntent().getExtras().getString("city");
        pincode = getIntent().getExtras().getString("pincode");
        country = getIntent().getExtras().getString("country");
        state = getIntent().getExtras().getString("state");
        mobile=getIntent().getExtras().getString("mobile");
        address_id=getIntent().getExtras().getString("addres_id");
        shipping=getIntent().getExtras().getString("shipping");
        Log.d("Session", Appconstatants.sessiondata + "");
        back = (LinearLayout) findViewById(R.id.menu);
        cont = (LinearLayout) findViewById(R.id.cont);
        comments = (EditText) findViewById(R.id.comments);
        error_network = (FrameLayout) findViewById(R.id.error_network);
        header = (TextView) findViewById(R.id.header);
        header.setText(R.string.pay_head);
        retry = (LinearLayout) findViewById(R.id.retry);
        loading = (FrameLayout) findViewById(R.id.loading);
        cart_items = (LinearLayout) findViewById(R.id.cart_items);
        fullayout=(FrameLayout)findViewById(R.id.fullayout);
        cart_items.setVisibility(View.GONE);
        errortxt1 = (TextView) findViewById(R.id.errortxt1);
        errortxt2 = (TextView) findViewById(R.id.errortxt2);
        loading_bar=(LinearLayout)findViewById(R.id.loading_bar);
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        radio_text = Typeface.createFromAsset(getAssets(), "font/Heebo-Regular.ttf");
        payment_list = (ListView) findViewById(R.id.payment_list);
        payment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                payadapter.setChild1(arg2);
                payadapter.notifyDataSetChanged();

                //if(paylist.get(arg2).getPaymentname().equalsIgnoreCase("Check / Money order")){
                paycode = paylist.get(arg2).getCode();
                paymentname = paylist.get(arg2).getTitle();
                Log.i("asdsadasasdasd sadsadas",paylist.get(arg2).getTitle()+"");

            }
        });





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        PaymentListTask paymentListTask = new PaymentListTask();
        paymentListTask.execute();


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (paylist != null && paylist.size() > 0) {
                    PaymentMethodTask paymentMethodTask = new PaymentMethodTask();
                    paymentMethodTask.execute(paycode, 1 + "", "");
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.pay_type), Toast.LENGTH_SHORT).show();
                }
            }
        });
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loading.setVisibility(View.VISIBLE);
                error_network.setVisibility(View.GONE);

                PaymentListTask paymentListTask = new PaymentListTask();
                paymentListTask.execute();
            }
        });
    }

    private class PaymentListTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            Log.d("Payment_list", "started");
        }

        protected String doInBackground(String... param) {
            logger.info("Payment_list api:"+Appconstatants.payment_list);

            String response = null;
            Connection connection = new Connection();
            try {
                Log.d("Payment_list", Appconstatants.payment_list);
                Log.d("Payment_list", Appconstatants.sessiondata);
                response = connection.connStringResponse(Appconstatants.payment_list, Appconstatants.sessiondata,  Appconstatants.key1,Appconstatants.key,Appconstatants.value, Appconstatants.Lang,Appconstatants.CUR,PaymentMethod.this);
                logger.info("Payment_list api resp:"+response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("Payment_list", "Payment_list--" + resp);
            if (resp != null) {
                paylist = new ArrayList<TypePO>();
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        JSONObject jsonObject = new JSONObject(json.getString("data"));
                        JSONArray object = jsonObject.getJSONArray("payment_methods");

                        for (int i = 0; i < object.length(); i++) {
                            JSONObject obj2 = object.getJSONObject(i);
                            TypePO bo = new TypePO();
                            bo.setCode(obj2.isNull("code") ? "" : obj2.getString("code"));
                            bo.setTitle(obj2.isNull("title") ? "" : obj2.getString("title"));
                            paylist.add(bo);
                            Log.i("obj2", obj2.toString() + "");
                        }


                        payadapter = new PaymentAdapter(
                                getApplicationContext(), R.layout.payment_item, paylist,0);
                        payment_list.setAdapter(payadapter);



                        if (paylist != null) {
                            if(paylist.size()>0){
                                paycode = paylist.get(0).getCode();
                                paymentname = paylist.get(0).getTitle();
                                payadapter.setChild1(0);
                                payadapter.notifyDataSetChanged();
                            }
                        }



                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.GONE);

                    } else {

                        loading.setVisibility(View.GONE);
                        error_network.setVisibility(View.VISIBLE);
                        errortxt1.setText(R.string.error_msg);

                        JSONArray array=json.getJSONArray("error");
                        errortxt2.setText(array.getString(0)+"");
                        Toast.makeText(PaymentMethod.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    loading.setVisibility(View.VISIBLE);
                    loading_bar.setVisibility(View.GONE);
                    error_network.setVisibility(View.GONE);
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_INDEFINITE)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    loading_bar.setVisibility(View.VISIBLE);
                                    PaymentListTask paymentListTask = new PaymentListTask();
                                    paymentListTask.execute();
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

    public static JSONObject objectToJSONObject(Object object) {
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    private class PaymentMethodTask extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(PaymentMethod.this);
            pDialog.setMessage(getResources().getString(R.string.loading_wait));
            pDialog.setCancelable(false);
            pDialog.show();
            Log.d("PaymentMethodTask", "started");
        }

        protected String doInBackground(String... param) {

            logger.info("Payment method save api"+Appconstatants.payment_method_api);

            String response = null;
            Connection connection = new Connection();
            try {
                JSONObject json = new JSONObject();
                json.put("payment_method", param[0]);
                json.put("agree", param[1]);
                json.put("comment", param[2]);
                Log.d("Login_format", json.toString());
                logger.info("Payment method save api req"+json);
                response = connection.sendHttpPostjson(Appconstatants.payment_method_api, json, Appconstatants.sessiondata, Appconstatants.key1,Appconstatants.key,Appconstatants.value, Appconstatants.Lang,Appconstatants.CUR,PaymentMethod.this);
                logger.info("Payment method save api resp"+response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return response;
        }

        protected void onPostExecute(String resp) {
            Log.i("PaymentMethodTask", "PaymentMethodTask--resp" + resp);
            pDialog.dismiss();
            if (resp != null) {
                try {
                    JSONObject json = new JSONObject(resp);
                    if (json.getInt("success") == 1) {
                        Intent i = new Intent(PaymentMethod.this, ConfirmOrder.class);
                        i.putExtra("address_id",address_id);
                        i.putExtra("fname", fname);
                        i.putExtra("from", from);
                        i.putExtra("lname", lname);
                        i.putExtra("company", company);
                        i.putExtra("addressone", addressone);
                        i.putExtra("addresstwo", addresstwo);
                        i.putExtra("city", city);
                        i.putExtra("pincode", pincode);
                        i.putExtra("country", country);
                        i.putExtra("state", state);
                        i.putExtra("mobile", mobile);
                        i.putExtra("payment_method",paymentname);
                        i.putExtra("shipping",shipping);
                        startActivity(i);

                    } else {

                        JSONArray array=json.getJSONArray("error");
                        Toast.makeText(PaymentMethod.this,array.getString(0)+"",Toast.LENGTH_SHORT).show();
                        Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                                .setAction(R.string.retry, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        cont.performClick();
                                    }
                                })
                                .show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(fullayout, R.string.error_msg, Snackbar.LENGTH_LONG)
                            .setAction(R.string.retry, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    cont.performClick();
                                }
                            })
                            .show();
                }

            } else {
                Snackbar.make(fullayout, R.string.error_net, Snackbar.LENGTH_LONG)
                        .setAction(R.string.retry, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                cont.performClick();
                            }
                        })
                        .show();
            }
        }
    }

}